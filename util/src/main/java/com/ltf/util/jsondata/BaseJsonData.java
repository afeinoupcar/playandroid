package com.ltf.util.jsondata;

import com.google.gson.annotations.SerializedName;
import com.ltf.util.network.RequestAction;

/**
 * Created by justin on 16/12/3.
 */
public abstract class BaseJsonData<T> {

    @SerializedName(value = "status", alternate = {"code"})
    private String status;//"status": int,返回结果状态。0：正常；其它：错误，具体错误见字段msg,
    private String msg;//"msg": string,返回结果状态对应的字符串,

    private boolean isAddData;  // 是否是增量数据的标志

    private RequestAction action = RequestAction.REQUEST_REFRESH;

    public RequestAction getAction() {
        return action;
    }

    public void setAction(RequestAction action) {
        this.action = action;
    }

    /**
     * 获取数据的渠道类型，网络/缓存
     **/
    public enum DATA_TYPE {
        NETWORK, CACHE
    }

    private DATA_TYPE type = DATA_TYPE.NETWORK;

    public DATA_TYPE getType() {
        return type;
    }

    public void setType(DATA_TYPE type) {
        this.type = type;
    }

    public final boolean isNetWorkData() {
        return type == DATA_TYPE.NETWORK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isAddData() {
        return isAddData;
    }

    public void setIsAddData(boolean isAddData) {
        this.isAddData = isAddData;
    }

    public abstract boolean isSucc();

    //获取uimodel,不需要转换直接返回this
    public abstract T obtainUIModel();

}
