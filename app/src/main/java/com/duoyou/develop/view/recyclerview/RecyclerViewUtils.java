package com.duoyou.develop.view.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.SizeUtils;
import com.duoyou.develop.view.recyclerview.spaces_item_decoration.GridSpaceItemDecoration;
import com.duoyou_cps.appstore.R;


/**
 * <pre>
 *     @author : shixuhui
 *     e-mail : shixuhui1993@163.com
 *     time   : 2017/11/20
 *     version: 2.1.2
 * </pre>
 */
public class RecyclerViewUtils {
    public static void setRecyclerViewDivider(RecyclerView recyclerView) {
        setRecyclerViewDivider(recyclerView, R.drawable.lib_divider_tran_shape);
    }

    public static void setRecyclerViewDividerBgMain(RecyclerView recyclerView) {
        setRecyclerViewDividerBgMain(recyclerView, LinearLayoutManager.VERTICAL);
    }

    public static void setRecyclerViewDividerBgMain(RecyclerView recyclerView, int orientation) {
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context, orientation, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.lib_divider_bg_shape));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public static void setRecyclerView5dpDivider(RecyclerView recyclerView, Context context) {
        setRecyclerView5dpDivider(recyclerView, LinearLayoutManager.VERTICAL, context);
    }

    public static void setRecyclerView5dpDivider(RecyclerView recyclerView, int orientation, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, orientation, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, orientation);
//        int divider = orientation == LinearLayout.HORIZONTAL ? R.drawable.divider_tran_width_5dp_shape : R.drawable.divider_tran_5dp_shape;
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, divider));
//        recyclerView.addItemDecoration(dividerItemDecoration);


    }

    public static void setRecyclerViewDivider(RecyclerView recyclerView, int dividerShape) {
        if (recyclerView == null) {
            return;
        }
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        if (dividerShape > 0) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, dividerShape));
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    public static void setGridLayoutManager(RecyclerView recyclerView, int sPan) {
        setGridLayoutManager(recyclerView, sPan, 0, false);

    }

    public static void setGridLayoutManager(RecyclerView recyclerView, int sPan, boolean includeEdge) {
        setGridLayoutManager(recyclerView, sPan, 0, includeEdge);

    }

    public static void setGridLayoutManager(RecyclerView recyclerView, int spanCount, float itemDecoration, boolean includeEdge) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), spanCount, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (recyclerView.getItemDecorationCount() == 0 && itemDecoration > 0) {
            GridSpaceItemDecoration gridSpaceItemDecoration = new GridSpaceItemDecoration(spanCount, SizeUtils.dp2px(itemDecoration), includeEdge);
            recyclerView.addItemDecoration(gridSpaceItemDecoration);
        }
    }

    public static void setStaggeredGridManager(RecyclerView recyclerView, int spanCount, int itemDecoration) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                StaggeredGridLayoutManager.VERTICAL));
        if (itemDecoration > 0) {
            GridSpaceItemDecoration gridSpaceItemDecoration = new GridSpaceItemDecoration(spanCount, SizeUtils.dp2px(itemDecoration), false);
            recyclerView.addItemDecoration(gridSpaceItemDecoration);
        }
    }

    public static void setGridItemDecoration(RecyclerView recyclerView, int spanCount, int itemDecoration) {
        if (itemDecoration > 0) {
            GridSpaceItemDecoration gridSpaceItemDecoration = new GridSpaceItemDecoration(spanCount, SizeUtils.dp2px(itemDecoration), false);
            recyclerView.addItemDecoration(gridSpaceItemDecoration);
        }
    }


    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    public static class StaggeredGridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public StaggeredGridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
