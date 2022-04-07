package com.duoyou.develop.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebView;


public class MyWebView extends WebView {

    private ViewGroup viewGroup;


    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public MyWebView(Context context) {
        super(getFixedContext(context));
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(getScrollY() <= 0)
                        scrollTo(0,1);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }

    public static Context getFixedContext(Context context) {
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) // Android Lollipop 5.0 & 5.1
            return context.createConfigurationContext(new Configuration());
        return context;
    }

}
