package com.afei.bat.afeiplayandroid.ui;

import com.afei.bat.afeiplayandroid.bean.User;

/**
 * Created by MrLiu on 2017/12/26.
 */

public interface ILoginView {
    String getLoginName();

    String getLoginPass();

    void loginSuccess(User dataBean);

    void loginFail(String message);

    void registerSuccess(User dataBean);

    void registerFail(String message);

    void showLoading();

    void hideLoading();
}
