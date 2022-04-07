package com.duoyou.develop.view.recyclerview.wrapper;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.duoyou.develop.view.recyclerview.BaseSimpleRecyclerAdapter;
import com.duoyou.develop.view.recyclerview.ViewHolder;


/**
 *
 * @author baozi
 * @date 2017/4/30
 */

public class HeaderAndFootWrapper<T> extends BaseWrapper<T> {
    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFootViews = new SparseArray<>();

    public HeaderAndFootWrapper(BaseSimpleRecyclerAdapter<T> adapter) {
        super(adapter);
        mAdapter.setCheckItem(new CheckItem() {
            @Override
            public boolean checkPosition(int position) {
                return !(isHeaderViewPos(position) || isFooterViewPos(position));
            }

            @Override
            public int getAfterCheckingPosition(int position) {
                return position - getHeadersCount();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(mHeaderViews.get(viewType));
        } else if (mFootViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(mFootViews.get(viewType));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            return;
        }
        super.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - mAdapter.getItemCount());
        }
        return super.getItemViewType(position - getHeadersCount());
    }


    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size(), view);
    }

    public void addFootView(View view) {
        mFootViews.put(Integer.MAX_VALUE - mFootViews.size(), view);
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + mAdapter.getItemCount();
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }


}
