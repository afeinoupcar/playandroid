package com.afei.bat.afeiplayandroid.ui.fragment;

import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.HomeList;

import java.util.List;

/**
 * Created by MrLiu on 2017/12/26.
 */

public interface IHomeFragmentView {

    void bannerSuccess(List<HomeBanner.DataBean> beanList);

    void bannerFail(String message);

    void listSuccess(HomeList.DataBean bean);

    void listFail(String message);
}
