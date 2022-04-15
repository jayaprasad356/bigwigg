package com.example.bigwigg.helper;

import java.util.HashMap;

public class Constant {
    //public static final String MainBaseUrl = "http://bigwigg.greymatterworks.in/";
    //public static final String MainBaseUrl = "http://192.168.43.38/bigwigg/";
    public static final String MainBaseUrl = "https://bigwigg.app/";
    public static final String BaseUrl = MainBaseUrl + "api/";
    public static final String EMAIL_REGISTER_URL = BaseUrl + "login_user.php";
    public static final String UPDATE_PROFILE_URL = BaseUrl + "update_profile.php";
    public static final String UPLOAD_POST_URL = BaseUrl + "post.php";
    public static final String USER_LIST_URL = BaseUrl + "userlists.php";
    public static final String POST_LIST_URL = BaseUrl + "postlists.php";
    public static final String SEARCH_LIST_URL = BaseUrl + "search.php";
    public static final String FAVOURITE_POST_LIST_URL = BaseUrl + "favouritepostlists.php";
    public static final String RATING_URL = BaseUrl + "ratings.php";
    public static final String CHECK_POST_RATING_URL = BaseUrl + "checkpostratings.php";
    public static final String CHECK_POST_FAVOURITE_URL = BaseUrl + "checkpostfavourites.php";
    public static final String SEND_FAVOURITE_URL = BaseUrl + "favourite_posts.php";
    public static final String SEND_COMMENT_URL = BaseUrl + "addcomments.php";
    public static final String LIST_COMMENT_URL = BaseUrl + "listcomments.php";
    public static final String FOLLOW_USER_URL = BaseUrl + "follow_user.php";
    public static final String USER_DETAILS_COUNT_URL = BaseUrl + "user_details_count.php";
    public static final String LIST_NOTIFICATIONS_URL = BaseUrl + "listnotifications.php";
    public static final String NOTIFICATIONS_READ_COUNT_URL = BaseUrl + "notifications_read_count.php";


    public static final String WebSiteUrl = ""; //Admin panel url
    public static final String ID = "id";
    public static final String PROFILE = "profile";
    public static final String USER_ID = "user_id";
    public static final String SEARCH = "search";
    public static final String POST_ID = "post_id";
    public static final String FOLLOW_ID = "follow_id";
    public static final String TOTAL = "total";
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
    public static final String POST_COUNT = "posts_count";
    public static final String FOLLOWERS_COUNT = "followers_count";
    public static final String FOLLOWING_COUNT = "following_count";
    public static final String NOTIFICATIONS_COUNT = "notifications_count";
 public static final String MESSAGE = "message";

    public static final String STATUS = "status";
    public static final String FALSE = "false";
    public static final String TRUE = "true";
}