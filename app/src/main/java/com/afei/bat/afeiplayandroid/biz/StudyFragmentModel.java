package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.StudyList;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/12.
 */

public class StudyFragmentModel implements IStudyFragmentModel {

    @Override
    public void list(final IStudyFragmentListener iStudyFragmentListener) {
        HttpManager.getInstance().getStudyList().compose(RxSchedulers.<BaseEntity<List<StudyList.DataBean>>>compose()).subscribe(new BaseObserver<List<StudyList.DataBean>>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(List<StudyList.DataBean> beanList) {
                iStudyFragmentListener.netSeccess(beanList);
            }

            @Override
            protected void onFail(String msg) {
                iStudyFragmentListener.netFail(msg);
            }
        });
    }
}
