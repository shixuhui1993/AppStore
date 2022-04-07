package com.duoyou.develop.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.duoyou_cps.appstore.R;


public abstract class BaseDialog extends Dialog {

    public Context context;

    public BaseDialog(Context context) {
        super(context, R.style.DyDialogStyle);
        this.context = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate();
    }

    protected abstract void onCreate();



}

