package com.afei.bat.afeiplayandroid;

import android.app.Activity;
import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.ltf.util.BaseApplication;
import com.ltf.util.PackageUtils;
import com.ltf.util.SharePreferenceUtil;

import java.util.ArrayList;

/**
 * Created by MrLiu on 2017/12/15.
 */

public class MyApplication extends BaseApplication {

    private ArrayList<Activity> activityList = new ArrayList<>();
    private static MyApplication myApplication;
    private int mAppVersionCode;//app版本号int
    private String mAppVersionName;//app版本号
    private SharePreferenceUtil mAppSharePreference;

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    public int getAppVersionCode() {
        return mAppVersionCode;
    }

    public String getAppVersionName() {
        return mAppVersionName;
    }

    public SharePreferenceUtil getAppSharePreference() {
        return mAppSharePreference;
    }

    @Override
    public void onCreate() {
        myApplication = this;
        super.onCreate();
        mAppSharePreference = new SharePreferenceUtil(this, "app_info");
        mAppVersionCode = PackageUtils.getVersionCode(this);
        mAppVersionName = PackageUtils.getVersionName(this);
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        Utils.init(this);

    }

    /**
     * 添加到ArrayList<Activity>
     *
     * @param activity：Activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 退出所有的Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
