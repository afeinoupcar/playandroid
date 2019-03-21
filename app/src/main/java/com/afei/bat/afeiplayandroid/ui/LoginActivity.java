package com.afei.bat.afeiplayandroid.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.User;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.event.LoginEvent;
import com.afei.bat.afeiplayandroid.event.RefreshEvent;
import com.afei.bat.afeiplayandroid.presenter.LoginPresenter;
import com.afei.bat.afeiplayandroid.ui.base.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/LoginActivity")
public class LoginActivity extends BaseActivity implements ILoginView {


    @BindView(R.id.activity_login_user_name)
    TextInputEditText activityLoginUserName;
    @BindView(R.id.activity_login_name_layout)
    TextInputLayout activityLoginNameLayout;
    @BindView(R.id.activity_login_user_pass)
    TextInputEditText activityLoginUserPass;
    @BindView(R.id.activity_login_pass_layout)
    TextInputLayout activityLoginPassLayout;
    @BindView(R.id.activity_login_btn)
    Button activityLoginBtn;
    @BindView(R.id.activity_login_register)
    Button activityLoginRegister;
    @BindView(R.id.activity_login_pb_bar)
    ProgressBar activityLoginPbBar;
    /**
     * 得到presenter的实例，传入参数
     */
    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @Override
    protected void findViewId() {
        ButterKnife.bind(this);
        addActivityInList(this);
        textChangedListener();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
    }

    @Override
    protected void initCenterTitle(TextView centerTitle) {
        centerTitle.setText("注册登录");
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    public String getLoginName() {
        return activityLoginUserName.getText().toString().trim();
    }

    @Override
    public String getLoginPass() {
        return activityLoginUserPass.getText().toString().trim();
    }

    @Override
    public void loginSuccess(User dataBean) {
        showToast("登录成功");
        MyApplication.getMyApplication().getAppSharePreference().writeBooleanValue(Constant.LOGIN_TAG, true);
        MyApplication.getMyApplication().getAppSharePreference().writeStringValue(Constant.LOGIN_NAME, dataBean.getUsername());
        EventBus.getDefault().post(new LoginEvent(dataBean));
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void loginFail(String message) {
        showSnackbar(message);
    }

    @Override
    public void registerSuccess(User dataBean) {
        showToast("注册成功");
    }

    @Override
    public void registerFail(String message) {
        showSnackbar(message);
    }

    @Override
    public void showLoading() {
        activityLoginPbBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        activityLoginPbBar.setVisibility(View.INVISIBLE);
    }


    @OnClick({R.id.activity_login_btn, R.id.activity_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_login_btn:
                if (TextUtils.isEmpty(getLoginName())) {
                    activityLoginNameLayout.setError("用户名为空");
                    return;
                }
                if (TextUtils.isEmpty(getLoginPass())) {
                    activityLoginPassLayout.setError("密码为空");
                    return;
                }
                loginPresenter.login();
                break;
            case R.id.activity_login_register:
                if (TextUtils.isEmpty(getLoginName())) {
                    activityLoginNameLayout.setError("用户名为空");
                    return;
                }
                if (TextUtils.isEmpty(getLoginPass())) {
                    activityLoginPassLayout.setError("密码为空");
                    return;
                }
                loginPresenter.register();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void textChangedListener() {
        activityLoginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(getLoginName())) {
                    activityLoginNameLayout.setErrorEnabled(false);
                }
            }
        });
        activityLoginUserPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(getLoginPass())) {
                    activityLoginPassLayout.setErrorEnabled(false);
                }
            }
        });
    }

}
