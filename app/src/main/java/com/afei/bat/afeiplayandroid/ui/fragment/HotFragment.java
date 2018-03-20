package com.afei.bat.afeiplayandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.HotKey;
import com.afei.bat.afeiplayandroid.bean.UseUrl;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.presenter.HotFragmentPresenter;
import com.afei.bat.afeiplayandroid.ui.adapter.HotAdapter;
import com.afei.bat.afeiplayandroid.ui.adapter.HotKeyAndUrlAdapter;
import com.afei.bat.afeiplayandroid.ui.adapter.StudyAdapter;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ltf.util.ui.BaseFragment;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MrLiu on 2018/3/13.
 */

public class HotFragment extends BaseFragment implements IHotFragmentView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.fragment_hot_rv)
    RecyclerView fragmentHotRv;
    @BindView(R.id.fragment_hot_srl)
    SwipeRefreshLayout fragmentHotSrl;
    Unbinder unbinder;
    private HotFragmentPresenter hotFragmentPresenter = new HotFragmentPresenter(this);
    private HotAdapter hotAdapter;
    private HotKeyAndUrlAdapter<HotKey.DataBean> mHotKeyAdapter;
    private HotKeyAndUrlAdapter<UseUrl.DataBean> mHotUrlAdapter;
    private View mHotHeadView;
    private TagFlowLayout mTflHotKeys, mTflUseUrl;
    private List<HotKey.DataBean> hotKeyList = new ArrayList<>();
    private List<UseUrl.DataBean> useUrlList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initView(View parent) {
        unbinder = ButterKnife.bind(this, parent);
        fragmentHotRv.setLayoutManager(new LinearLayoutManager(getContext()));
        hotAdapter = new HotAdapter();
        hotAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        fragmentHotRv.setAdapter(hotAdapter);
        mHotHeadView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hot_head, null);
        mTflHotKeys = (TagFlowLayout) mHotHeadView.findViewById(R.id.fragment_hot_head_tfl_search);
        mTflUseUrl = (TagFlowLayout) mHotHeadView.findViewById(R.id.fragment_hot_head_tfl_url);
        hotAdapter.addHeaderView(mHotHeadView);
        fragmentHotSrl.setOnRefreshListener(this);
        mHotKeyAdapter = new HotKeyAndUrlAdapter<>(getContext(), hotKeyList);
        mTflHotKeys.setAdapter(mHotKeyAdapter);
        mHotUrlAdapter = new HotKeyAndUrlAdapter<>(getContext(), useUrlList);
        mTflUseUrl.setAdapter(mHotUrlAdapter);
        mTflHotKeys.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String search_k = mHotKeyAdapter.getItem(position).getName();
                ARouter.getInstance().build("/activity/SearchActivity")
                        .withString(Constant.SEARCH_K, search_k)
                        .navigation();
                return false;
            }
        });
        mTflUseUrl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ARouter.getInstance().build("/activity/WebContentActivity")
                        .withString(Constant.WEB_TITLE, useUrlList.get(position).getName())
                        .withString(Constant.WEB_URL, useUrlList.get(position).getLink())
                        .navigation();
                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle bundle) {
        hotFragmentPresenter.hotKey();
        hotFragmentPresenter.useUrl();
    }

    public static HotFragment newInstance() {
        return new HotFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void hotKeySuccess(List<HotKey.DataBean> beanList) {
        hotKeyList.clear();
        hotKeyList.addAll(beanList);
        mHotKeyAdapter.notifyDataChanged();
    }

    @Override
    public void hotKeyFail(String message) {
        showSnackbar(message);
    }

    @Override
    public void useUrlSuccess(List<UseUrl.DataBean> beanList) {
        useUrlList.clear();
        useUrlList.addAll(beanList);
        mHotUrlAdapter.notifyDataChanged();
    }

    @Override
    public void useUrlFail(String message) {
        showSnackbar(message);
    }

    @Override
    public void onRefresh() {
        hotFragmentPresenter.refresh();
        mHotKeyAdapter.notifyDataChanged();
        mHotUrlAdapter.notifyDataChanged();
        fragmentHotSrl.setRefreshing(false);
    }
}
