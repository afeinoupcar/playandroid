package com.afei.bat.afeiplayandroid.ui.activity;

import com.afei.bat.afeiplayandroid.bean.HomeList;

/**
 * Created by MrLiu on 2017/12/26.
 */

public interface ISearchView {

    void listSuccess(HomeList.DataBean bean);

    void listFail(String message);
}
