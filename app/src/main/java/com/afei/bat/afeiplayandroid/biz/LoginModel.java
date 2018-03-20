package com.afei.bat.afeiplayandroid.biz;

import android.text.TextUtils;
import android.util.Log;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.User;
import com.ltf.util.LogUtil;


/**
 * Created by MrLiu on 2017/12/26.
 * 3.实现登录ILooginModel接口
 */

public class LoginModel implements ILoginModel {

    @Override
    public void login(String loginName, String loginPass, final ILoginListener iLoginListener) {
            HttpManager.getInstance().login(loginName, loginPass).compose(RxSchedulers.<BaseEntity<User>>compose()).subscribe(new BaseObserver<User>(MyApplication.getContext()) {
                @Override
                protected void onSuccess(User dataBean) {
                    iLoginListener.loginSeccess(dataBean);
                }

                @Override
                protected void onFail(String msg) {
                    iLoginListener.loginFail(msg);
                }
            });
    }
}