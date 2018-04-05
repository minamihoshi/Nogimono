package org.nogizaka46.config;



public class UrlConfig {

   // https://platform.idolx46.top/

    public static  final  String BASE_URL ="https://nogimono.tk/";
    public static  final  String BASE_FORMATWEB ="https://nogimono.tk";


    public static final  String VERSION_CHECK =BASE_URL+"check/version/android";
    public static final String DATA_MEMBERLIST = "/data/memberlist";
    public static final String    PATH_BLOGS = "/data/blogs";

    public static final String PATH_NEWS_ALL = "getall";
    public static final String PATH_NEWS_BLOGS = "getblog";
    public static final String PATH_NEWS_NEWS = "getnews";
    public static final String PATH_NEWS_MAGAZINE = "getmagazine";


    public static final String PATH_REGISTER ="/api/user/new";
    public static final String PATH_LOGIN_PHONE ="/api/user/login/phone";
    public static final String PATH_LOGIN_EMAIL ="/api/user/login/email";
    public static final String  PATH_LOGIN_NICKNAME = "/api/user/login/nickname";
    public static final String PATH_USERINFO ="/api/user/get";
    public static final String  PATH_USERSET ="/api/user/userset";

    public static final String PATH_COMMENT_ALL = "/api/comment/get";
    public static final String PATH_COMMENT_NEW  ="/api/comment/new";
    public static final String PATH_OTHERUSER ="/api/user/getother";

    public static final String PATH_COMMENT_DEL ="/api/comment/delete";
    public static final String PATH_COMMENT_UNREAD ="/api/comment/unread";
    public static final String PATH_COMMENT_READ ="/api/comment/read";
    public static final String PATH_USER_AVATAR = "/api/user/avatar";



}