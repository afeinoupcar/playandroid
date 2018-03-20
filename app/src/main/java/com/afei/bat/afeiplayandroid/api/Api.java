package com.afei.bat.afeiplayandroid.api;

import com.afei.bat.afeiplayandroid.bean.BaseEntity;
import com.afei.bat.afeiplayandroid.bean.CollectList;
import com.afei.bat.afeiplayandroid.bean.HomeBanner;
import com.afei.bat.afeiplayandroid.bean.HomeList;
import com.afei.bat.afeiplayandroid.bean.HotKey;
import com.afei.bat.afeiplayandroid.bean.KnowledgeList;
import com.afei.bat.afeiplayandroid.bean.StudyList;
import com.afei.bat.afeiplayandroid.bean.UseUrl;
import com.afei.bat.afeiplayandroid.bean.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by a on 2017/3/31.
 */

public interface Api {
    /**
     * 登录接口
     *
     * @param loginName
     * @param loginPass
     * @return
     */
    @FormUrlEncoded
    @POST("/user/login")
    Observable<BaseEntity<User>> login(@Field("username") String loginName, @Field("password") String loginPass);

    /**
     * 注册接口
     *
     * @param loginName
     * @param loginPass
     * @return
     */
    @FormUrlEncoded
    @POST("/user/register")
    Observable<BaseEntity<User>> register(@Field("username") String loginName, @Field("password") String loginPass, @Field("repassword") String repassword);

    /**
     * banner接口
     *
     * @return
     */
    @GET("/banner/json")
    Observable<BaseEntity<List<HomeBanner.DataBean>>> getHomeBanner();

    /**
     * 首页数据
     * http://www.wanandroid.com/article/list/0/json
     *
     * @param page page
     */
    @GET("/article/list/{page}/json")
    Observable<BaseEntity<HomeList.DataBean>> getHomeArticles(@Path("page") int page);

    /**
     * 知识体系接口
     *
     * @return
     */
    @GET("/tree/json")
    Observable<BaseEntity<List<StudyList.DataBean>>> getStudyList();

    /**
     * 知识体系二级接口
     * http://www.wanandroid.com/article/list/0/json?cid=168
     *
     * @param page page
     */
    @GET("/article/list/{page}/json")
    Observable<BaseEntity<KnowledgeList.DataBean>> getKnowledgeList(@Path("page") int page, @Query("cid") int cid);

    /**
     * 搜索热词
     *
     * @return
     */
    @GET("/hotkey/json")
    Observable<BaseEntity<List<HotKey.DataBean>>> getHotKey();

    /**
     * 常用网站
     *
     * @return
     */
    @GET("/friend/json")
    Observable<BaseEntity<List<UseUrl.DataBean>>> getUseUrl();

    /**
     * 搜索
     * http://www.wanandroid.com/article/query/0/json
     *
     * @param page page
     * @param k    POST search key
     */
    @POST("/article/query/{page}/json")
    @FormUrlEncoded
    Observable<BaseEntity<HomeList.DataBean>> getSearch(@Path("page") int page, @Field("k") String k);

    /**
     * 收藏文章
     *
     * @param id id
     * @return Deferred<DataResponse>
     */
    @POST("/lg/collect/{id}/json")
    Observable<BaseEntity> addCollectArticle(@Path("id") int id);

    /**
     * 收藏站外文章
     *
     * @param title  title
     * @param author author
     * @param link   link
     * @return Deferred<DataResponse>
     */
    @POST("/lg/collect/add/json")
    @FormUrlEncoded
    Observable<BaseEntity> addCollectOutsideArticle(@Field("title") String title, @Field("author") String author, @Field("link") String link);

    /**
     * 删除收藏文章
     *
     * @param id       id
     * @param originId -1
     * @return Deferred<DataResponse>
     */
    @POST("/lg/uncollect/{id}/json")
    @FormUrlEncoded
    Observable<BaseEntity> removeCollectArticle(@Path("id") int id, @Field("originId") int originId);


    /**
     * 获取自己收藏的文章列表
     *
     * @param page page
     * @return Deferred<Article>
     */
    @GET("/lg/collect/list/{page}/json")
    Observable<BaseEntity<CollectList.DataBean>> getCollectArticles(@Path("page") int page);
}
