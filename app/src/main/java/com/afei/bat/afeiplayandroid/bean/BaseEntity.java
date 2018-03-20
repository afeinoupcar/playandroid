package com.afei.bat.afeiplayandroid.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MrLiu on 2018/1/4.
 */

public class BaseEntity<D> {
    @SerializedName("errorCode")
    private String code;
    @SerializedName("errorMsg")
    private String msg;
    @SerializedName("data")
    private D data;

    public Boolean isSuccess() {
        return code.equals("0");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
