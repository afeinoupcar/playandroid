package com.afei.bat.afeiplayandroid.ui.fragment;

import com.afei.bat.afeiplayandroid.bean.KnowledgeList;
import com.afei.bat.afeiplayandroid.bean.StudyList;

import java.util.List;

/**
 * Created by MrLiu on 2017/12/26.
 */

public interface IKnowledgeFragmentView {


    void listSuccess(KnowledgeList.DataBean dataBean);

    void listFail(String message);

    int getCid();
}
