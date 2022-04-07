package com.duoyou.develop.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.duoyou_cps.appstore.R;


public class LoadingDialog extends BaseDialog {

    private ContentLoadingProgressBar contentLoadingProgressBar;
    private String mDesc;
    private Context mContext;
    private TextView descTv;
    private Boolean isCanCancel = true;

    public static Dialog show(Context context, String tipContent) {
        LoadingDialog loadingDialog = new LoadingDialog(context, tipContent);
        loadingDialog.show();
        return loadingDialog;
    }

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, boolean canCancel) {
        super(context);
        isCanCancel = canCancel;
    }

    public LoadingDialog(Context context, String desc) {
        super(context);
        mDesc = desc;
        mContext = context;
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.lib_lay_dialog_loading_layout);
        descTv = findViewById(R.id.progress_desc);
        contentLoadingProgressBar = findViewById(R.id.progress_bar);


        if (mDesc != null) {
            descTv.setText(mDesc);
        }
        contentLoadingProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white), PorterDuff.Mode.MULTIPLY);
    }

    public void updateTip(String text) {
        if (descTv != null && text != null)
            descTv.setText(text);

    }


    @Override
    public void dismiss() {
        try {
            contentLoadingProgressBar.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dismiss();
    }
}
