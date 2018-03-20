package com.afei.bat.afeiplayandroid.api.rxcache;

import com.afei.bat.afeiplayandroid.MyApplication;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by MrLiu on 2018/3/16.
 * 未使用
 */

public class CacheProviders {
    private static Providers providers;

    public synchronized static Providers getCacheHomeList() {
        if (providers == null) {
            providers = new RxCache.Builder()
                    .persistence(MyApplication.getMyApplication().getCacheDir(), new GsonSpeaker())//缓存文件的配置、数据的解析配置
                    .using(Providers.class);//这些配置对应的缓存接口
        }
        return providers;
    }
}
