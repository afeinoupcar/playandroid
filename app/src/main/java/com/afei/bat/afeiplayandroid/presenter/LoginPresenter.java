package com.afei.bat.afeiplayandroid.presenter;

import android.os.Handler;

import com.afei.bat.afeiplayandroid.bean.User;
import com.afei.bat.afeiplayandroid.biz.ILoginListener;
import com.afei.bat.afeiplayandroid.biz.IRegitsterListener;
import com.afei.bat.afeiplayandroid.biz.LoginModel;
import com.afei.bat.afeiplayandroid.biz.RegisterModel;
import com.afei.bat.afeiplayandroid.ui.ILoginView;
import com.ltf.util.LogUtil;


/**
 * Created by MrLiu on 2017/12/26.
 * 5.实现presenter接口的方法
 * view层通过presenter构造方法传递view
 * 拿到view中的数据，new出model实例传入参数，进行网络操作获取数据
 * 通过loginlistener回调得到model中的操作结果
 * 传入view的方法中，刷新ui
 * <p>
 * <p>
 * mvp的整体流程
 * <p>
 * view负责触发事件，把所需参数传递至presenter层
 * presenter层把所需产生参数传递至model层中进行数据处理和网络请求
 * model层通过回调把处理结果返回至presenter层
 * presenter层把结果返回给view层进行UI显示
 * <p>
 * （
 * 可以添加 IBaseView和IPresenter两个基类
 * 在Contract契约类中统一管理UI和业务的方法
 * <参考官方mvpappdemo></>
 * ）
 */

public class LoginPresenter implements ILoginPresenter {
    private ILoginView iLoginView;
    private LoginModel loginModel;
    private RegisterModel registerModel;
    //需在ui线程中刷新ui
    private Handler mHandler = new Handler();

    public LoginPresenter(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        loginModel = new LoginModel();
        registerModel = new RegisterModel();
    }

    @Override
    public void login() {
        iLoginView.showLoading();
        loginModel.login(iLoginView.getLoginName(), iLoginView.getLoginPass(), new ILoginListener() {
            @Override
            public void loginSeccess(final User dataBean) {
                if (dataBean != null) {
                    iLoginView.loginSuccess(dataBean);
                    iLoginView.hideLoading();
                } else {
                    LogUtil.e("presenter", "user is null");
                }
            }

            @Override
            public void loginFail(String message) {
                iLoginView.loginFail(message);
                iLoginView.hideLoading();
            }
        });
    }

    @Override
    public void register() {
        iLoginView.showLoading();
        registerModel.register(iLoginView.getLoginName(), iLoginView.getLoginPass(),iLoginView.getLoginPass(), new IRegitsterListener() {
            @Override
            public void registerSeccess(final User dataBean) {
                if (dataBean != null) {
                    iLoginView.registerSuccess(dataBean);
                    iLoginView.hideLoading();
                } else {
                    LogUtil.e("presenter", "user is null");
                }
            }

            @Override
            public void registerFail(String message) {
                iLoginView.registerFail(message);
                iLoginView.hideLoading();
            }
        });
    }
}
