package com.duoyou.develop.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.duoyou_cps.appstore.R;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

public class SmartRefreshHelper {
    public static void initRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.themeBackgroundColor, R.color.theme_color);//全局设置主题颜色

                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.color_3f6, R.color.colorAccent);//全局设置主题颜色
//                layout.setPrimaryColorsId(R.color.themeBackgroundColor, R.color.theme_color);//全局设置主题颜色
                ClassicsFooter classicsFooter = new ClassicsFooter(context);
                classicsFooter.setTextSizeTitle(12);
//                classicsFooter.setAccentColor(Color.parseColor("#BBBBBB"));
//                layout.setEnableLoadMoreWhenContentNotFull(false);
//                layout.setEnableFooterFollowWhenLoadFinished(true);
                layout.setEnableFooterFollowWhenNoMoreData(true);
                return classicsFooter.setDrawableSize(20);
            }
        });
    }

    public static void initClassicsHeader(Activity activity, SmartRefreshLayout smartRefreshLayout, String color1, String color2) {
        if (smartRefreshLayout == null) {
            return;
        }
        ClassicsHeader classicsHeader = new ClassicsHeader(activity);
        classicsHeader.setPrimaryColors(Color.parseColor(color1), Color.parseColor(color2));
        smartRefreshLayout.setRefreshHeader(classicsHeader);
    }

    public static void finishLoadData(SmartRefreshLayout smartRefreshLayout) {
        if (smartRefreshLayout == null) {
            return;
        }
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
    }

    public static void enableLoadMore(SmartRefreshLayout smartRefreshLayout, int size) {
        if (smartRefreshLayout == null) {
            return;
        }

        if (size < 8) {
            smartRefreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            smartRefreshLayout.setEnableLoadMore(true);
        }

    }


}
