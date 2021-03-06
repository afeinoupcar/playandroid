package com.afei.bat.afeiplayandroid.presenter;

import android.os.Handler;

import com.afei.bat.afeiplayandroid.bean.CollectList;
import com.afei.bat.afeiplayandroid.bean.KnowledgeList;
import com.afei.bat.afeiplayandroid.biz.ICollectListListener;
import com.afei.bat.afeiplayandroid.biz.IKnowledgeFragmentListListener;
import com.afei.bat.afeiplayandroid.biz.ILikeActivityModel;
import com.afei.bat.afeiplayandroid.biz.KnowledgeFragmentModel;
import com.afei.bat.afeiplayandroid.ui.activity.IILikeActivityView;
import com.afei.bat.afeiplayandroid.ui.fragment.IKnowledgeFragmentView;


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

public class ILikeActivityPresenter implements IILikeActivityPresenter {
    private IILikeActivityView iiLikeActivityView;
    private ILikeActivityModel iLikeActivityModel;
    private int mPage;
    private boolean mIsRefresh;
    //需在ui线程中刷新ui
    private Handler mHandler = new Handler();

    public ILikeActivityPresenter(IILikeActivityView iiLikeActivityView) {
        this.iiLikeActivityView = iiLikeActivityView;
        iLikeActivityModel = new ILikeActivityModel();
    }

    @Override
    public void list() {
        iLikeActivityModel.list(mPage, new ICollectListListener() {
            @Override
            public void netSeccess(CollectList.DataBean dataBean) {
                iiLikeActivityView.listSuccess(dataBean);
            }

            @Override
            public void netFail(String message) {
                iiLikeActivityView.listFail(message);
            }
        });
    }

    @Override
    public void refresh() {
        mPage = 0;
        list();
    }

    @Override
    public void load() {
        mPage++;
        list();
    }
}
