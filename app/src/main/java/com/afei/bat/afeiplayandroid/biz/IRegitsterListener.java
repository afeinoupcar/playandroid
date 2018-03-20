package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.bean.User;

/**
 * Created by MrLiu on 2017/12/26.
 * 登录接口参数3 listener
 */

public interface IRegitsterListener {
    void registerSeccess(User dataBean);

    void registerFail(String message);
}
