package com.afei.bat.afeiplayandroid.api;

import android.content.Context;

import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.ltf.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by MrLiu on 2018/1/8.
 */

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {
    private static final String TAG = "BaseObserver";
    private Context context;

    public BaseObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseEntity<T> value) {
        if (value.isSuccess()) {
            T t = value.getData();
            onSuccess(t);
        } else {
            onFail(value.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        onFail(e.toString());
    }

    @Override
    public void onComplete() {
        LogUtil.d(TAG, "onComplete");
    }

    protected abstract void onSuccess(T t);

    protected abstract void onFail(String msg);

}
