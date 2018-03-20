package com.afei.bat.afeiplayandroid.biz;

/**
 * Created by MrLiu on 2017/12/26.
 *2. 登录模块model接口和所需参数
 */

public interface ILoginModel {
    //登录肯定要传 loginNAme/loginPass/listener
    void login(String loginName, String loginPass, ILoginListener iLoginListener);
}
