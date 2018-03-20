package com.afei.bat.afeiplayandroid.biz;

import android.database.Observable;
import android.widget.Toast;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.api.rxcache.CacheProviders;
import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.HomeList;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class HomeFragmentModel implements IHomeFragmentBannerModel, IHomeFragmentListModel {

    @Override
    public void banner(final IHomeFragmentBannerListener iHomeFragmentBannerListener) {
        HttpManager.getInstance().getHomeBanner().compose(RxSchedulers.<BaseEntity<List<HomeBanner.DataBean>>>compose()).subscribe(new BaseObserver<List<HomeBanner.DataBean>>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(List<HomeBanner.DataBean> beanList) {
                iHomeFragmentBannerListener.netSeccess(beanList);
            }

            @Override
            protected void onFail(String msg) {
                iHomeFragmentBannerListener.netFail(msg);
            }
        });
    }

    @Override
    public void list(int mPage, final IHomeFragmentListListener iHomeFragmentListListener) {
        HttpManager.getInstance().getHomeArticles(mPage).compose(RxSchedulers.<BaseEntity<HomeList.DataBean>>compose()).subscribe(new BaseObserver<HomeList.DataBean>(MyApplication.getContext()) {

            @Override
            protected void onSuccess(HomeList.DataBean dataBean) {
                iHomeFragmentListListener.netSeccess(dataBean);
            }

            @Override
            protected void onFail(String msg) {
                iHomeFragmentListListener.netFail(msg);
            }
        });

    }
}
