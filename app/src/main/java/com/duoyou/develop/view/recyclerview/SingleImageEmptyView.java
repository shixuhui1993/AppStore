package com.duoyou.develop.view.recyclerview;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.duoyou_cps.appstore.R;


/**
 * @author hbhd
 */
public class SingleImageEmptyView {
    private final Context mContext;
    private ImageView mEmptyIcon;
    private View emptyView;

    public SingleImageEmptyView(Context context) {
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
        emptyView = View.inflate(mContext, R.layout.lib_common_empty_view_single_image, null);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
        emptyView.setLayoutParams(lp);
        mEmptyIcon = emptyView.findViewById(R.id.empty_view_iv_icon);
        return emptyView;
    }

    public void setEmptyImage(Integer integer) {
        mEmptyIcon.setImageResource(integer);
    }

    public void setImageSize(int widthWeight, int heightWeight) {
        if (emptyView == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = mEmptyIcon.getLayoutParams();
        layoutParams.height = layoutParams.width * heightWeight / widthWeight;
        mEmptyIcon.setLayoutParams(layoutParams);
    }

}