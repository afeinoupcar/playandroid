package com.afei.bat.afeiplayandroid.biz;

/**
 * Created by MrLiu on 2017/12/26.
 * 2. 登录模块model接口和所需参数
 */

public interface IKnowledgeFragmentListModel {
    void list(int page, int cid, IKnowledgeFragmentListListener iKnowledgeFragmentListListener);
}
