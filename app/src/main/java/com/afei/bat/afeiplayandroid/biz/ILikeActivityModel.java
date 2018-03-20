package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.CollectList;
import com.afei.bat.afeiplayandroid.bean.KnowledgeList;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class ILikeActivityModel implements ICollectListModel {

    @Override
    public void list(int page, final ICollectListListener iCollectListListener) {
        HttpManager.getInstance().getCollectArticles(page).compose(RxSchedulers.<BaseEntity<CollectList.DataBean>>compose()).subscribe(new BaseObserver<CollectList.DataBean>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(CollectList.DataBean dataBean) {
                iCollectListListener.netSeccess(dataBean);
            }

            @Override
            protected void onFail(String msg) {
                iCollectListListener.netFail(msg);
            }
        });
    }
}
