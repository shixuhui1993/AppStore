//package com.duoyou.develop.ui;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Handler;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.collection.ArrayMap;
//
//import com.duoyou.develop.base.BaseCompatActivity;
//import com.duoyou.develop.utils.WebViewUtils;
//import com.duoyou.develop.utils.eventbus.EventBusUtils;
//import com.duoyou.develop.utils.eventbus.PageRefreshEvent;
//import com.duoyou.develop.utils.eventbus.TabSelectedEvent;
//import com.duoyou.develop.utils.http.JSONUtils;
//import com.duoyou.develop.utils.http.okhttp.MyGsonCallback;
//import com.duoyou.develop.utils.java_impi.JavaScriptInterfaceImpl;
//import com.duoyou.develop.view.MyWebView;
//import com.duoyou.develop.view.VerticalSwipeRefreshLayout;
//import com.duoyou_cps.appstore.R;
//import com.tencent.smtt.export.external.interfaces.SslError;
//import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
//import com.tencent.smtt.sdk.WebView;
//import com.tencent.smtt.sdk.WebViewClient;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class PayWebViewActivity extends BaseCompatActivity {
//    public static final String WEB_VIEW_JAVA_IMP_TAG = "mh";
//
//    private VerticalSwipeRefreshLayout verticalSwipeRefreshLayout;
//    private MyWebView webView;
//    private String url;
//    private int isCount;
//    private int isRefresh;
//    private boolean showTitle;
//    private Handler mHandler = new Handler();
//    private JavaScriptInterfaceImpl javaScriptInterface;
//    private int orderType;
//    private boolean isPay = false;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.web_view_activity;
//    }
//
//    @Override
//    public void initView() {
//        EventBusUtils.register(this);
//        verticalSwipeRefreshLayout = findViewById(R.id.web_view_layout);
//        verticalSwipeRefreshLayout.setRefreshing(true);
//        webView = findViewById(R.id.web_view);
//        showTitle = true;
//        WebViewUtils.initWebViewSettings(getActivity(), webView);
//        WebViewUtils.setAndroidNativeLightStatusBar(this, true);
//        javaScriptInterface = new JavaScriptInterfaceImpl(getActivity(), webView);
//        webView.addJavascriptInterface(javaScriptInterface, WEB_VIEW_JAVA_IMP_TAG);
//        verticalSwipeRefreshLayout.setNestedScrollingEnabled(false);
//        orderType = getIntent().getIntExtra("payOrderType", -1);
//    }
//
//    @Override
//    protected void onTitleChanged(CharSequence title, int color) {
//        setText(titleTv, title.toString());
//    }
//
//    @Override
//    public void initData() {
//        url = getIntent().getStringExtra("url");
//
//        if (StringUtils.isEmpty(url)) {
//            finish();
//            return;
//        }
//        webView.post(() -> webView.loadData(url, "text/html", "UTF-8"));
//    }
//
//
//    public void onReceivedTitle(String title) {
//        if (titleTv != null) {
//            titleTv.setText(title);
//        }
//    }
//
//    private boolean isFinish = false;
//
//    @Override
//    public void initListener() {
//
//
//        webView.setWebViewClient(new WebViewClient() {
//
//            @SuppressWarnings("deprecation")
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (isOpenPay) {
//                    return true;
//                }
//
//                // ------  对alipays:相关的scheme处理 -------
//                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
//                    try {
//                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
//                    } catch (Exception e) {
//                        new AlertDialog.Builder(getActivity())
//                                .setMessage("未检测到支付宝客户端，请安装后重试。")
//                                .setPositiveButton("立即安装", (dialog, which) -> {
//                                    isOpenPay = true;
//                                    Uri alipayUrl = Uri.parse("https://d.alipay.com");
//                                    startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
//                                }).setNegativeButton("取消", null).show();
//                    }
//                    return true;
//                }
//
//
//                if (!(url.startsWith("http") || url.startsWith("https"))) {
//                    return true;
//                }
//                /*
//                  推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
//                 */
//                isOpenPay = true;
//                final PayTask task = new PayTask(PayWebViewActivity.this);
//                boolean isIntercepted = task.payInterceptorWithUrl(url, true, result -> {
//
////                    final String url1 = result.getReturnUrl();
//                    final String code = result.getResultCode();
//                    isFinish = true;
//                    if ("9000".equals(code)) {
//                        EventBusUtils.post(new PageRefreshEvent());
//                        if (orderType == ORDER_TYPE_BLIND_BOX) {
//                            EventBusUtils.post(new PaySuccess());
//                            EventBusUtils.post(new TabSelectedEvent(1));
//                            BoxBuySuccess boxInfoJson = GsonUtils.fromJson(getIntent().getStringExtra("boxInfoJson"), BoxBuySuccess.class);
////                            EventBusUtils.post(boxInfoJson);
//
//                            ArrayMap<String, String> mutableMapOf = new ArrayMap<>();
//                            mutableMapOf.put("order_no", boxInfoJson.getOrder());
//
//                            PayApiKt.getOrderInfoForOrderNum(mutableMapOf, new MyGsonCallback() {
//                                @Override
//                                public void onSuccessNoCode(String result) {
//                                    JSONObject jsonObject = JSONUtils.formatJSONObject(result);
//                                    int num = jsonObject.optInt("nums");
//                                    int boxId = jsonObject.optInt("box_id");
//                                    String name = jsonObject.optString("name");
//
//                                    runOnUiThread(() -> {
//                                        Intent intent = new Intent(getActivity(), BlindBoxBuySuccessActivity.class);
//                                        intent.putExtra("ids", "[" + boxId + "]");
//                                        intent.putExtra("blindBoxTypeStr", name);
//                                        intent.putExtra("blindBoxNum", num);
//                                        intent.putExtra("listImage", boxInfoJson.getIcon());
//                                        intent.putExtra("operateType", SUCCESS_PAGE_TYPE_BUY);
//                                        startActivity(intent);
//                                    });
//                                }
//
//                                @Override
//                                public void onTaskFinish() {
//                                    setResult(PAY_SUCCESS_FINISH_ACTIVITY);
//                                    finish();
//                                }
//                            });
//                            return;
//                        } else if (orderType == ORDER_TYPE_BEAN_COMMODITY || orderType == ORDER_TYPE_PAY_FREIGHT || orderType == ORDER_TYPE_CASH_COMMODITY) {
//                            if (orderType == ORDER_TYPE_BEAN_COMMODITY) {
//                                EventBusUtils.post(new UpdateCurrentBalance());
//                            }
//                            if (orderType == ORDER_TYPE_CASH_COMMODITY) {
//                                EventBusUtils.post(new GoodsBuySuccess());
//                            }
//                            setResult(PAY_SUCCESS_FINISH_ACTIVITY);
//                        }
//                    }
//                    finish();
//                    overridePendingTransition(0, R.anim.act_anim_alpha_exit);
//                });
//
//                /*
//                  判断是否成功拦截
//                  若成功拦截，则无需继续加载该URL；否则继续加载
//                 */
//                if (!isIntercepted) {
//                    view.loadUrl(url);
//                }
//                return true;
//            }
//
//
//            @Override
//            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError
//                    sslError) {
//                sslErrorHandler.proceed();
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if (isFinish) {
//                    return;
//                }
//                super.onPageFinished(view, url);
//                verticalSwipeRefreshLayout.setRefreshing(false);
//                verticalSwipeRefreshLayout.setEnabled(false);
//            }
//
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String
//                    failingUrl) {
//                if (isFinish) {
//                    return;
//                }
//                verticalSwipeRefreshLayout.setRefreshing(false);
//                verticalSwipeRefreshLayout.setEnabled(false);
//                super.onReceivedError(view, errorCode, description, failingUrl);
////                if (!failingUrl.contains("download")) {
////                    showErrorPage();
////                }
//
//            }
//
//
//        });
//
//
//        webView.setViewGroup(verticalSwipeRefreshLayout);
//        verticalSwipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> webView.getScrollY() > 0);
//
//        verticalSwipeRefreshLayout.setOnRefreshListener(() -> webView.reload());
//
//    }
//
//    @Override
//    public void refresh() {
//        if (isFinish) {
//            return;
//        }
//        verticalSwipeRefreshLayout.setEnabled(true);
//        verticalSwipeRefreshLayout.setRefreshing(true);
//        webView.reload();
//        hideErrorPage();
//    }
//
//    private boolean isOpenPay = false;
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    protected void onDestroy() {
//        try {
//            if (mHandler != null) {
//                mHandler.removeCallbacksAndMessages(null);
//                mHandler = null;
//            }
//            javaScriptInterface.finishActivity();
//            webView.removeAllViews();
//            ((ViewGroup) webView.getParent()).removeAllViews();
//            webView.destroy();
//            EventBusUtils.unRegister(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onDestroy();
//    }
//
//    public static void launch(Context context, String url) {
//        launch(context, url, true);
//    }
//
//    public static void launch(Context context, String url, boolean showTitle) {
//        Intent intent = new Intent();
//        intent.setClass(context, PayWebViewActivity.class);
//        intent.putExtra("url", url);
//        intent.putExtra("showTitle", showTitle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//        if (findViewById(R.id.error_layout).getVisibility() == View.VISIBLE) {
//            finish();
//            overridePendingTransition(0, R.anim.act_anim_alpha_exit);
//            return;
//        }
//        super.onBackPressed();
//    }
//    @Override
//    public JSONObject getTrackProperties() {
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("#title", getIntent().getStringExtra("title"));
//            String url = getIntent().getStringExtra("url");
//            if (url == null) {
//                url = "";
//            }
//            String[] split = url.split("\\?");
//            properties.put("#webUrl", split[0]);
//            return properties;
//        } catch (JSONException e) {
//            // ignore
//        }
//        return null;
//    }
//
//
//}
