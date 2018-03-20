package com.ltf.util.network;


import com.ltf.util.jsondata.BaseJsonData;

import java.util.HashMap;

/**
 * Created by justin on 16/12/3.
 */
public abstract class HttpRequest {

    /**
     * 构造get请求必选参数
     * @return
     */
    public final HashMap<String, String> preBuildParams() {
        HashMap<String, String> map = new HashMap<String, String>();
        buildParams(map);
        return map;
    }

    protected final <T extends BaseJsonData> void refreshGet(HttpRequestListener<T> listener) {
        OkHttpUtils.getAsync(RequestAction.REQUEST_REFRESH, listener);
    }

    protected final <T extends BaseJsonData> void refreshPost(HttpRequestListener<T> listener) {
        OkHttpUtils.postAsync(RequestAction.REQUEST_REFRESH, listener);
    }

    protected final <T extends BaseJsonData> void loadMoreGet(HttpRequestListener<T> listener) {
        OkHttpUtils.getAsync(RequestAction.REQUEST_LOADMORE, listener);
    }

    protected final <T extends BaseJsonData> void loadMorePost(HttpRequestListener<T> listener) {
        OkHttpUtils.postAsync(RequestAction.REQUEST_LOADMORE, listener);
    }

    protected final <T extends BaseJsonData> void formPost(HttpRequestListener<T> listener) {
        OkHttpUtils.postFormAsync(RequestAction.REQUEST_REFRESH, listener);
    }

    /**
     * 请求
     * @param listener 请求回调
     * @param tag   用于发起请求或者取消请求的标志tag
     * @param <T>
     */
    public <T extends BaseJsonData> void request(HttpRequestListener<T> listener, Object tag) {
        HashMap<String, String> params = preBuildParams();
        listener.setTag(tag);
        listener.setParams(params);
        listener.setUrl(getUrl());
        refreshGet(listener);
    }

    protected abstract String getUrl();
    protected abstract void buildParams(HashMap<String, String> params);
}
