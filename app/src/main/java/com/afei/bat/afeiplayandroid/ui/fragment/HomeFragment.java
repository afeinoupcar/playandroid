package com.afei.bat.afeiplayandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.HomeList;
import com.afei.bat.afeiplayandroid.biz.AddRemoveModel;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.event.LoginEvent;
import com.afei.bat.afeiplayandroid.event.RefreshEvent;
import com.afei.bat.afeiplayandroid.presenter.HomeFragmentPresenter;
import com.afei.bat.afeiplayandroid.ui.adapter.HomeAdapter;
import com.afei.bat.afeiplayandroid.util.GlideImageLoader;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ltf.util.ui.BaseFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class HomeFragment extends BaseFragment implements IHomeFragmentView, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.fragment_home_rv)
    RecyclerView fragmentHomeRv;
    @BindView(R.id.fragment_home_srl)
    SwipeRefreshLayout fragmentHomeSrl;
    @BindView(R.id.fragment_home_backtv)
    TextView fragmentHomeBacktv;
    Unbinder unbinder1;
    private Banner banner;
    private Unbinder unbinder;
    private HomeFragmentPresenter homeFragmentPresenter = new HomeFragmentPresenter(this);
    private View mHomeBannerHeadView;
    private HomeAdapter homeAdapter;
    private List<HomeList.DataBean.DatasBean> datasBeanList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View parent) {
        unbinder = ButterKnife.bind(this, parent);
        EventBus.getDefault().register(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        fragmentHomeRv.setLayoutManager(linearLayoutManager);
        homeAdapter = new HomeAdapter(datasBeanList);
        homeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        fragmentHomeRv.setAdapter(homeAdapter);
        mHomeBannerHeadView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_banner_head, null);
        banner = mHomeBannerHeadView.findViewById(R.id.fragment_banner);
        homeAdapter.addHeaderView(mHomeBannerHeadView);
        homeAdapter.setOnItemClickListener(this);
        homeAdapter.setOnItemChildClickListener(this);
        fragmentHomeSrl.setOnRefreshListener(this);
        homeAdapter.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData(Bundle bundle) {
        homeFragmentPresenter.banner();
        homeFragmentPresenter.list();
        fragmentHomeRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0) {
                    if (linearLayoutManager.findFirstVisibleItemPosition() > 1) {
                        fragmentHomeBacktv.setVisibility(View.VISIBLE);
                    } else {
                        fragmentHomeBacktv.setVisibility(View.GONE);
                    }
                } else {
                    fragmentHomeBacktv.setVisibility(View.GONE);
                }
            }
        });
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ARouter.getInstance().build("/activity/WebContentActivity")
                .withString(Constant.WEB_TITLE, datasBeanList.get(position).getTitle())
                .withString(Constant.WEB_URL, datasBeanList.get(position).getLink())
                .navigation();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.home_adapter_item_name) {
            ARouter.getInstance().build("/activity/KnowledgeActivity")
                    .withString(Constant.KNOWLEDGE_TITLE, datasBeanList.get(position).getChapterName())
                    .withInt(Constant.KNOWLEDGE_CID, datasBeanList.get(position).getChapterId())
                    .navigation();
        }
        if (view.getId() == R.id.home_adapter_item_collection) {
            if (datasBeanList.get(position).isCollect()) {
                AddRemoveModel.addAndRemove(datasBeanList.get(position).getId(), true);
                datasBeanList.get(position).setCollect(false);
                homeAdapter.notifyDataSetChanged();
            } else {
                AddRemoveModel.addAndRemove(datasBeanList.get(position).getId(), false);
                datasBeanList.get(position).setCollect(true);
                homeAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onLoadMoreRequested() {
        fragmentHomeSrl.setEnabled(false);
        homeFragmentPresenter.load();
        fragmentHomeSrl.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        homeAdapter.setEnableLoadMore(false);
        homeFragmentPresenter.refresh();
        datasBeanList.clear();
        homeAdapter.setNewData(datasBeanList);
        fragmentHomeSrl.setRefreshing(false);
        homeAdapter.setEnableLoadMore(true);
    }

    @Override
    public void bannerSuccess(final List<HomeBanner.DataBean> beanList) {
        List<String> images = new ArrayList();
        List<String> titles = new ArrayList();
        for (HomeBanner.DataBean d : beanList) {
            images.add(d.getImagePath());
            titles.add(d.getTitle());
        }
        banner.setImages(images)
                .setBannerTitles(titles)
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new GlideImageLoader())
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ARouter.getInstance().build("/activity/WebContentActivity")
                        .withString(Constant.WEB_TITLE, beanList.get(position).getTitle())
                        .withString(Constant.WEB_URL, beanList.get(position).getUrl())
                        .navigation();
            }
        });
    }

    @Override
    public void bannerFail(String message) {
        showSnackbar(message);
    }

    @Override
    public void listSuccess(HomeList.DataBean bean) {
        if (bean.getDatas() == null || bean.getDatas().isEmpty() || bean.getDatas().size() < Constant.PAGE_SIZE) {
            homeAdapter.loadMoreEnd(false);
        } else {
            homeAdapter.loadMoreComplete();
        }
        datasBeanList.addAll(bean.getDatas());
        homeAdapter.notifyDataSetChanged();
    }

    @Override
    public void listFail(String message) {
        homeAdapter.loadMoreFail();
        showSnackbar(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshEvent(RefreshEvent refreshEvent) {
        homeAdapter.setEnableLoadMore(false);
        homeFragmentPresenter.refresh();
        datasBeanList.clear();
        homeAdapter.setNewData(datasBeanList);
        fragmentHomeSrl.setRefreshing(false);
        homeAdapter.setEnableLoadMore(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.fragment_home_backtv)
    public void onViewClicked() {
        fragmentHomeRv.smoothScrollToPosition(0);
    }

}
