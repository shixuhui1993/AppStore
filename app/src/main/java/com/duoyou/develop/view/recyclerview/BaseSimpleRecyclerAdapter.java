package com.duoyou.develop.view.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhy
 * date 16/4/9
 */
public abstract class BaseSimpleRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private List<T> mDatas;
    private CheckItem mCheckItem;
    private ItemManager<T> mItemManager;
    protected OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseSimpleRecyclerAdapter(List<T> datas) {
        mDatas = datas;
    }

    public BaseSimpleRecyclerAdapter() {
        this(null);
    }

    protected OnItemContentClickListener mOnItemContentClickListener;

    public OnItemContentClickListener getOnItemContentClickListener() {
        return mOnItemContentClickListener;
    }

    public void setOnItemContentClickListener(OnItemContentClickListener mOnItemContentClickListener) {
        this.mOnItemContentClickListener = mOnItemContentClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(parent, viewType);
        onBindViewHolderClick(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, getDatas().get(position), position);
    }


    @Override
    public int getItemViewType(int position) {
        return getLayoutId();
    }

    @Override
    public int getItemCount() {
        return getDatas().size();
    }

    /**
     * 返回item Layout
     *
     * @return layoutId
     */
    public abstract int getLayoutId();

    /**
     * 实现item的点击事件
     *
     * @param holder 绑定点击事件的ViewHolder
     */
    public void onBindViewHolderClick(final ViewHolder holder) {
        //判断当前holder是否已经设置了点击事件
        if (holder.itemView instanceof ViewGroup && !holder.itemView.hasOnClickListeners()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获得holder的position
                    int layoutPosition = holder.getLayoutPosition();
                    //检查item的position,是否可以点击.
                    if (getCheckItem().checkPosition(layoutPosition)) {
                        //检查并得到真实的position
                        int itemPosition = getCheckItem().getAfterCheckingPosition(layoutPosition);
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(holder, itemPosition);
                        }
                    }
                }
            });
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获得holder的position
                int layoutPosition = holder.getLayoutPosition();
                //检查position是否可以点击
                if (getCheckItem().checkPosition(layoutPosition)) {
                    //检查并得到真实的position
                    int itemPosition = getCheckItem().getAfterCheckingPosition(layoutPosition);
                    if (mOnItemLongClickListener != null) {
                        return mOnItemLongClickListener.onItemLongClick(holder, itemPosition);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 检查item的position,主要viewholder的getLayoutPosition不一定是需要的.
     * 比如添加了headview和footview.
     */
    public interface CheckItem {
        boolean checkPosition(int position);

        int getAfterCheckingPosition(int position);
    }

    /**
     * 默认实现的CheckItem接口
     *
     * @return
     */
    public CheckItem getCheckItem() {
        if (mCheckItem == null) {
            mCheckItem = new CheckItem() {
                @Override
                public boolean checkPosition(int position) {
                    return true;
                }

                @Override
                public int getAfterCheckingPosition(int position) {
                    return position;
                }
            };
        }
        return mCheckItem;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.mCheckItem = checkItem;
    }

    public List<T> getDatas() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        return mDatas;
    }

    public void setDatas(List<T> datas) {
        if (datas != null) {
            getDatas().clear();
            getDatas().addAll(datas);
            notifyDataSetChanged();
        }
    }

    public abstract void convert(ViewHolder holder, T t, int position);


    /**
     * 操作adapter
     *
     * @return
     */
    public ItemManager<T> getItemManager() {
        if (mItemManager == null) {
            mItemManager = new ItemManageImpl(this);
        }
        return mItemManager;
    }

    public void setItemManager(ItemManager<T> itemManager) {
        mItemManager = itemManager;
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemLongClickListener {
        boolean onItemLongClick(ViewHolder viewHolder, int position);
    }

    public interface OnItemContentClickListener {
        /**
         * item 内容被点击
         *
         * @param position 第几个
         */
        void onItemContentClickListener(ViewHolder viewHolder, int position, int viewId);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    /**
     * 默认使用 notifyDataChanged();刷新.
     * 如果使用带动画效果的,条目过多可能会出现卡顿.
     */
    private class ItemManageImpl extends ItemManager<T> {

        public ItemManageImpl(BaseSimpleRecyclerAdapter<T> adapter) {
            super(adapter);
        }

        @Override
        public void addAllItems(List<T> items) {
            if (items != null) {
                getDatas().addAll(items);
                notifyDataChanged();
            }
        }

        @Override
        public void replaceAllItem(List<T> items) {
            if (items == null) {
                items = new ArrayList<>();
            }
            setDatas(items);
            notifyDataChanged();
        }

    }
}
