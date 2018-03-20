package com.ltf.util.network;

import com.ltf.util.jsondata.BaseJsonData;

import java.util.HashMap;

/**
 * Created by justin on 16/12/3.
 */
public abstract class HttpRequestListener<T extends BaseJsonData> implements RequestListener<T> {

    private boolean isMainThread = true;// 标识是否再主线程中更新ui
    private Object tag;
    private String url;
    private HashMap<String, String> params;
//    private File uploadFile;
//    private String uploadName;
    private HashMap<String, String> uploadFileParams;//uploadName, uploadFileLocalPath
    private String uploadFileType;

    public HttpRequestListener(Object tag) {
        this.tag = tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public void setParamsNoSign(HashMap<String, String> params) {
        this.params = params;
    }

    @Override
    public HashMap<String, String> getParams() {
        return params;
    }

    public HashMap<String, String> getUploadFileParams() {
        return uploadFileParams;
    }

    public void setUploadFileParams(HashMap<String, String> uploadFileParams) {
        this.uploadFileParams = uploadFileParams;
    }

    public String getUploadFileType() {
        return uploadFileType;
    }

    public void setUploadFileType(String uploadFileType) {
        this.uploadFileType = uploadFileType;
    }

    public void setIsMainThread(boolean isMainThread) {
        this.isMainThread = isMainThread;
    }

    public boolean isMainThread() {
        return isMainThread;
    }

    /**
     * 是否忽略对结果的处理，默认是否
     *
     * @return
     */
    public boolean isIgnoreResult() {
        return false;
    }
}