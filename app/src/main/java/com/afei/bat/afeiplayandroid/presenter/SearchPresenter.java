package com.afei.bat.afeiplayandroid.presenter;

import android.os.Handler;

import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.HomeList;
import com.afei.bat.afeiplayandroid.biz.HomeFragmentModel;
import com.afei.bat.afeiplayandroid.biz.IHomeFragmentBannerListener;
import com.afei.bat.afeiplayandroid.biz.IHomeFragmentListListener;
import com.afei.bat.afeiplayandroid.biz.ISearchListener;
import com.afei.bat.afeiplayandroid.biz.SearchModel;
import com.afei.bat.afeiplayandroid.ui.activity.ISearchView;
import com.afei.bat.afeiplayandroid.ui.fragment.IHomeFragmentView;

import java.util.List;


/**
 * Created by MrLiu on 2017/12/26.
 * 5.实现presenter接口的方法
 * view层通过presenter构造方法传递view
 * 拿到view中的数据，new出model实例传入参数，进行网络操作获取数据
 * 通过loginlistener回调得到model中的操作结果
 * 传入view的方法中，刷新ui
 * <p>
 * <p>
 * mvp的整体流程
 * <p>
 * view负责触发事件，把所需参数传递至presenter层
 * presenter层把所需产生参数传递至model层中进行数据处理和网络请求
 * model层通过回调把处理结果返回至presenter层
 * presenter层把结果返回给view层进行UI显示
 * <p>
 * （
 * 可以添加 IBaseView和IPresenter两个基类
 * 在Contract契约类中统一管理UI和业务的方法
 * <参考官方mvpappdemo></>
 * ）
 */

public class SearchPresenter implements ISearchPresenter {
    private ISearchView iSearchView;
    private SearchModel searchModel;
    private int mPage;
    private boolean mIsRefresh;
    private String k = "";
    //需在ui线程中刷新ui
    private Handler mHandler = new Handler();

    public SearchPresenter(ISearchView iSearchView) {
        this.iSearchView = iSearchView;
        searchModel = new SearchModel();
    }

    @Override
    public void list(String k) {
        if (!this.k.equals(k)) {
            mPage = 0;
            this.k = k;
        }
        searchModel.list(mPage, k, new ISearchListener() {
            @Override
            public void netSeccess(HomeList.DataBean dataBean) {
                iSearchView.listSuccess(dataBean);
            }

            @Override
            public void netFail(String message) {
                iSearchView.listFail(message);
            }
        });
    }

    @Override
    public void refresh() {
        mPage = 0;
        list(k);
    }

    @Override
    public void load() {
        mPage++;
        list(k);
    }
}
