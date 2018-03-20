package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.bean.HotKey;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/13.
 */

public interface IHotFragmentHotKeyListener {
    void netSeccess(List<HotKey.DataBean> beanList);

    void netFail(String message);
}
