package com.duoyou.develop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.duoyou.develop.utils.glide.GlideUtils;
import com.duoyou_cps.appstore.R;


public class LoadingView extends RelativeLayout {

    public LoadingView(Context context) {
        super(context);
        initView();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.lib_lay_loading_view, this, false);
        addView(view);
        ImageView loadingIv = view.findViewById(R.id.loading_iv);
        GlideUtils.loadAsGifImage(getContext(), R.drawable.lib_icon_loading_gif, loadingIv);
    }


//    public static LoadingView mLoadingView;
//
//    public static View showLoadView(Context context){
//        if(mLoadingView != null){
//            mLoadingView.dis
//        }
//        mLoadingView = new LoadingView(context);
//    }

}
