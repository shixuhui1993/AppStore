package com.duoyou_cps.appstore.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.duoyou.develop.utils.glide.GlideUtils;
import com.duoyou.develop.view.recyclerview.BaseSimpleRecyclerAdapter;
import com.duoyou.develop.view.recyclerview.ViewHolder;
import com.duoyou_cps.appstore.R;
import com.duoyou_cps.appstore.entity.OptionEntity;

public class OptionAdapter extends BaseSimpleRecyclerAdapter<OptionEntity> {
    @Override
    public int getLayoutId() {
        return R.layout.lay_item_mine_option;
    }

    @Override
    public void convert(ViewHolder holder, OptionEntity entity, int position) {
        String title = entity.getTitle();
        View view = holder.getView(R.id.ll_item_mine_options);
        holder.setVisible(R.id.view_stub_line, entity.getId() < 0);
        holder.setVisible(R.id.iv_options_icon, entity.getResoureId() != 0);

        if ("".equals(title)) {
            holder.setVisible(R.id.view_stub, true);
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            ImageView imageView = holder.getImageView(R.id.iv_options_icon);
            holder.setVisible(R.id.view_stub, false);
            holder.setText(R.id.tv_item_mine_title, title);
            holder.setText(R.id.tv_item_mine_desc, entity.getDesc());
            if (entity.getResoureId() != 0) {
                GlideUtils.loadImage(holder.getContext(), entity.getResoureId(), imageView);
            }
            holder.getView(R.id.iv_more).setVisibility(entity.isShowNext() ? View.VISIBLE : View.GONE);
        }
    }
}
