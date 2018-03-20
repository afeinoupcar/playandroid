package com.ltf.util.jsondata;

import org.json.JSONObject;

/**
 * Created by justin on 16/12/3.
 * 实现该接口自己去解析
 */
public interface Parseable {

    void parse(JSONObject object);
}
