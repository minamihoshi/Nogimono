package org.nogizaka46.contract;


import org.nogizaka46.bean.AvatarSucBean;
import org.nogizaka46.bean.BlogBean;
import org.nogizaka46.bean.CommentBean;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.bean.MemberListBean;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.bean.RegisterSuccessBean;
import org.nogizaka46.bean.UserInfoBean;
import org.nogizaka46.bean.VersionBean;
import org.nogizaka46.config.UrlConfig;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;



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
    Observable<List<MemberListBean>> getMemberBean(@Query("group") String group);

    @GET(UrlConfig.PATH_BLOGS)
    Observable<List<BlogBean>> getBlogBean(@Query("page") int page, @Query("size") int size,@Query("group") String group);

    @GET(UrlConfig.PATH_BLOGS)
    Observable<List<BlogBean>> getBlogBean(@Query("member") String member,@Query("page") int page, @Query("size") int size);


    @POST(UrlConfig.PATH_REGISTER)
    Observable<LzyResponse<RegisterSuccessBean>> UserRegiter(@Query("nickname") String nickname ,@Query("password") String psw);

    @POST(UrlConfig.PATH_LOGIN_PHONE)
    Observable<LzyResponse<RegisterSuccessBean>> UserLoginPhone(@Query("phone") String phone ,@Query("password")String psw);

    @POST(UrlConfig.PATH_LOGIN_EMAIL)
    Observable<LzyResponse<RegisterSuccessBean>> UserLoginEmail(@Query("email") String phone ,@Query("password")String psw);

    @POST(UrlConfig.PATH_LOGIN_NICKNAME)
    Observable<LzyResponse<RegisterSuccessBean>> UserLoginNickname(@Query("nickname") String nickname ,@Query("password")String psw);


    @POST(UrlConfig.PATH_USERINFO)
    Observable<LzyResponse<UserInfoBean>> getUserInfo(@Query("id") String userid , @Query("token") String token);

    @POST(UrlConfig.PATH_USERSET)
    Observable<LzyResponse<String>> UserSet(@Query("id") String userid, @Query("token")String toke
            ,@Query("nickname") String nickname ,@Query("phone") String phone,
            @Query("email")String email , @Query("introduction")String introduction
    );


    @Multipart
    @POST(UrlConfig.PATH_USER_AVATAR)
    Observable<LzyResponse<AvatarSucBean>> uploadAvatar(@Query("uid") int uid , @Query("token") String token ,@Part("photo")MultipartBody.Part part );


    @POST(UrlConfig.PATH_USER_AVATAR)
    Observable<LzyResponse<AvatarSucBean>> uploadAvatar(@Body RequestBody requestBody);
//    RequestBody requestBody = new MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("name", name)
//            .addFormDataPart("psd", psd)
//            .addFormDataPart("file", file.getName(),
//                    RequestBody.create(MediaType.parse("image/*"), file))
//            .build();

    @POST(UrlConfig.PATH_COMMENT_NEW)
    Observable<LzyResponse<String>> sendComment(@Query("fid") String fid ,@Query("uid") String uid ,@Query("token")String token,
    @Query("message") String message ,@Query("father") String father,@Query("touid") String touid
    );

   @POST(UrlConfig.PATH_COMMENT_ALL)
    Observable<LzyResponse<List<CommentBean>>> getAllComment(@Query("fid") String fid);


}
