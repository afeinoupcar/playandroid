package com.afei.bat.afeiplayandroid.ui;

import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.api.CookiesManager;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.event.LoginEvent;
import com.afei.bat.afeiplayandroid.event.RefreshEvent;
import com.afei.bat.afeiplayandroid.ui.base.BaseActivity;
import com.afei.bat.afeiplayandroid.ui.fragment.HomeFragment;
import com.afei.bat.afeiplayandroid.ui.fragment.HotFragment;
import com.afei.bat.afeiplayandroid.ui.fragment.StudyFragment;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ltf.util.ui.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/MainActivity")
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.activity_navigation)
    BottomNavigationView activityNavigation;
    @BindView(R.id.activity_main)
    DrawerLayout activityMain;
    @BindView(R.id.activity_left_login)
    TextView activityLeftLogin;
    @BindView(R.id.activity_left_login_out)
    TextView activityLeftLoginOut;
    @BindView(R.id.activity_left_i_like)
    TextView activityLeftILike;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private int mLastFgIndex;
    private TextView setTitle;
    private boolean mDrawerLayoutState = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViewId() {
        ButterKnife.bind(this);
        addActivityInList(this);
        setUserLoginState();
        EventBus.getDefault().register(this);
        activityNavigation.setOnNavigationItemSelectedListener(this);
        activityMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                mDrawerLayoutState = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                mDrawerLayoutState = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setUserLoginState() {
        boolean isLogin = MyApplication.getMyApplication().getAppSharePreference().getBooleanValue(Constant.LOGIN_TAG, false);
        if (isLogin) {
            String loginName = MyApplication.getMyApplication().getAppSharePreference().getStringValue(Constant.LOGIN_NAME);
            activityLeftLogin.setText(loginName);
            activityLeftLoginOut.setVisibility(View.VISIBLE);
        } else {
            activityLeftLogin.setText("点击登录");
            activityLeftLoginOut.setVisibility(View.GONE);
        }
    }


    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.toolbar_icon);
    }

    @Override
    protected void initData(Bundle bundle) {
        initFragment();
        switchFragment(0);
    }

    private void initFragment() {
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(StudyFragment.newInstance());
        fragmentList.add(HotFragment.newInstance());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                switchFragment(0);
                break;
            case R.id.navigation_study:
                switchFragment(1);
                break;
        }
        return true;
    }

    @Override
    protected void initCenterTitle(TextView centerTitle) {
        setTitle = centerTitle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayoutState) {
                activityMain.closeDrawer(GravityCompat.START);
                mDrawerLayoutState = false;
            } else {
                activityMain.openDrawer(GravityCompat.START);
                mDrawerLayoutState = true;
            }
            return true;
        } else if (item.getItemId() == R.id.menuHot) {
            switchFragment(2);
        } else if (item.getItemId() == R.id.menuSearch) {
            ARouter.getInstance().build("/activity/SearchActivity").navigation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(int position) {
        if (position >= fragmentList.size()) {
            return;
        }
        if (position == 0) {
            setTitle.setText(getString(R.string.navigation_home));
        } else {
            setTitle.setText(getString(R.string.navigation_study));
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = fragmentList.get(position);
        Fragment lastFg = fragmentList.get(mLastFgIndex);
        mLastFgIndex = position;
        ft.hide(lastFg);
        if (!targetFg.isAdded())
            ft.add(R.id.activity_framelayout, targetFg);
        ft.show(targetFg);
        ft.commitAllowingStateLoss();
    }

    /**
     * 两次返回退出应用间隔时间
     */
    private static final long DIFF_DEFAULT_BACK_TIME = 2000;
    /**
     * 上次返回时间
     */
    private long mBackTime = -1;

    @Override
    public void onBackPressed() {
        // 连续点击返回退出
        long nowTime = SystemClock.elapsedRealtime();
        long diff = nowTime - mBackTime;
        if (diff >= DIFF_DEFAULT_BACK_TIME) {
            mBackTime = nowTime;
            showSnackbar(R.string.toast_exit_app_str);
        } else {
            MyApplication.getMyApplication().finishAllActivity();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent loginEvent) {
        setUserLoginState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.activity_left_login, R.id.activity_left_login_out, R.id.activity_left_i_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_left_login:
                ARouter.getInstance().build("/ui/LoginActivity").navigation();
                break;
            case R.id.activity_left_login_out:
                MyApplication.getMyApplication().getAppSharePreference().clear();
                CookiesManager.clearAllCookies();
                EventBus.getDefault().post(new RefreshEvent());
                setUserLoginState();
                break;
            case R.id.activity_left_i_like:
                ARouter.getInstance().build("/activity/ILikeActivity").navigation();
                break;
        }
    }
}
