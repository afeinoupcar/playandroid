package com.afei.bat.afeiplayandroid.biz;

import com.afei.bat.afeiplayandroid.MyApplication;
import com.afei.bat.afeiplayandroid.api.BaseObserver;
import com.afei.bat.afeiplayandroid.api.HttpManager;
import com.afei.bat.afeiplayandroid.api.RxSchedulers;
import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.HotKey;
import com.afei.bat.afeiplayandroid.bean.UseUrl;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/13.
 */

public class HotFragmentModel implements IHotFragmentHotKeyModel, IHotFragmentUseUrlModel {
    @Override
    public void hotKey(final IHotFragmentHotKeyListener iHotFragmentHotKeyListener) {
        HttpManager.getInstance().getHotKey().compose(RxSchedulers.<BaseEntity<List<HotKey.DataBean>>>compose()).subscribe(new BaseObserver<List<HotKey.DataBean>>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(List<HotKey.DataBean> dataBeans) {
                iHotFragmentHotKeyListener.netSeccess(dataBeans);
            }

            @Override
            protected void onFail(String msg) {
                iHotFragmentHotKeyListener.netFail(msg);
            }
        });
    }

    @Override
    public void useUrl(final IHotFragmentUseUrlListener iHotFragmentUseUrlListener) {
        HttpManager.getInstance().getUseUrl().compose(RxSchedulers.<BaseEntity<List<UseUrl.DataBean>>>compose()).subscribe(new BaseObserver<List<UseUrl.DataBean>>(MyApplication.getContext()) {
            @Override
            protected void onSuccess(List<UseUrl.DataBean> dataBeans) {
                iHotFragmentUseUrlListener.netSeccess(dataBeans);
            }

            @Override
            protected void onFail(String msg) {
                iHotFragmentUseUrlListener.netFail(msg);
            }
        });
    }
}
