package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class AddRemoveModel {
    public static void addAndRemove(int id, boolean flag) {
        if (flag) {
            HttpManager.getInstance().removeCollectArticle(id, -1).compose(RxSchedulers.<BaseEntity>compose()).subscribe(new Observer<BaseEntity>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(BaseEntity baseEntity) {
                    ToastUtils.showShort("已取消收藏");
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.showShort(e.toString());
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            HttpManager.getInstance().addCollectArticle(id).compose(RxSchedulers.<BaseEntity>compose()).subscribe(new Observer<BaseEntity>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(BaseEntity baseEntity) {
                    ToastUtils.showShort("收藏成功");
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.showShort(e.toString());
                }

                @Override
                public void onComplete() {

                }
            });
        }


    }
}
