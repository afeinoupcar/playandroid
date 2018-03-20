package com.afei.bat.afeiplayandroid.event;

import com.afei.bat.afeiplayandroid.bean.User;

/**
 * Created by lw on 2018/1/25.
 */

public class LoginEvent {
    private User user;

    public LoginEvent(User user) {
        this.user = user;
    }

    public User getLoginUser() {
        return user;
    }

    public void setLoginUser(User user) {
        this.user = user;
    }

}
