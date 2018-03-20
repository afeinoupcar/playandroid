package com.afei.bat.afeiplayandroid.bean;

import javax.inject.Inject;

/**
 * Created by MrLiu on 2018/3/7.
 */

public class Two {
    private One one;

    @Inject
    public Two(One one) {
        this.one = one;
    }

    public String show() {
        return one.info();
    }
}
