package com.ltf.util.ui.refreshui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ltf.util.LogUtil;
import com.ltf.util.R;
import com.ltf.util.SwipeToRefreshHelper;
import com.ltf.util.jsondata.BaseJsonData;
import com.ltf.util.network.NetworkUtils;
import com.ltf.util.network.OkHttpUtils;
import com.ltf.util.network.RequestAction;
import com.ltf.util.network.RequestError;
import com.ltf.util.ui.BaseFragment;
import com.ltf.util.ui.BaseRecyclerAdapter;

import java.util.List;

/**
 * Created by justin on 16/12/3.
 */
public abstract class RefreshFragment<T extends BaseRecyclerAdapter, EO extends BaseJsonData> extends BaseFragment {

    private static final String TAG = RefreshFragment.class.getSimpleName();
    private boolean mIsUpdating;//请求标志，且刷新和加载更多只能同时操作一个

    private SwipeRefreshLayout mSwipeToRefreshLayout;
    public SwipeToRefreshHelper mSwipeToRefreshHelper;

    private RecyclerView mRecyclerView;
    private T mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt onCreate ");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt onViewCreated ");
    }

    @Override
    public void onStart() {
        super.onStart();
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt onStart EventBus register ");
    }

    @Override
    public void onStop() {
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt onStop EventBus unregister ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt onDestroyView ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt onDestroy ");
    }

    @Override
    protected void initView(View parent) {
        // swipe to refresh setup
        mSwipeToRefreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.ptr_layout);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.recycler_view);
        if ((needSwipeToRefreshLayout() && mSwipeToRefreshLayout == null) || (needRecyclerView() && mRecyclerView == null)) {
            throw new NullPointerException(getClassTag() +
                    " mSwipeToRefreshLayout is empty, please check id is 'ptr_layout'." +
                            " Or mRecyclerView is empty, please check id is 'recycler_view'");
        }
        if (needSwipeToRefreshLayout()) {
            mSwipeToRefreshHelper = new SwipeToRefreshHelper(getActivity(), mSwipeToRefreshLayout,
                    new SwipeToRefreshHelper.RefreshListener() {
                        @Override
                        public void onRefreshStarted() {
                            refresh();
                        }
                    }
            );
        }

        if (needRecyclerView()) {
            mRecyclerView.setHasFixedSize(false);
            initRecyclerView(mRecyclerView);

            mRecyclerView.setAdapter(getRecyclerAdapter());
        }

        if (needInitData()) {
            // 获取数据
            parent.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setIsUpdating(true, RequestAction.REQUEST_REFRESH, true, false);
                }
            }, 50);
        }
    }

    protected void showSwipeToRefreshProgress(boolean showProgress) {
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt setIsUpdating == mSwipeToRefreshHelper.isRefreshing() = " + (mSwipeToRefreshHelper == null ? " is null" : mSwipeToRefreshHelper.isRefreshing()) + " , showProgress = " + showProgress);
        if (mSwipeToRefreshHelper != null && mSwipeToRefreshHelper.isRefreshing() != showProgress) {
            mSwipeToRefreshHelper.setRefreshing(showProgress);
        }
    }

    protected final void setIsUpdating(boolean isUpdating, RequestAction updateAction, boolean init, boolean isSucc) {
//        AppLog.d(AppLog.T.API, getClassTag() + " -- zjt setIsUpdating == canUpdateUI() = " + canUpdateUI() + " , mIsUpdating = " + mIsUpdating + " , isUpdating = " + isUpdating+" , updateAction = "+updateAction);
        if (!canUpdateUI() || mIsUpdating == isUpdating) {
            if (updateAction == RequestAction.REQUEST_REFRESH) {
                //解决当正在加载更多的时候又去刷新，ui一直不隐藏的问题（或者，重写SwipeRefreshLayout.canChildScrollUp方法，判断mIsUpdating变量，最合理）
                showSwipeToRefreshProgress(false);
            } else if (updateAction == RequestAction.REQUEST_LOADMORE) {
                //TODO 原因同上
            }
            return;
        }

        if (updateAction == RequestAction.REQUEST_LOADMORE) {
            // show/hide progress bar at bottom if these are older posts
//            showLoadingProgress(isUpdating);
            updateLoadMore(isUpdating, isSucc);
        } else {
            if (isUpdating /*&& isPostAdapterEmpty()*/) {
                // show swipe-to-refresh if update started and no posts are showing
                showSwipeToRefreshProgress(true);
                refresh(init);
            } else {
                // hide swipe-to-refresh progress if update is complete
                showSwipeToRefreshProgress(false);
            }
        }
        mIsUpdating = isUpdating;
    }


    public final T getRecyclerAdapter() {
        if (mAdapter == null) {
            mAdapter = obtainRecyclerAdapter();
        }
        return mAdapter;
    }

    public final T getRecyclerAdapterWhenDestroy() {
        return mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    //子类实现方法

    /** 取消请求，如果有请求的话 **/
    protected void cancelRequestByTag(Object... tags) {
        if (tags == null) {
            throw new RuntimeException("cancel Tag must be not null");
        }
        for (Object obj : tags) {
            OkHttpUtils.cancelRequestByTag(obj);
        }
    }

    /**
     * 对recyclerview的设置
     * @param recyclerView
     */
    protected abstract void initRecyclerView(RecyclerView recyclerView);

    /**
     * 获取adapter
     * @return
     */
    protected abstract T obtainRecyclerAdapter();

    /**
     * 刷新
     * @param init 是否是初始化
     */
    protected abstract void refresh(boolean init);

    /**
     * 返回adapter的数据
     * @param object
     * @return
     */
    protected abstract List getData(EO object);

    /**
     * REQUEST_LOADMORE 使用
     * @param isUpdating 是否正在更新
     * @param isSucc 是否成功
     */
    protected void updateLoadMore(boolean isUpdating, boolean isSucc){}

    /** 更新数据失败 */
    protected abstract void loadDataFail(boolean isLoadMore, EO object);

    //--- end ---

    /** 在主线程接收EO事件，必须是public void */
    public void mainThread(final EO object) {
        LogUtil.d(TAG, getClass() + " -- zjt onEventMainThread == "+object +" -- type = "+ (object == null ? "null" : object.getType()));

        if (canUpdateUI()) {
            if (object != null) {
                boolean succ = object.isSucc();
                changeData(object, succ);
            }
            if (object != null)  {
                updateUiForResult(object.getAction(), object.isSucc());
            } else {
                if (mSwipeToRefreshLayout != null) {
                    mSwipeToRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateLoadMore(false, false);
                            showSwipeToRefreshProgress(true);
                        }
                    }, 200);
                } else {
                    updateLoadMore(false, false);
                    showSwipeToRefreshProgress(true);
                }
            }
        }
    }

    public void mainThread(final RequestError<EO> error) {
        if (error != null) {
            LogUtil.d(TAG, getClass() + " -- zjt onEventMainThread == " + error + " -- error.msg = " + error.getError());
        }
        if (canUpdateUI()) {
            updateUiForResult(error.getAction(), false);
        }
    }

    protected void changeData(EO object, boolean succ) {
        if (object.getAction() == RequestAction.REQUEST_REFRESH) {
            if (succ) {
                List data = getData(object);
                if (checkData(data)) {
                    if(object != null && object.isAddData()) {
                        getRecyclerAdapter().addData(true, data);
                    } else {
                        getRecyclerAdapter().setData(data);
                    }
                }
            } else {
                loadDataFail(false, object);
            }
        } else {
            if (succ) {
                getRecyclerAdapter().addData(getData(object));
            } else {
                loadDataFail(true, object);
            }
        }
    }

    protected void updateUiForResult(final RequestAction action, final boolean isSucc) {
        if (mSwipeToRefreshLayout != null) {
            mSwipeToRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setIsUpdating(false, action, false, isSucc);
                }
            }, 200);
        } if (mRecyclerView != null) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setIsUpdating(false, action, false, isSucc);
                }
            }, 200);
        } else {
            setIsUpdating(false, action, false, isSucc);
        }
    }

    private String getClassTag() {
        return this.getClass().getSimpleName().toString();
    }

    /** 不需要SwipeToRefreshLayout的话，覆盖此方法 showSwipeToRefreshProgress方法 **/
    protected boolean needSwipeToRefreshLayout() {
        return true;
    }

    protected boolean needRecyclerView() {
        return true;
    }
    /** 是否需要初始化数据 **/
    protected boolean needInitData() {
        return true;
    }

    /** 刷新方法，没有SwipeToRefreshLayout情况下，需要的话，手动调用 **/
    public final void refresh() {
        if (!canUpdateUI()) {
            return;
        }
        if (!NetworkUtils.checkConnection(getActivity())) {
            showSwipeToRefreshProgress(false);
            updateUIWhenNotConnection();
            return;
        }
        setIsUpdating(true, RequestAction.REQUEST_REFRESH, false, false);
    }

    @Override
    protected void initData(Bundle bundle) {
        // TODO
    }

    /**
     * 核对数据之用，etg 当服务端返回null数据时 客户端是否需要覆盖本地
     * 需要子类去检测就覆盖这个方法
     * @param data
     * @return
     */
    protected boolean checkData(List data) {
        return true;
    }

    public boolean isIsUpdating() {
        return mIsUpdating;
    }


    /**
     * 平滑滚动到头部
     */
    public void smoothScrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    public boolean canScrollUp() {
        return mRecyclerView != null && mRecyclerView.canScrollVertically(-1);
    }

    public boolean canScrollDown() {
        return mRecyclerView != null && mRecyclerView.canScrollVertically(1);
    }

    /** 无网络刷新调用 **/
    protected void updateUIWhenNotConnection() {

    }
}
