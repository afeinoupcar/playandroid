package com.afei.bat.afeiplayandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.StudyList;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.ui.base.BaseActivity;
import com.afei.bat.afeiplayandroid.ui.fragment.KnowledgeFragment;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/activity/KnowledgeActivity")
public class KnowledgeActivity extends BaseActivity {
    private static final String TAG = "KnowledgeActivity";
    @BindView(R.id.activity_knowledge_vp)
    ViewPager activityKnowledgeVp;
    private String knowledgeTitle;
    private List<StudyList.DataBean.ChildrenBean> childrenBeanList;
    private int cid;
    private KnowledgeFragment knowledgeFragment;
    private TabLayout mTabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_knowledge;
    }

    @Override
    protected void findViewId() {
        ButterKnife.bind(this);
        addActivityInList(this);
        knowledgeTitle = getIntent().getStringExtra(Constant.KNOWLEDGE_TITLE);
        childrenBeanList = (List<StudyList.DataBean.ChildrenBean>) getIntent().getSerializableExtra(Constant.KNOWLEDGE_DATAS);
        cid = getIntent().getIntExtra(Constant.KNOWLEDGE_CID, -1);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
    }

    @Override
    protected void initCenterTitle(TextView centerTitle) {
        centerTitle.setText(knowledgeTitle);
    }

    @Override
    protected void initTabLayout(TabLayout mTabLayout) {
        this.mTabLayout = mTabLayout;
        mTabLayout.setVisibility(View.VISIBLE);
        activityKnowledgeVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                if (childrenBeanList == null || childrenBeanList.size() <= 0) {
                    return knowledgeTitle;
                }
                return childrenBeanList.get(position).getName();
            }

            @Override
            public Fragment getItem(int position) {
                knowledgeFragment = new KnowledgeFragment();
                Bundle b = new Bundle();
                b.putInt(Constant.POSITION, position);
                b.putSerializable(Constant.KNOWLEDGE_DATAS, (Serializable) childrenBeanList);
                b.putInt(Constant.KNOWLEDGE_CID, cid);
                knowledgeFragment.setArguments(b);
                return knowledgeFragment;
            }

            @Override
            public int getCount() {
                if (childrenBeanList == null || childrenBeanList.size() <= 0) {
                    return 1;
                }
                return childrenBeanList.size();
            }
        });
        mTabLayout.setupWithViewPager(activityKnowledgeVp);
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_type, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShare) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (childrenBeanList == null || childrenBeanList.size() <= 0) {
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_type_url, getString(R.string.app_name),
                        knowledgeTitle, cid));
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_type_url, getString(R.string.app_name),
                        childrenBeanList.get(mTabLayout.getSelectedTabPosition()).getName(), childrenBeanList.get(mTabLayout.getSelectedTabPosition()).getId()));
            }

            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
        } else if (item.getItemId() == R.id.menuSearch) {
            ARouter.getInstance().build("/activity/SearchActivity").navigation();
        }
        return super.onOptionsItemSelected(item);
    }
}
