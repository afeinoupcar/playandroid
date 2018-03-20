package com.afei.bat.afeiplayandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.HomeList;
import com.afei.bat.afeiplayandroid.bean.KnowledgeList;
import com.afei.bat.afeiplayandroid.bean.StudyList;
import com.afei.bat.afeiplayandroid.biz.AddRemoveModel;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.presenter.KnowledgeFragmentPresenter;
import com.afei.bat.afeiplayandroid.ui.adapter.HomeAdapter;
import com.afei.bat.afeiplayandroid.ui.adapter.KnowledgeAdapter;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ltf.util.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MrLiu on 2018/3/12.
 */

public class KnowledgeFragment extends BaseFragment implements IKnowledgeFragmentView, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.fragment_knowledge_srl)
    SwipeRefreshLayout fragmentKnowledgeSrl;
    Unbinder unbinder;
    @BindView(R.id.fragment_knowledge_rv)
    RecyclerView fragmentKnowledgeRv;
    private int position;
    private List<StudyList.DataBean.ChildrenBean> childrenBeanList;
    private LinearLayoutManager linearLayoutManager;
    private KnowledgeAdapter knowledgeAdapter;
    private List<KnowledgeList.DataBean.DatasBean> datasBeanList = new ArrayList<>();
    private KnowledgeFragmentPresenter knowledgeFragmentPresenter = new KnowledgeFragmentPresenter(this);
    private int cid;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge;
    }

    @Override
    protected void initView(View parent) {
        unbinder = ButterKnife.bind(this, parent);
        position = getArguments().getInt(Constant.POSITION);
        childrenBeanList = (List<StudyList.DataBean.ChildrenBean>) getArguments().getSerializable(Constant.KNOWLEDGE_DATAS);
        cid = getArguments().getInt(Constant.KNOWLEDGE_CID);
        linearLayoutManager = new LinearLayoutManager(getContext());
        fragmentKnowledgeRv.setLayoutManager(linearLayoutManager);
        knowledgeAdapter = new KnowledgeAdapter(datasBeanList);
        knowledgeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        fragmentKnowledgeRv.setAdapter(knowledgeAdapter);
        knowledgeAdapter.setOnItemClickListener(this);
        knowledgeAdapter.setOnItemChildClickListener(this);
        fragmentKnowledgeSrl.setOnRefreshListener(this);
        knowledgeAdapter.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData(Bundle bundle) {
        knowledgeFragmentPresenter.list();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void listSuccess(KnowledgeList.DataBean dataBean) {
        if (dataBean.getDatas() == null || dataBean.getDatas().isEmpty() || dataBean.getDatas().size() < Constant.PAGE_SIZE) {
            knowledgeAdapter.loadMoreEnd(false);
        } else {
            knowledgeAdapter.loadMoreComplete();
        }
        datasBeanList.addAll(dataBean.getDatas());
        knowledgeAdapter.notifyDataSetChanged();
    }

    @Override
    public void listFail(String message) {
        knowledgeAdapter.loadMoreFail();
        showSnackbar(message);
    }

    @Override
    public int getCid() {
        if(childrenBeanList==null||childrenBeanList.size()<=0){
            return cid;
        }
        return childrenBeanList.get(position).getId();
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
        if (view.getId() == R.id.home_adapter_item_collection) {
            if (datasBeanList.get(position).isCollect()) {
                AddRemoveModel.addAndRemove(datasBeanList.get(position).getId(), true);
                datasBeanList.get(position).setCollect(false);
                knowledgeAdapter.notifyDataSetChanged();
            } else {
                AddRemoveModel.addAndRemove(datasBeanList.get(position).getId(), false);
                datasBeanList.get(position).setCollect(true);
                knowledgeAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onRefresh() {
        knowledgeAdapter.setEnableLoadMore(false);
        knowledgeFragmentPresenter.refresh();
        datasBeanList.clear();
        knowledgeAdapter.setNewData(datasBeanList);
        fragmentKnowledgeSrl.setRefreshing(false);
        knowledgeAdapter.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMoreRequested() {
        fragmentKnowledgeSrl.setEnabled(false);
        knowledgeFragmentPresenter.load();
        fragmentKnowledgeSrl.setEnabled(true);
    }
}
