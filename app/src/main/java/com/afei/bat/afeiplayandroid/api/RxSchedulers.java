package com.afei.bat.afeiplayandroid.api;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MrLiu on 2018/1/8.
 */

public class RxSchedulers {
    private static final String TAG = "RxSchedulers";

    public static <T> ObservableTransformer<T, T> compose() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (!NetworkUtils.isConnected()) {
                                    Log.e(TAG, "accept: 当前无网络");
                                    ToastUtils.setBgColor(Color.RED);
                                    ToastUtils.showShort("当前无网络");
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
