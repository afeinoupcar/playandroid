package com.afei.bat.afeiplayandroid.ui.activity;

import com.afei.bat.afeiplayandroid.bean.CollectList;

/**
 * Created by MrLiu on 2017/12/26.
 */

public interface IILikeActivityView {


    void listSuccess(CollectList.DataBean dataBean);

    void listFail(String message);

}
