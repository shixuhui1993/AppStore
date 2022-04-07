package com.duoyou.develop.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.SizeUtils;
import com.duoyou.develop.utils.MiaokanBarUtils;
import com.duoyou.develop.utils.eventbus.EventBusUtils;
import com.duoyou.develop.utils.eventbus.LoginEvent;
import com.duoyou.develop.utils.eventbus.PageRefreshEvent;
import com.duoyou_cps.appstore.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class BaseFragment extends Fragment  {

    public View rootView;
    public View errorLayout;
    private TextView refreshTv;
    protected String umengParams = getClass().getName();
    protected boolean firstLoad = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isRepeatInit()) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            EventBusUtils.register(this);
            initView();
            initExtraView();
            initErrorView();
            initData();
            initListener();
            request();
        } else {
            if (rootView == null) {
                rootView = inflater.inflate(getLayoutId(), container, false);
                EventBusUtils.register(this);
                initView();
                initExtraView();
                initErrorView();
                initData();
                initListener();
                request();
            }
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            if (rootView != null) {
                ViewGroup parent = (ViewGroup) rootView.getParent();
                if (parent != null) {
                    parent.removeView(rootView);
                }
            }
        }
//        ThinkingDataUtil.getInstance().setFragmentTrack(this);
        return rootView;
    }

    public <T extends View> T findViewById(int rid) {
        if (rootView == null) {
            return null;
        }
        return rootView.findViewById(rid);
    }

    public int getLayoutId() {
        return -1;
    }

    public void initView() {

    }

    public void initExtraView() {
    }

    public void initListener() {

    }

    public void initData() {

    }

    public void request() {

    }

    public void initErrorView() {
//        errorLayout = findViewById(R.id.error_layout);
//        refreshTv = findViewById(R.id.refresh_tv);

        if (refreshTv != null) {
            refreshTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refresh();
                }
            });
        }
    }

    /**
     * 是否重复初始化
     */
    public boolean isRepeatInit() {
        return false;
    }

    private long lastTime;


    public void onTabSelect(int position) {
//        lastTime = TimeUtils.getNowMills();
//        ArrayMap<String, Object> params = new ArrayMap<>();
//        params.put("page_name", FragmentIndexConfig.getMainTabTitleFromPosition(position));
//        params.put("open_time", lastTime);
//        params.put("event_desc", "tab页面停留时长");
//        ThinkingDataEvent.eventTrack(ThinkingDataEventIdKt.THINKING_EVENT_TAB_OPEN_TIME, params);
    }

    public void onTabUnSelect(int position) {
//        if (lastTime == 0) {
//            return;
//        }
//        ArrayMap<String, Object> params = new ArrayMap<>();
//        params.put("page_name", FragmentIndexConfig.getMainTabTitleFromPosition(position));
//        params.put("close_time", TimeUtils.getNowMills());
//        params.put("event_desc", "tab页面停留时长");
//        ThinkingDataEvent.eventTrack(ThinkingDataEventIdKt.THINKING_EVENT_TAB_CLOSE_TIME, params);
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

    @Override
    public void onResume() {
        super.onResume();
//        LogUtils.i("umengParams---",umengParams);
//        MobclickAgent.onPageStart(umengParams);
        if (firstLoad) {
            firstLoad = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        LogUtils.i("umengParams---",umengParams);
//        MobclickAgent.onPageEnd(umengParams);
    }

    protected void setStatusBar() {
        View viewById = findViewById(R.id.view_status_bar);
        if (viewById != null) {
            ViewGroup.LayoutParams layoutParams = viewById.getLayoutParams();
            layoutParams.height = MiaokanBarUtils.getStatusBarHeight();
        }
    }

    protected void setStatusBar(View view) {
        View viewById = view.findViewById(R.id.view_status_bar);
        if (viewById != null) {
            ViewGroup.LayoutParams layoutParams = viewById.getLayoutParams();
            layoutParams.height = MiaokanBarUtils.getStatusBarHeight();
        }
    }

    protected void setStatusBar(int heightDp) {
        View viewById = findViewById(R.id.view_status_bar);
        if (viewById != null) {
            ViewGroup.LayoutParams layoutParams = viewById.getLayoutParams();
            layoutParams.height = MiaokanBarUtils.getStatusBarHeight() + SizeUtils.dp2px(heightDp);
        }
    }
//
//    @Override
//    public String getScreenUrl() {
//        return "thinkingdata://page/" + getClass().getSimpleName();
//    }
//
//    @Override
//    public JSONObject getTrackProperties() {
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("#title", getArguments().getString("title"));
//            properties.put("#FragmentType", getClass().getSimpleName());
//            return properties;
//        } catch (JSONException e) {
//            // ignore
//        }
//        return null;
//    }

    @Override
    public void onDestroy() {
        EventBusUtils.unRegister(this);
        super.onDestroy();
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
