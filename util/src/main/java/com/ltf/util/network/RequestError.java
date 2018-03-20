package com.ltf.util.network;

import com.ltf.util.jsondata.BaseJsonData;

/**
 * Created by justin on 16/12/3.
 */
public class RequestError<T extends BaseJsonData> {
    public static final int CODE_NO_NETWORK = -21000;// 没有网络
    public static final int CODE_ERROR = -21001;// 其他情况
    public static final int CODE_CANCELED = -21002;// 取消
    public static final int CODE_ERROR_200 = 200;// 解析出错

    /**
     * 默认的请求类型，刷新/加载
     **/
    private RequestAction action = RequestAction.REQUEST_REFRESH;

    private String error;
    private String url;
    private int code;//response的code
    private String dataCode;
    private String tag; //用于回传标识符，比如article_id之类的

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public RequestAction getAction() {
        return action;
    }

    public void setAction(RequestAction action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "RequestError{" +
                "action=" + action +
                ", error='" + error + '\'' +
                ", url='" + url + '\'' +
                ", code=" + code +
                ", dataCode='" + dataCode + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
