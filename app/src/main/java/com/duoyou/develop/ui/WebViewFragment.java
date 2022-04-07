package com.duoyou.develop.ui;


import static com.duoyou_cps.appstore.config.AppConfigKt.JAVA_IMPL_TARGET_APP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.duoyou.develop.base.BaseFragment;
import com.duoyou.develop.entity.UserInfo;
import com.duoyou.develop.utils.SPManager;
import com.duoyou.develop.utils.WebViewUtils;
import com.duoyou.develop.utils.eventbus.EventBusUtils;
import com.duoyou.develop.utils.eventbus.LoginEvent;
import com.duoyou.develop.utils.eventbus.PageRefreshEvent;
import com.duoyou.develop.utils.java_impi.JavaScriptInterfaceImpl;
import com.duoyou.develop.view.LoadingView;
import com.duoyou.develop.view.MyWebView;
import com.duoyou.develop.view.VerticalSwipeRefreshLayout;
import com.duoyou_cps.appstore.R;
import com.duoyou_cps.appstore.impi.ILoginStatusChange;
import com.duoyou_cps.appstore.util.LoginImplManager;
import com.duoyou_cps.appstore.util.httputils.HttpUrlKt;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WebViewFragment extends BaseFragment implements ILoginStatusChange {

    private VerticalSwipeRefreshLayout verticalSwipeRefreshLayout;
    private MyWebView webView;
    private String url = "";
    private LoadingView loadingView;
    private int tab;

    public static WebViewFragment newInstance(String url) {
        Bundle args = new Bundle();
        WebViewFragment fragment = new WebViewFragment();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.lib_frg_web_view;
    }

    @Override
    public boolean isRepeatInit() {
        return true;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        verticalSwipeRefreshLayout = findViewById(R.id.web_view_layout);

        webView = findViewById(R.id.web_view);
        loadingView = findViewById(R.id.loading_view);
        verticalSwipeRefreshLayout.setNestedScrollingEnabled(false);

        WebViewUtils.initWebViewSettings(getActivity(), webView);

        webView.addJavascriptInterface(new JavaScriptInterfaceImpl(getActivity(), webView), JAVA_IMPL_TARGET_APP);
        verticalSwipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return webView.getScrollY() > 0;
            }
        });
    }


    @Override
    public void initData() {
        boolean isRefresh = SPManager.getValue("isRefresh", false);
        if (!isRefresh) {
            webView.postDelayed(() -> {
                if (UserInfo.getInstance().isLogin()) {
                    changeToken();
                }
            }, 500);
            SPManager.getValue("isRefresh", true);
        }

        webView.postDelayed(() -> {
            if (UserInfo.getInstance().isLogin()) {
                loadFirstUrl();
            }
        }, 300);
    }

    @Override
    public void initListener() {
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 90) {
                    if (verticalSwipeRefreshLayout != null) {
                        verticalSwipeRefreshLayout.setRefreshing(false);
                    }
                    loadingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.show();
                return true;
            }


        });
        webView.setWebViewClient(new WebViewClient() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request != null && request.getRequestHeaders() != null) {
//                    request.getRequestHeaders().put("headerInfo", HttpHeaderUtil.getHeaderInfoJsonString());
                }
                return super.shouldOverrideUrlLoading(view, request);
            }


            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                FragmentActivity activity = getActivity();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                activity.runOnUiThread(() -> {
//                    if(isResume){
//                        operateMusic(true);
//                    }
                    if (verticalSwipeRefreshLayout != null) {
                        verticalSwipeRefreshLayout.setRefreshing(false);
                    }
                    loadingView.setVisibility(View.GONE);
                });
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                FragmentActivity activity = getActivity();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                activity.runOnUiThread(() -> {
                    if (verticalSwipeRefreshLayout != null) {
                        verticalSwipeRefreshLayout.setRefreshing(false);
                    }
                    loadingView.setVisibility(View.GONE);
                    showErrorPage();
                });

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                hideErrorPage();
            }

        });

        verticalSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }


    @Override
    public void refresh() {
        if (verticalSwipeRefreshLayout != null) {
            if (UserInfo.getInstance().isLogin())
                reload();
        }
    }

    private void loadFirstUrl() {
        url = getArguments().getString("url");
        url = HttpUrlKt.Companion.getH5HttpUrl(url);
        webView.loadUrl(url);
    }


    private void changeToken() {
        loadFirstUrl();
        webView.postDelayed(() -> webView.loadUrl("javascript:window.location.reload( true )"), 150);
    }

    private boolean isResume = false;

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isResume = false;
    }

    private void reload() {
        changeToken();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PageRefreshEvent event) {
        reload();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uploadInfo(UserInfo info) {
        webView.reload();
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoginImplManager.INSTANCE.removeListener(this);
        EventBusUtils.unRegister(this);
    }

    @Override
    public void login(LoginEvent event) {
        if (event.isLogin()) {
            changeToken();
        }
    }

    @Override
    public void login() {
        changeToken();
    }

    @Override
    public void logout() {
        changeToken();
    }
}
