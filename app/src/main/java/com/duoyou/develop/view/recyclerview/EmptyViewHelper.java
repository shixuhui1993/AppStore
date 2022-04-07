package com.duoyou.develop.view.recyclerview;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.duoyou_cps.appstore.R;


/**
 * @author hbhd
 */
public class EmptyViewHelper {
    private Context mContext;
    private TextView mTextView;
    private ImageView mEmptyIcon;
    private View emptyView;

    public EmptyViewHelper(Context context) {
        this.mContext = context;
        initEmptyView();
    }

    public View getEmptyView() {
        return initEmptyView();
    }

    public View initEmptyView() {
        if (emptyView != null) {
            return emptyView;
        }
        emptyView = View.inflate(mContext, R.layout.lib_content_empty_view, null);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
        emptyView.setLayoutParams(lp);
        mEmptyIcon = emptyView.findViewById(R.id.empty_view_iv_icon);
        mTextView = emptyView.findViewById(R.id.empty_view_tv_msg);
        return emptyView;
    }

    public void setOtherEmptyView(String content, Integer integer) {
        mTextView.setText(content);
        mEmptyIcon.setImageResource(integer);
    }
    public void setBackgroundColor( Integer integer) {
        emptyView.setBackgroundColor(integer);
    }
    public void setImageSize(int widthWeight, int heightWeight) {
        if (emptyView == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = mEmptyIcon.getLayoutParams();
        layoutParams.height = layoutParams.width * heightWeight / widthWeight;
        mEmptyIcon.setLayoutParams(layoutParams);
    }

    public void setOtherEmptyView(String content) {
        mTextView.setText(content);
    }

    public void setNetErrorEmptyView() {
        mTextView.setText("网络连接失败，请重试...");
//        mEmptyIcon.setImageResource(R.drawable.empty_no_wifi);
    }

    public void setLocationErrorEmptyView() {
        mTextView.setText("定位失败，请检查手机定位权限");
//        mEmptyIcon.setImageResource(R.drawable.empty_no_search);
    }

}