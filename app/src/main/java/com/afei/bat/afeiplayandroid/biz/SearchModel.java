package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.HomeList;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class SearchModel implements ISearchModel {


    @Override
    public void list(int page, String k, final ISearchListener iSearchListener) {
        HttpManager.getInstance().getSearch(page, k).compose(RxSchedulers.<BaseEntity<HomeList.DataBean>>compose()).subscribe(new BaseObserver<HomeList.DataBean>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(HomeList.DataBean dataBean) {
                iSearchListener.netSeccess(dataBean);
            }

            @Override
            protected void onFail(String msg) {
                iSearchListener.netFail(msg);
            }
        });
    }
}
