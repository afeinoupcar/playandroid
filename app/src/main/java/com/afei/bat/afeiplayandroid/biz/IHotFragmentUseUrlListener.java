package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.bean.UseUrl;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/13.
 */

public interface IHotFragmentUseUrlListener {
    void netSeccess(List<UseUrl.DataBean> beanList);

    void netFail(String message);
}
