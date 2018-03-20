package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.bean.CollectList;

/**
 * Created by MrLiu on 2017/12/26.
 * 登录接口参数3 listener
 */

public interface ICollectListListener {
    void netSeccess(CollectList.DataBean dataBean);

    void netFail(String message);
}
