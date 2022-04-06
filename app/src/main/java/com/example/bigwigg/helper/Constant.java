package com.example.bigwigg.helper;

import java.util.HashMap;

public class Constant {
    //public static final String MainBaseUrl = "http://bigwigg.greymatterworks.in/";
    public static final String MainBaseUrl = "http://192.168.43.38/bigwigg/";
    public static final String BaseUrl = MainBaseUrl + "api/";
    public static final String EMAIL_REGISTER_URL = BaseUrl + "login_user.php";
    public static final String UPDATE_PROFILE_URL = BaseUrl + "update_profile.php";
    public static final String UPLOAD_POST_URL = BaseUrl + "post.php";
    public static final String USER_LIST_URL = BaseUrl + "userlists.php";
    public static final String POST_LIST_URL = BaseUrl + "postlists.php";
    public static final String RATING_URL = BaseUrl + "ratings.php";
    public static final String CHECK_POST_RATING_URL = BaseUrl + "checkpostratings.php";

    public static final String WebSiteUrl = ""; //Admin panel url
    public static final String ID = "id";
    public static final String PROFILE = "profile";
    public static final String USER_ID = "user_id";
    public static final String POST_ID = "post_id";
    public static final String IMAGE = "image";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String CAPTION = "caption";
    public static final String DESCRIPION = "description";
    public static final String ROLE = "role";
    public static final String DATA = "data";
    public static final String UPLOAD_PROFILE = "upload_profile";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AccessKey = "accesskey";
    public static final String AccessKeyVal = "90336";
    //set your jwt secret key here...key must same in PHP and Android
    public static final String JWT_KEY = "big_wigg";
    public static final String SUCCESS = "success";
 public static final String MESSAGE = "message";

    public static final String STATUS = "status";
    public static final String FALSE = "false";
    public static final String TRUE = "true";
}