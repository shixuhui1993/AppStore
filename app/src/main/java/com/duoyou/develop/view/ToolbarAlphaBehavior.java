package com.duoyou.develop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.duoyou.develop.utils.LogUtil;
import com.google.android.material.appbar.AppBarLayout;

public class ToolbarAlphaBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {
    private static final String TAG = "ToolbarAlphaBehavior";
    private int offset = 0;
    private int startOffset = 0;
    private int endOffset = 0;
    private Context context;

    public ToolbarAlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        int scrollY = child.getScrollY();
        LogUtil.i("appBar---", scrollY + "  "+nestedScrollAxes);
        return true;
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout toolbar, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        LogUtil.i("appBar--onNestedScroll-", dyConsumed + "  " + dyUnconsumed + "");
        toolbar.scrollBy(0,dyConsumed);

//        startOffset = 0;
//        endOffset = context.getResources().getDimensionPixelOffset(R.dimen.header_height) - toolbar.getHeight();
//        offset += dyConsumed;
//        if (offset <= startOffset) {  //alpha为0
//            toolbar.getBackground().setAlpha(0);
//        } else if (offset > startOffset && offset < endOffset) { //alpha为0到255
//            float precent = (float) (offset - startOffset) / endOffset;
//            int alpha = Math.round(precent * 255);
//            toolbar.getBackground().setAlpha(alpha);
//        } else if (offset >= endOffset) {  //alpha为255
//            toolbar.getBackground().setAlpha(255);
//        }
    }

}