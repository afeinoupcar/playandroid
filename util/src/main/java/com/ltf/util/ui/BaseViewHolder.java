package com.ltf.util.ui;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by justin on 16/12/3.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {

    private IViewHolderListener mListener;

    public BaseViewHolder(View itemView, IViewHolderListener listener) {
        this(itemView, listener, false);
    }

    public BaseViewHolder(View itemView, IViewHolderListener listener, boolean needClick) {
        super(itemView);
        if (listener != null || needClick) {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener!= null) {
            mListener.holderOnClickListener(this, v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener!= null) {
            mListener.holderOnLongClickListener(this, v, getLayoutPosition());
            return true;
        }
        return false;
    }


    /**
     * Created by jiantian1 on 2015/4/24.
     */
    public interface IViewHolderListener<T extends BaseViewHolder> {

        void holderOnClickListener(T holder, View v, int position);

        void holderOnLongClickListener(T holder, View v, int position);

    }

}
