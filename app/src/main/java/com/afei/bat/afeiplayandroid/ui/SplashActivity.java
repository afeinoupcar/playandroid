package com.afei.bat.afeiplayandroid.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.ui.base.BaseActivity;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ltf.util.LogUtil;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    @BindView(R.id.activity_splash_jump_img)
    ImageView activitySplashJumpImg;
    @BindView(R.id.activity_splash_jump_tv)
    TextView activitySplashJumpTv;
    private Timer timer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findViewId() {
        ButterKnife.bind(this);
        hideToolBarLayout(true);
        addActivityInList(this);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
    }

    @Override
    protected void initData(Bundle bundle) {
        loadImg();
        coutDown();
    }

    private void coutDown() {
        final long count = 3;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return count - aLong;
                    }
                })
                .take(4)
                .subscribe(new Observer<Long>() {
                    Disposable dis;

                    @Override
                    public void onSubscribe(Disposable d) {
                        dis = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        activitySplashJumpTv.setText(getString(R.string.jump_advertising) + " " + value);
                        LogUtil.e(TAG, getString(R.string.jump_advertising) + " " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "倒计时结束");
                        dis.dispose();
                        ARouter.getInstance().build("/ui/MainActivity").navigation();
                        finish();
                    }
                });
    }

    private void loadImg() {
        Glide.with(this).
                load("http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg")
                .transition(new DrawableTransitionOptions().crossFade(600))
                .into(activitySplashJumpImg);
    }

    @OnClick(R.id.activity_splash_jump_tv)
    public void onViewClicked() {
        ARouter.getInstance().build("/ui/MainActivity").navigation();
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
