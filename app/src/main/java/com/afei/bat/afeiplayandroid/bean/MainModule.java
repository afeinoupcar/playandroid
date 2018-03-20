package com.afei.bat.afeiplayandroid.bean;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MrLiu on 2018/3/7.
 */
@Module
public class MainModule {
    @Provides
    public Gson provideGson() {
        return new Gson();
    }
}
