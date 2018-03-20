package com.afei.bat.afeiplayandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.StudyList;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.presenter.StudyFragmentPresenter;
import com.afei.bat.afeiplayandroid.ui.adapter.StudyAdapter;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ltf.util.ui.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class StudyFragment extends BaseFragment implements IStudyFragmentView, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.fragment_study_rv)
    RecyclerView fragmentStudyRv;
    @BindView(R.id.fragment_study_srl)
    SwipeRefreshLayout fragmentStudySrl;
    Unbinder unbinder;
    private StudyFragmentPresenter studyFragmentPresenter = new StudyFragmentPresenter(this);
    private List<StudyList.DataBean> datasBeanList = new ArrayList<>();
    private StudyAdapter studyAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    protected void initView(View parent) {
        unbinder = ButterKnife.bind(this, parent);
        fragmentStudyRv.setLayoutManager(new LinearLayoutManager(getContext()));
        studyAdapter = new StudyAdapter(datasBeanList);
        studyAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        fragmentStudyRv.setAdapter(studyAdapter);
        studyAdapter.setOnItemClickListener(this);
        fragmentStudySrl.setOnRefreshListener(this);
    }

    @Override
    protected void initData(Bundle bundle) {
        studyFragmentPresenter.list();
    }

    public static StudyFragment newInstance() {
        return new StudyFragment();
    }

    @Override
    public void listSuccess(List<StudyList.DataBean> beanList) {
        datasBeanList.clear();
        datasBeanList.addAll(beanList);
        studyAdapter.notifyDataSetChanged();
    }

    @Override
    public void listFail(String message) {
        showSnackbar(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ARouter.getInstance().build("/activity/KnowledgeActivity")
                .withString(Constant.KNOWLEDGE_TITLE, studyAdapter.getItem(position).getName())
                .withSerializable(Constant.KNOWLEDGE_DATAS, (Serializable) studyAdapter.getItem(position).getChildren())
                .navigation();
    }

    @Override
    public void onRefresh() {
        studyFragmentPresenter.refresh();
        studyAdapter.setNewData(datasBeanList);
        fragmentStudySrl.setRefreshing(false);
    }
}
