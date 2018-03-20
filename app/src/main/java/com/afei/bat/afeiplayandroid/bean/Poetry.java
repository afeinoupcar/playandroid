package com.afei.bat.afeiplayandroid.bean;

import javax.inject.Inject;

/**
 * Created by MrLiu on 2018/3/7.
 */

public class Poetry {
    private String mPemo;

    // 用Inject标记构造函数,表示用它来注入到目标对象中去
    @Inject
    public Poetry(String poems) {
        mPemo =poems;
    }

    public String getPemo() {
        return mPemo;
    }

}
