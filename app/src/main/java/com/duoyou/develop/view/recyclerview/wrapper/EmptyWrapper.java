package com.duoyou.develop.view.recyclerview.wrapper;

import android.view.View;
import android.view.ViewGroup;

import com.duoyou.develop.view.recyclerview.BaseSimpleRecyclerAdapter;
import com.duoyou.develop.view.recyclerview.ViewHolder;


/**
 * Created by baozi on 2017/4/30.
 */

public class EmptyWrapper<T> extends BaseWrapper<T> {
    public static final int ITEM_TYPE_EMPTY = Integer.MIN_VALUE;

    private View mEmptyView;
    private int mEmptyLayoutId;

    public EmptyWrapper(BaseSimpleRecyclerAdapter<T> adapter) {
        super(adapter);
    }

    private boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && mAdapter.getItemCount() == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmpty()) {
            if (mEmptyView != null) {
                return ViewHolder.createViewHolder(mEmptyView);
            } else {
                return ViewHolder.createViewHolder(parent, mEmptyLayoutId);
            }
        }
        return super.onCreateViewHolder(parent, viewType);
    }


    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return ITEM_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isEmpty()) {
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {
            return 1;
        }
        return mAdapter.getItemCount();
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setEmptyView(int layoutId) {
        mEmptyLayoutId = layoutId;
    }

}
