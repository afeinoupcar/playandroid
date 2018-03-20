package com.afei.bat.afeiplayandroid.ui.fragment;

import com.afei.bat.afeiplayandroid.bean.StudyList;

import java.util.List;

/**
 * Created by MrLiu on 2017/12/26.
 */

public interface IStudyFragmentView {


    void listSuccess(List<StudyList.DataBean> beanList);

    void listFail(String message);
}
