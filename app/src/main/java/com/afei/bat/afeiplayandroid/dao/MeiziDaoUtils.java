package com.afei.bat.afeiplayandroid.dao;

import android.content.Context;

import com.afei.bat.afeiplayandroid.bean.People;
import com.ltf.greendao.gen.PeopleDao;
import com.ltf.util.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by MrLiu on 2018/2/26.
 */

public class MeiziDaoUtils {
    private static final String TAG = MeiziDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public MeiziDaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成meizi记录的插入，如果表未创建，先创建Meizi表
     * @param meizi
     * @return
     */
    public boolean insertMeizi(People meizi){
        boolean flag = false;
        flag = mManager.getDaoSession().getPeopleDao().insert(meizi) == -1 ? false : true;
        LogUtil.e(TAG, "insert Meizi :" + flag + "-->" + meizi.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param meiziList
     * @return
     */
    public boolean insertMultMeizi(final List<People> meiziList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (People meizi : meiziList) {
                        mManager.getDaoSession().insertOrReplace(meizi);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param meizi
     * @return
     */
    public boolean updateMeizi(People meizi){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(meizi);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param meizi
     * @return
     */
    public boolean deleteMeizi(People meizi){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(meizi);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAll(){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(People.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<People> queryAllMeizi(){
        return mManager.getDaoSession().loadAll(People.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public People queryMeiziById(long key){
        return mManager.getDaoSession().load(People.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<People> queryMeiziByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(People.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<People> queryMeiziByQueryBuilder(long id){
        QueryBuilder<People> queryBuilder = mManager.getDaoSession().queryBuilder(People.class);
        return queryBuilder.where(PeopleDao.Properties.Id.eq(id)).list();
    }
}
