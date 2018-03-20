package com.afei.bat.afeiplayandroid.bean;

import com.afei.bat.afeiplayandroid.ui.MainActivity;

import dagger.Component;

/**
 * Created by MrLiu on 2018/3/7.
 */
@Component(modules = {MainModule.class, PoetryModule.class})
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
