
package com.ltf.util.ui.refreshui;

import com.ltf.util.jsondata.BaseJsonData;
import com.ltf.util.network.RequestAction;

import java.util.List;

/**
 * Created by justin on 16/12/3.
 */
public abstract class LoadMoreFragment<T extends LoadMoreAdapter, EO extends BaseJsonData> extends RefreshFragment<T, EO> implements
        LoadMoreAdapter.LoadMoreCallback {

    /** 请求分页数据时的默认首页值，0 or 1 */
    private static int DEFAULT_FIRST_PAGE = 1;

    private int mCurrentPid = DEFAULT_FIRST_PAGE;// TODO 加载的id ,现在是用页数

    @Override
    public void onLastItemVisible() {
        setIsUpdating(true, RequestAction.REQUEST_LOADMORE, false, false);
    }

    @Override
    protected final T obtainRecyclerAdapter() {
        T adapter = obtainLoadMoreAdapter();
        adapter.setLoadMoreCallback(this);
        removeRVScrollListener(adapter);
        addRVScrollListener(adapter);
        return adapter;
    }

    @Override
    public void onDestroy() {
        removeRVScrollListener(getRecyclerAdapterWhenDestroy());
        super.onDestroy();
    }

    @Override
    protected void changeData(EO object, boolean succ) {
        if (object.isNetWorkData()) {
            setCanLoadmore(getCanLoadMore(object, succ));
        }
        super.changeData(object, succ);
    }

    public int getCurrentPid() {
        return mCurrentPid;
    }

    /** 重置当前的currentPid **/
    protected void resetCurrentPid() {
        mCurrentPid = DEFAULT_FIRST_PAGE;
    }

    /** 改变页数 **/
    private void changeCurrentPid() {
        mCurrentPid++;
    }

    /** 设置能否加载更多 **/
    private void setCanLoadmore(boolean response) {
        if (response) {
            changeCurrentPid();
        }
        getRecyclerAdapter().setCanLoadMore(response);
    }

    protected void resultUpdate(boolean succ) {
        if (getRecyclerAdapter().getMoreViewHolder() != null) {
            getRecyclerAdapter().getMoreViewHolder().updateLoadMoreText(succ);
        }
    }

    protected abstract T obtainLoadMoreAdapter();
    protected abstract void loadMore();

    @Override
    protected void refresh(boolean init) {
        resetCurrentPid();
    }

    @Override
    protected final void updateLoadMore(boolean isUpdating, boolean isSucc) {
        super.updateLoadMore(isUpdating, isSucc);
        if (isUpdating) {
            // 本地加载更多 或者 发起加载更多的请求
            loadMore();
        } else {
            //
            resultUpdate(isSucc);
        }
    }

    /** 判断能否加载更多 **/
    protected abstract boolean getCanLoadMore(EO object, boolean succ);

    protected void addLocalMoreData(List moreData) {
        if (moreData != null && moreData.size() > 0) {
            getRecyclerAdapter().addData(moreData);
            updateUiForResult(RequestAction.REQUEST_LOADMORE, true);
        } else {
            updateUiForResult(RequestAction.REQUEST_LOADMORE, false);
        }
    }

    protected final void addRVScrollListener(T t) {
        if (getRecyclerView() != null && t != null) {
            getRecyclerView().addOnScrollListener(t.getScrollListener());
        }
    }

    protected final void removeRVScrollListener(T t) {
        if (getRecyclerView() != null && t != null) {
            getRecyclerView().addOnScrollListener(t.getScrollListener());
        }
    }
}
