package com.afei.bat.afeiplayandroid.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.CollectList;
import com.afei.bat.afeiplayandroid.biz.AddRemoveModel;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.presenter.ILikeActivityPresenter;
import com.afei.bat.afeiplayandroid.ui.adapter.ILikeAdapter;
import com.afei.bat.afeiplayandroid.ui.base.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/activity/ILikeActivity")
public class ILikeActivity extends BaseActivity implements IILikeActivityView, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.activity_ilike_rv)
    RecyclerView activityIlikeRv;
    @BindView(R.id.activity_ilike_srl)
    SwipeRefreshLayout activityIlikeSrl;
    private ILikeActivityPresenter iLikeActivityPresenter = new ILikeActivityPresenter(this);
    private List<CollectList.DataBean.DatasBean> datasBeanList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ILikeAdapter iLikeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ilike;
    }

    @Override
    protected void findViewId() {
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(this);
        activityIlikeRv.setLayoutManager(linearLayoutManager);
        iLikeAdapter = new ILikeAdapter(datasBeanList);
        iLikeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        activityIlikeRv.setAdapter(iLikeAdapter);
        iLikeAdapter.setOnItemClickListener(this);
        iLikeAdapter.setOnItemChildClickListener(this);
        activityIlikeSrl.setOnRefreshListener(this);
        iLikeAdapter.setOnLoadMoreListener(this);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
    }

    @Override
    protected void initCenterTitle(TextView centerTitle) {
        centerTitle.setText("我喜欢的");
    }

    @Override
    protected void initData(Bundle bundle) {
        iLikeActivityPresenter.list();
    }

    @Override
    public void listSuccess(CollectList.DataBean dataBean) {
        if (dataBean.getDatas() == null || dataBean.getDatas().isEmpty() || dataBean.getDatas().size() < Constant.PAGE_SIZE) {
            iLikeAdapter.loadMoreEnd(false);
        } else {
            iLikeAdapter.loadMoreComplete();
        }
        datasBeanList.addAll(dataBean.getDatas());
        iLikeAdapter.notifyDataSetChanged();
    }

    @Override
    public void listFail(String message) {
        iLikeAdapter.loadMoreFail();
        showSnackbar(message);
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
            AddRemoveModel.addAndRemove(datasBeanList.get(position).getId(), true);
            datasBeanList.remove(position);
            iLikeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        iLikeAdapter.setEnableLoadMore(false);
        iLikeActivityPresenter.refresh();
        datasBeanList.clear();
        iLikeAdapter.setNewData(datasBeanList);
        activityIlikeSrl.setRefreshing(false);
        iLikeAdapter.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMoreRequested() {
        activityIlikeSrl.setEnabled(false);
        iLikeActivityPresenter.load();
        activityIlikeSrl.setEnabled(true);
    }
}
