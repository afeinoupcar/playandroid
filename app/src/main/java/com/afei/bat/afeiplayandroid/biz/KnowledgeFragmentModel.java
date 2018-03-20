package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.HomeList;
import com.afei.bat.afeiplayandroid.bean.KnowledgeList;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class KnowledgeFragmentModel implements IKnowledgeFragmentListModel {


    @Override
    public void list(int page, int cid, final IKnowledgeFragmentListListener iKnowledgeFragmentListListener) {
        HttpManager.getInstance().getKnowledgeList(page, cid).compose(RxSchedulers.<BaseEntity<KnowledgeList.DataBean>>compose()).subscribe(new BaseObserver<KnowledgeList.DataBean>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(KnowledgeList.DataBean dataBean) {
                iKnowledgeFragmentListListener.netSeccess(dataBean);
            }

            @Override
            protected void onFail(String msg) {
                iKnowledgeFragmentListListener.netFail(msg);
            }
        });
    }
}
