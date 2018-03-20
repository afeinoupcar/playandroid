package com.afei.bat.afeiplayandroid.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.HomeList;
import com.afei.bat.afeiplayandroid.biz.AddRemoveModel;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.presenter.SearchPresenter;
import com.afei.bat.afeiplayandroid.ui.adapter.SearchAdapter;
import com.afei.bat.afeiplayandroid.ui.base.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/activity/SearchActivity")
public class SearchActivity extends BaseActivity implements ISearchView, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.activity_search_rv)
    RecyclerView activitySearchRv;
    @BindView(R.id.activity_search_srl)
    SwipeRefreshLayout activitySearchSrl;
    private SearchView mSearchView;
    private SearchPresenter searchPresenter = new SearchPresenter(this);
    private List<HomeList.DataBean.DatasBean> datasBeanList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SearchAdapter searchAdapter;
    private String search_k;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void findViewId() {
        ButterKnife.bind(this);
        addActivityInList(this);
        search_k = getIntent().getStringExtra(Constant.SEARCH_K);
        linearLayoutManager = new LinearLayoutManager(this);
        activitySearchRv.setLayoutManager(linearLayoutManager);
        searchAdapter = new SearchAdapter(datasBeanList);
        searchAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        activitySearchRv.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(this);
        searchAdapter.setOnItemChildClickListener(this);
        activitySearchSrl.setOnRefreshListener(this);
        searchAdapter.setOnLoadMoreListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        mSearchView.setMaxWidth(1920);
        mSearchView.setIconified(false);
        mSearchView.setQueryHint("请输入关键词");
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                SearchActivity.this.finish();
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                datasBeanList.clear();
                searchPresenter.list(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                datasBeanList.clear();
                if (!TextUtils.isEmpty(newText)) {
                    searchPresenter.list(newText);
                }
                return true;
            }

        });
        if (!TextUtils.isEmpty(search_k)) {
            mSearchView.setQuery(search_k, true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
    }

    @Override
    protected void initData(Bundle bundle) {
    }


    @Override
    public void listSuccess(HomeList.DataBean bean) {
        if (bean.getDatas() == null || bean.getDatas().isEmpty() || bean.getDatas().size() < Constant.PAGE_SIZE) {
            searchAdapter.loadMoreEnd(false);
        } else {
            searchAdapter.loadMoreComplete();
        }
        datasBeanList.addAll(bean.getDatas());
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void listFail(String message) {
        showSnackbar(message);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ARouter.getInstance().build("/activity/WebContentActivity")
                .withString(Constant.WEB_TITLE, String.valueOf(Html.fromHtml(datasBeanList.get(position).getTitle())))
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
                searchAdapter.notifyDataSetChanged();
            } else {
                AddRemoveModel.addAndRemove(datasBeanList.get(position).getId(), false);
                datasBeanList.get(position).setCollect(true);
                searchAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onRefresh() {
        searchAdapter.setEnableLoadMore(false);
        searchPresenter.refresh();
        datasBeanList.clear();
        searchAdapter.setNewData(datasBeanList);
        activitySearchSrl.setRefreshing(false);
        searchAdapter.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMoreRequested() {
        activitySearchSrl.setEnabled(false);
        searchPresenter.load();
        activitySearchSrl.setEnabled(true);
    }
}
