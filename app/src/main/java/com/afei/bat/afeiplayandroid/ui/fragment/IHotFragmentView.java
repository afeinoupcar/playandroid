package com.afei.bat.afeiplayandroid.ui.fragment;

import com.afei.bat.afeiplayandroid.bean.HotKey;
import com.afei.bat.afeiplayandroid.bean.StudyList;
import com.afei.bat.afeiplayandroid.bean.UseUrl;

import java.util.List;

/**
 * Created by MrLiu on 2017/12/26.
 */

public interface IHotFragmentView {


    void hotKeySuccess(List<HotKey.DataBean> beanList);

    void hotKeyFail(String message);

    void useUrlSuccess(List<UseUrl.DataBean> beanList);

    void useUrlFail(String message);
}
