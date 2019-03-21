package com.ltf.util.ui;


import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by justin on 16/12/3.
 * 单元表格cell
 *  _______
 * |   a   |
 * |_______|
 * | b | c |
 * |___|___|
 *
 *  ___________
 * |     a     |
 * |___________|
 * | b | c | d |
 * |___|___|___|
 *
 *
 */
public abstract class BaseRecyclerAdapter2<VH extends BaseViewHolder, T> extends BaseRecyclerAdapter<VH, T> {

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return needJoinCell(position)
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && needJoinCell(holder.getLayoutPosition())) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    /** 是否需要合并行单元格 **/
    public abstract boolean needJoinCell(int position);

}
