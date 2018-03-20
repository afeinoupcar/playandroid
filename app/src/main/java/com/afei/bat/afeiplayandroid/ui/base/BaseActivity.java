package com.afei.bat.afeiplayandroid.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.R;
import com.ltf.util.SnackbarUtils;
import com.ltf.util.ToastUtils;


public abstract class BaseActivity extends AppCompatActivity {

    private TextView mToolbarCenterTitle;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;
    private FrameLayout mFrameLayout;
    private CoordinatorLayout mCoordinatorLayout;
    protected ProgressDialog mProgressDialog;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRoate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBaseLayoutId());

        mToolbarCenterTitle = (TextView) findViewById(R.id.toolbar_center_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.ac_base_appbar);
        mTabLayout = (TabLayout) findViewById(R.id.ac_base_tablayout);
        mFrameLayout = (FrameLayout) findViewById(R.id.ac_base_framelayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.ac_base_coorlayout);
        mFrameLayout.addView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        findViewId();
        initToolbar(mToolbar);//Toolbar的setTitle方法要在setSupportActionBar(toolbar)之前调用，否则不起作用
        initCenterTitle(mToolbarCenterTitle);
        initTabLayout(mTabLayout);
        initData(savedInstanceState == null ? getIntent().getExtras() : savedInstanceState);
        setSupportActionBar(mToolbar);
        afterSetSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHome());
        }
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideToolBarLayout(boolean hide) {
        int visibility = hide ? View.GONE : View.VISIBLE;
        if (mToolbar != null) {
            mToolbar.setVisibility(visibility);
        }
    }

    protected void addActivityInList(Activity activity) {
        MyApplication.getMyApplication().addActivity(activity);
    }

    protected void showLoading(String str) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(str);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showSnackbar(int resId) {
        showSnackbar(getString(resId));
    }

    public void showSnackbar(String text) {
        SnackbarUtils.showSnackbar(mFrameLayout, text);
    }

    public void showToast(String text) {
        ToastUtils.showToast(this, text);
    }

    public void showToast(@StringRes int resId) {
        ToastUtils.showToast(this, resId);
    }

    public void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRoate = isAllowScreenRoate;
    }

    protected abstract int getLayoutId();

    protected abstract void findViewId();

    protected abstract void initToolbar(Toolbar toolbar);

    protected abstract void initData(Bundle bundle);

    protected void initCenterTitle(TextView centerTitle) {
    }

    protected void afterSetSupportActionBar(Toolbar toolbar) {

    }

    protected void initTabLayout(TabLayout mTabLayout) {
    }

    protected int getBaseLayoutId() {
        return R.layout.activity_base;
    }

    protected boolean isDisplayHome() {
        return true;
    }
}
