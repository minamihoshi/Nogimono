package org.nogizaka46.contract;


import org.nogizaka46.bean.BlogBean;
import org.nogizaka46.bean.MemberListBean;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.bean.VersionBean;
import org.nogizaka46.config.UrlConfig;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


/**
 * Created by acer on 2016/11/15.
 */

public interface IApiService {


//    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
//    @POST("api/FlyRoute/Add")
//    Call<FlyRouteBean> postFlyRoute(@Body RequestBody route);//传入的参数为RequestBody

//    @Headers({"Content-type:application/json",
//            "Content-Length:59"})*/
//    @POST("FundPaperTrade/AppUserLogin")
//    Observable<Response> getTransData(@Body TestBean str);


//    @POST(UrlConfig.SEARCH_URL)
//    Observable<SearchBean> getSearchReponse(@Body JsonObject jsonObject);





//    @GET("data/{category}")
//    Observable<NewsBean> getNews(@Path("category") String category);

    @GET("/data/list?")
    Observable<List<NewBean>> getNewsBean(@Query("type")  String type , @Query("page") int page, @Query("size") int size);

    @GET("/data/list")
    Observable<List<NewBean>> getNewsBean(@Query("page") int page, @Query("size") int size);

    @GET
    Observable<VersionBean> getVersionCheck(@Url String url);

    @GET
    Observable<ResponseBody> getAPK(@Url String string);

    @GET(UrlConfig.DATA_MEMBERLIST)
    Observable<List<MemberListBean>> getMemberBean();

    @GET(UrlConfig.PATH_BLOGS)
    Observable<List<BlogBean>> getBlogBean(@Query("page") int page, @Query("size") int size);

    @GET(UrlConfig.PATH_BLOGS)
    Observable<List<BlogBean>> getBlogBean(@Query("member") String member,@Query("page") int page, @Query("size") int size);


}
