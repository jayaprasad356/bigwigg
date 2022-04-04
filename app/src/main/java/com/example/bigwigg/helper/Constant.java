package com.example.bigwigg.helper;

import java.util.HashMap;

public class Constant {
   public static final String MainBaseUrl = "http://bigwigg.greymatterworks.in/";
    public static final String BaseUrl = MainBaseUrl + "api/";
    public static final String EMAIL_REGISTER_URL = BaseUrl + "login_user.php";
    public static final String UPDATE_PROFILE_URL = BaseUrl + "update_profile.php";
    public static final String USER_LIST_URL = BaseUrl + "userlists.php";

    public static final String WebSiteUrl = ""; //Admin panel url
    public static final String ID = "id";
    public static final String PROFILE = "profile";
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String TYPE = "type";
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

}