package com.ltf.util.ui.refreshui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ltf.util.R;
import com.ltf.util.ui.BaseRecyclerAdapter;
import com.ltf.util.ui.BaseViewHolder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by justin on 16/12/3.
 * base loadmore
 */
public abstract class LoadMoreAdapter<VH extends BaseViewHolder, T> extends BaseRecyclerAdapter<VH, T> {

    public static final int ITEM_TYPE_GENERAL = 0;// 默认类型
    public static final int ITEM_TYPE_LOADMORE = ITEM_TYPE_GENERAL + 1;// 加载更多类型

    protected LayoutInflater mInflater;

    private boolean mLastItemVisible;
    private boolean mCanLoadMore = false;

    private RecyclerView.OnScrollListener mScrollListener;
    private MoreViewHolder moreViewHolder;

    private LoadMoreCallback mCallback;

    public LoadMoreAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mScrollListener = new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastItemVisible) {
                    if (canLoadMore()) {
                        onLastItemVisible();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = manager.findLastVisibleItemPosition();
                int totalItemCount = getItemCount();
                mLastItemVisible = (totalItemCount > 0) && dy > 0 && (position >= totalItemCount - 1);
            }
        };
    }

    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = null;
        switch (viewType) {
            case ITEM_TYPE_LOADMORE:
                moreViewHolder = new MoreViewHolder(mInflater, null);
                moreViewHolder.setAdapter(this);
                holder = (VH) moreViewHolder;
                break;
            default:
                holder = super.onCreateViewHolder(parent, viewType);
                break;
        }
        return holder;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (holder instanceof MoreViewHolder) {

        } else {
            handlerViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (needMoreView() && (position - getOtherCount() == getDataSize())) {
            return ITEM_TYPE_LOADMORE;
        } else {
            return ITEM_TYPE_GENERAL;
        }
    }

    /** 是否能加载更多，默认不需要，需要的话重新该方法 **/
    public boolean canLoadMore() {
        return mCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }

    /** 是否需要加载更多的功能 **/
    public boolean needMoreView() {
        return true;
    }

    /** 其他项数量 **/
    public int getOtherCount() {
        return 0;
    }

    @Override
    public final int getItemCount() {
        return canLoadMore() ? super.getItemCount() + 1 : super.getItemCount();
    }

    public static class MoreViewHolder extends BaseViewHolder {
        private TextView load_more_text;
        private ProgressBar progressbar;
        private LoadMoreAdapter adapter;

        public MoreViewHolder(LayoutInflater inflater, IViewHolderListener listener) {
            super(inflater.inflate(R.layout.layout_load_more_foot, null), listener, true);
            load_more_text = (TextView) itemView.findViewById(R.id.load_more_text);
            load_more_text.setTag(R.string.text_list_footer_loading);
            progressbar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }

        public void updateLoadMoreText(boolean succ) {
            if (load_more_text != null) {
                if (succ) {
                    load_more_text.setText(R.string.text_list_footer_loading);
                    load_more_text.setTag(R.string.text_list_footer_loading);
                    if (progressbar != null) {
                        progressbar.setVisibility(View.VISIBLE);
                    }
                } else {
                    load_more_text.setText(R.string.text_list_footer_load_fail);
                    load_more_text.setTag(R.string.text_list_footer_load_fail);
                    if (progressbar != null) {
                        progressbar.setVisibility(View.GONE);
                    }
                }
            }
        }

        public boolean updateLoadIsFailed() {
            if (load_more_text != null) {
                int tag = (Integer) load_more_text.getTag();
                return tag == R.string.text_list_footer_load_fail;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            if (load_more_text != null) {
                int tag = (Integer) load_more_text.getTag();
                if (tag == R.string.text_list_footer_load_fail) {
                    load_more_text.setText(R.string.text_list_footer_loading);
                    load_more_text.setTag(R.string.text_list_footer_loading);
                    if (progressbar != null) {
                        progressbar.setVisibility(View.VISIBLE);
                    }
                    if (adapter!= null) {
                        adapter.onLastItemVisible();
                    }
                }
            }
        }

        public void setAdapter(LoadMoreAdapter adapter) {
            this.adapter = adapter;
        }
    }

    public abstract void handlerViewHolder(VH holder, int position);
    
    private void onLastItemVisible() {
        if (mCallback != null) {
            mCallback.onLastItemVisible();
        }
    }

    public final MoreViewHolder getMoreViewHolder() {
        return moreViewHolder;
    }

    public void setLoadMoreCallback(LoadMoreCallback callback) {
        mCallback = callback;
    }

    public interface LoadMoreCallback {
        /** 加载更多时候调用 **/
        void onLastItemVisible();
    }

}
