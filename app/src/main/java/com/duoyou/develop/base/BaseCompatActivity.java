package com.duoyou.develop.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.duoyou.develop.utils.MiaokanBarUtils;
import com.duoyou.develop.utils.eventbus.EventBusUtils;
import com.duoyou.develop.utils.eventbus.LoginEvent;
import com.duoyou.develop.utils.eventbus.PageRefreshEvent;
import com.duoyou.develop.utils.thinkingdata.ScreenAutoTracker;
import com.duoyou_cps.appstore.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;



public class BaseCompatActivity extends AppCompatActivity implements ScreenAutoTracker {

    public TextView titleTv;
    public View backLayout;
    public View titleBarView;
    public View errorLayout;
    protected TextView refreshTv;
    protected View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeSetContentView();
        if (getLayoutId() != -1) {
            setContentView();
        }
        EventBusUtils.register(this);
        setStatusBar();
        initTitleView();
        initTitleData();
        initView();
        initExtraView();
        initData();
        initListener();
    }
    public void setContentView(){
        rootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(rootView);

    }

    public void initExtraView() {
    }

    public int getLayoutId() {
        return -1;
    }


    public void initView() {

    }

    public void initBeforeSetContentView() {

    }

    public void initListener() {

    }

    public void initData() {

    }

    public View getRootView() {
        return rootView;
    }

    protected void setOnClick(View view, View.OnClickListener listener) {
        if (view == null || listener == null) {
            return;
        }
        view.setOnClickListener(listener);
    }

    public void initTitleView() {

        backLayout = findViewById(R.id.back_layout);
        titleTv = findViewById(R.id.title_tv);
        titleBarView = findViewById(R.id.title_bar_layout);
        errorLayout = findViewById(R.id.error_layout);

        refreshTv = findViewById(R.id.refresh_tv);

        if (refreshTv != null) {
            refreshTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refresh();
                }
            });
        }
    }

    public void initTitleData() {
        if (titleTv != null) {
            titleTv.setText(title());
        }
        if (backLayout != null) {
            backLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        if (refreshTv != null) {
            refreshTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refresh();
                }
            });
        }
    }

    public void showTitleBar(int hidetitle) {
        if (titleBarView != null) {
            if (hidetitle == 1) {
                titleBarView.setVisibility(View.GONE);
            } else {
                titleBarView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void refresh() {

    }

    public void showErrorPage() {
        if (errorLayout != null) {
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

    public void hideErrorPage() {
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
    }


    public Activity getActivity() {
        return this;
    }

    public String title() {
        return "";
    }


    @Override
    protected void onStop() {
        Glide.with(this).pauseRequests();
        super.onStop();
    }

    @Override
    protected void onResume() {
//        MobclickAgent.onResume(this); // 不能遗漏
        super.onResume();
    }

    @Override
    protected void onPause() {
//        MobclickAgent.onPause(this); // 不能遗漏
        super.onPause();
    }


    protected void setStatusBar() {
        MiaokanBarUtils.transparentStatusBar(this);
        MiaokanBarUtils.setStatusBarLightMode(this, getLightModel());
        View viewById = findViewById(R.id.view_status_bar);
        if (viewById != null) {
            ViewGroup.LayoutParams layoutParams = viewById.getLayoutParams();
            layoutParams.height = MiaokanBarUtils.getStatusBarHeight();
        }
    }

    protected void setStatusBar(View view) {
        MiaokanBarUtils.transparentStatusBar(this);
        MiaokanBarUtils.setStatusBarLightMode(this, getLightModel());
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = MiaokanBarUtils.getStatusBarHeight();
        }
    }
    protected boolean getLightModel() {
        return true;
    }

    public static void setText(TextView tv, int text) {
        setText(tv, String.valueOf(text));
    }

    protected void setText(@IdRes int viewId, String text) {
        setText(findViewById(viewId), String.valueOf(text));
    }

    public static void setText(TextView tv, String text) {
        if (tv != null) {
            tv.setText(text == null ? "" : text);
        }
    }

    protected static long oldClickTime;

    public static boolean isDoubleClick() {
        long l = System.currentTimeMillis();

        if (l - oldClickTime < 600) {
            return true;
        }
        oldClickTime = l;
        return false;

    }

    public static void launcherActivity(Context context, Class<?> cls) {
        if (context != null && cls != null) {
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);
        }
    }

    private static void launcherActivity(Class<?> cls) {
        Intent intent = new Intent(Utils.getApp(), cls);
        Utils.getApp().startActivity(intent);
    }

    public static void launcherActivity(Context context, Class<?> cls, String paramsName, String params) {
        if (context != null && cls != null) {
            Intent intent = new Intent(context, cls);
            intent.putExtra("paramsName", paramsName);
            intent.putExtra(paramsName, params);
            context.startActivity(intent);
        }
    }

    @Override
    public String getScreenUrl() {
        return "thinkingdata://page/" + getClass().getSimpleName();
    }

    @Override
    public JSONObject getTrackProperties() {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void login(LoginEvent event) {
        login(event.isLogin());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void login(PageRefreshEvent event) {
        pageRefresh();
    }

    protected void login(boolean isLogin) {


    }

    protected void pageRefresh() {

    }
}
