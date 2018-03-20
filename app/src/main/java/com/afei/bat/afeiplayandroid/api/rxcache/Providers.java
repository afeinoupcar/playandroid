package com.afei.bat.afeiplayandroid.api.rxcache;

import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.HomeList;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.LifeCache;
import io.rx_cache2.ProviderKey;

/**
 * Created by MrLiu on 2018/3/16.
 * 未使用
 */

public interface Providers {
    @ProviderKey("mocks-1-minute-ttl")
    @LifeCache(duration = 1, timeUnit = TimeUnit.MINUTES)
        //缓存有效期5分钟
    Observable<BaseEntity<HomeList.DataBean>> getCacheHomeList(Observable<BaseEntity<HomeList.DataBean>> homelist);

}
