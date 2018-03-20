package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.User;


/**
 * Created by MrLiu on 2017/12/26.
 * 3.实现登录ILooginModel接口
 */

public class RegisterModel implements IRegisterModel {


    @Override
    public void register(String loginName, String loginPass, String repassword, final IRegitsterListener iRegitsterListener) {
        HttpManager.getInstance().register(loginName, loginPass, repassword).compose(RxSchedulers.<BaseEntity<User>>compose()).subscribe(new BaseObserver<User>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(User dataBean) {
                iRegitsterListener.registerSeccess(dataBean);
            }

            @Override
            protected void onFail(String msg) {
                iRegitsterListener.registerFail(msg);
            }
        });
    }
}