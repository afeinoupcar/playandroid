package com.ltf.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;

import android.util.TypedValue;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by justin on 16/12/3.
 */
public class SwipeToRefreshHelper implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RefreshListener mRefreshListener;
    private boolean mRefreshing;

    public interface RefreshListener {
        public void onRefreshStarted();
    }

    public SwipeToRefreshHelper(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RefreshListener listener) {
        init(activity, swipeRefreshLayout, listener);
    }

    public void init(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RefreshListener listener) {
        mRefreshListener = listener;
        mSwipeRefreshLayout = swipeRefreshLayout;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        mSwipeRefreshLayout.setProgressViewOffset(true, 20, 150);

        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 通过 setEnabled(false) 禁用下拉刷新
        mSwipeRefreshLayout.setEnabled(true);
    }

    public void setRefreshing(boolean refreshing) {
        mRefreshing = refreshing;
        // Delayed refresh, it fixes https://code.google.com/p/android/issues/detail?id=77712
        // 50ms seems a good compromise (always worked during tests) and fast enough so user can't notice the delay
        if (refreshing) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // use mRefreshing so if the refresh takes less than 50ms, loading indicator won't show up.
                    mSwipeRefreshLayout.setRefreshing(mRefreshing);
                }
            }, 50);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override
    public void onRefresh() {
        mRefreshListener.onRefreshStarted();
    }

    public void setEnabled(boolean enabled) {
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    public static TypedArray obtainStyledAttrsFromThemeAttr(Context context, int themeAttr, int[] styleAttrs) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(themeAttr, outValue, true);
        int styleResId = outValue.resourceId;
        return context.obtainStyledAttributes(styleResId, styleAttrs);
    }
}
