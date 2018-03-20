package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.bean.KnowledgeList;

/**
 * Created by MrLiu on 2017/12/26.
 * 登录接口参数3 listener
 */

public interface IKnowledgeFragmentListListener {
    void netSeccess(KnowledgeList.DataBean dataBean);

    void netFail(String message);
}
