package com.duoyou.develop.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.StringUtils;
import com.duoyou.develop.base.BaseCompatActivity;
import com.duoyou.develop.utils.SPManager;
import com.duoyou.develop.utils.WebViewUtils;
import com.duoyou.develop.utils.eventbus.EventBusUtils;
import com.duoyou.develop.utils.eventbus.PageRefreshEvent;
import com.duoyou.develop.utils.eventbus.WebRefreshEvent;
import com.duoyou.develop.utils.http.HttpHeaderUtil;
import com.duoyou.develop.utils.java_impi.JavaScriptInterfaceImpl;
import com.duoyou.develop.view.MyWebView;
import com.duoyou.develop.view.VerticalSwipeRefreshLayout;
import com.duoyou_cps.appstore.R;
import com.duoyou_cps.appstore.util.httputils.HttpUrlKtKt;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DownLoadWebViewActivity extends BaseCompatActivity {
    public static final String WEB_VIEW_JAVA_IMP_TAG = "cps";
    public static final String WEB_VIEW_DOWNLOAD = "download";

    private VerticalSwipeRefreshLayout verticalSwipeRefreshLayout;
    private MyWebView webView;
    private String url;
    private JavaScriptInterfaceImpl javaScriptInterface;
//    private final JavaScriptDownload scriptDownload = new JavaScriptDownload(this) {
//        @Override
//        public void updateTaskProgress(@NonNull DownloadTask task) {
//            webView.loadUrl(String.format("javascript:callDownloadTaskProgress(%s)", GsonUtil.toJson(task)));
//        }
//    };


    @Override
    public int getLayoutId() {
        return R.layout.lib_act_web_view;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        verticalSwipeRefreshLayout = findViewById(R.id.web_view_layout);
        verticalSwipeRefreshLayout.setRefreshing(true);
        webView = findViewById(R.id.web_view);
        WebViewUtils.initWebViewSettings(getActivity(), webView);
        WebViewUtils.setAndroidNativeLightStatusBar(this, true);
        javaScriptInterface = new JavaScriptInterfaceImpl(getActivity(), webView);
        webView.addJavascriptInterface(javaScriptInterface, WEB_VIEW_JAVA_IMP_TAG);
//        webView.addJavascriptInterface(scriptDownload, WEB_VIEW_DOWNLOAD);
        verticalSwipeRefreshLayout.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        setText(titleTv, title.toString());
    }

    @Override
    public void initData() {
        url = getIntent().getStringExtra("url");

        if (StringUtils.isEmpty(url)) {
            finish();
            return;
        }
        String newUrl = url.replace("#", "");
        Uri uri = Uri.parse(newUrl);
        String showAppTitle = uri.getQueryParameter("showAppTitle");
        String setMarginTop = uri.getQueryParameter("setMarginTop");

        if (!StringUtils.isEmpty(showAppTitle)) {
            if ("1".equals(showAppTitle)) {
                BarUtils.setStatusBarLightMode(this, true);
                findViewById(R.id.title_bar_layout).setVisibility(View.VISIBLE);
            } else {
                WebViewUtils.setAndroidNativeLightStatusBar(getActivity(), true);
            }
        } else if (!StringUtils.isEmpty(setMarginTop)) {
            if ("1".equals(setMarginTop)) {
//                setStatusBar();
                setStatusBar(findViewById(R.id.view_web_status_bar));
            }
        }

        url = HttpUrlKtKt.getUrlWithParams(url);
        webView.postDelayed(() -> {
            boolean value = SPManager.getValue(SPManager.AGREE_USER_PROTOCOL, false);
            if (value) {
                Map<String, String> headMap = new HashMap<>();
                headMap.put("headerInfo", HttpHeaderUtil.getHeaderInfoJsonStringBase64());
                webView.loadUrl(url, headMap);
                return;
            }
            webView.loadUrl(url);
        }, 800);
    }


    public void onReceivedTitle(String title) {
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }


    @Override
    public void initListener() {


        webView.setWebViewClient(new WebViewClient() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://")) {
                    return super.shouldOverrideUrlLoading(webView, url);
                }

//                /**
//                 * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
//                 */
//                final PayTask task = new PayTask(WebViewActivity.this);
//                boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
//                    @Override
//                    public void onPayResult(final H5PayResultModel result) {
//                        final String url = result.getReturnUrl();
//                        if (!TextUtils.isEmpty(url)) {
//                            WebViewActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    view.loadUrl(url);
//                                }
//                            });
//                        }
//                    }
//                });

//                /**
//                 * 判断是否成功拦截
//                 * 若成功拦截，则无需继续加载该URL；否则继续加载
//                 */
//                if (!isIntercepted) {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Map<String, String> requestHeaders = request.getRequestHeaders();
                if (requestHeaders != null) {
                    requestHeaders.put("headerInfo", HttpHeaderUtil.getHeaderInfoJsonString());
                }
                return super.shouldOverrideUrlLoading(view, request);
            }


            //            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                verticalSwipeRefreshLayout.setRefreshing(false);
                verticalSwipeRefreshLayout.setEnabled(false);


                String boxsInfo = getIntent().getStringExtra("boxsInfo");
                if (!isOpenBox && !StringUtils.isEmpty(boxsInfo)) {
                    isOpenBox = true;
                    webView.loadUrl("javascript:boxJson('" + boxsInfo + "')");
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                verticalSwipeRefreshLayout.setRefreshing(false);
                verticalSwipeRefreshLayout.setEnabled(false);
                super.onReceivedError(view, errorCode, description, failingUrl);

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

        });


        webView.setViewGroup(verticalSwipeRefreshLayout);
        verticalSwipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> webView.getScrollY() > 0);

        verticalSwipeRefreshLayout.setOnRefreshListener(() -> webView.reload());

    }

    private boolean isOpenBox = false;

    @Override
    public void refresh() {
        verticalSwipeRefreshLayout.setEnabled(true);
        verticalSwipeRefreshLayout.setRefreshing(true);
        webView.reload();
        hideErrorPage();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        try {
            javaScriptInterface.finishActivity();
//            scriptDownload.onDestroy();
            webView.removeAllViews();
            ((ViewGroup) webView.getParent()).removeAllViews();
            webView.destroy();
            EventBusUtils.unRegister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public static void launch(Context context, String url) {
        launch(context, url, true);
    }

    public static void launch(Context context, String url, boolean showTitle) {
        Intent intent = new Intent();
        intent.setClass(context, DownLoadWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("showTitle", showTitle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        if (findViewById(R.id.error_layout).getVisibility() == View.VISIBLE) {
//            finish();
//            return;
//        }
        super.onBackPressed();
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PageRefreshEvent watchAdEvent) {
        webView.reload();
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebRefreshEvent event) {
        webView.reload();
    }

    public void h5refreshPage() {
        webView.loadUrl("javascript:refreshPage()");
    }

    /**
     * 以下是webview的照片上传时候，用于在网页打开android图库文件
     */
//    public OpenFileWebChromeClient mOpenFileWebChromeClient = new OpenFileWebChromeClient(
//            this);

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (requestCode == OpenFileWebChromeClient.REQUEST_FILE_PICKER) {
//            if (mOpenFileWebChromeClient.mFilePathCallback != null) {
//                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
//                        : intent.getData();
//                if (result != null) {
//                    String path = MediaUtility.getPath(getApplicationContext(),
//                            result);
//                    Uri uri = Uri.fromFile(new File(path));
//                    mOpenFileWebChromeClient.mFilePathCallback
//                            .onReceiveValue(uri);
//                } else {
//                    mOpenFileWebChromeClient.mFilePathCallback
//                            .onReceiveValue(null);
//                }
//            }
//            if (mOpenFileWebChromeClient.mFilePathCallbacks != null) {
//                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
//                        : intent.getData();
//                if (result != null) {
//                    String path = MediaUtility.getPath(getApplicationContext(),
//                            result);
//                    Uri uri = Uri.fromFile(new File(path));
//                    mOpenFileWebChromeClient.mFilePathCallbacks
//                            .onReceiveValue(new Uri[]{uri});
//                } else {
//                    mOpenFileWebChromeClient.mFilePathCallbacks
//                            .onReceiveValue(null);
//                }
//            }
//            mOpenFileWebChromeClient.mFilePathCallback = null;
//            mOpenFileWebChromeClient.mFilePathCallbacks = null;
//        }
//    }
    @Override
    public JSONObject getTrackProperties() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("#title", getIntent().getStringExtra("title"));
            String url = getIntent().getStringExtra("url");
            if (url == null) {
                url = "";
            }
            String[] split = url.split("\\?");
            properties.put("#webUrl", split[0]);
            return properties;
        } catch (JSONException e) {
            // ignore
        }
        return null;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void pageClose(PaySuccess event) {
//        finish();
//    }
}
