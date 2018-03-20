package com.afei.bat.afeiplayandroid.ui.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MrLiu on 2018/1/8.
 */

public class BaseViewHolder {
    private final SparseArray<View> mViewSparseArray;
    private View mConvertView;

    public BaseViewHolder(Context context, ViewGroup viewGroup, int layoutId, int position) {
        this.mViewSparseArray = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        mConvertView.setTag(this);
    }

    public static BaseViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new BaseViewHolder(context, parent, layoutId, position);
        }
        return (BaseViewHolder) convertView.getTag();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {

        View view = mViewSparseArray.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViewSparseArray.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

}
