package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.HomeList;

import java.util.List;

/**
 * Created by MrLiu on 2017/12/26.
 * 登录接口参数3 listener
 */

public interface IHomeFragmentListListener {
    void netSeccess(HomeList.DataBean dataBean);

    void netFail(String message);
}
