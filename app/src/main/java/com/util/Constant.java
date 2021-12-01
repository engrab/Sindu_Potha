package com.util;



import com.slsindupotha.BuildConfig;

import java.io.Serializable;

public class Constant implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private static String SERVER_URL = BuildConfig.SERVER_URL;

    public static final String API_URL = SERVER_URL + "api.php";

    public static final String IMAGE_PATH = SERVER_URL + "images/";

    public static final String ARRAY_NAME = "ONLINE_STORY_APP";

    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_CID = "cid";
    public static final String CATEGORY_IMAGE = "category_image";

    public static final String STORY_ID = "id";
    public static final String STORY_TITLE = "story_title";
    public static final String STORY_DATE = "story_date";
    public static final String STORY_VIEW = "story_views";
    public static final String STORY_DESC = "story_description";
    public static final String STORY_IMAGE = "story_image";

    public static final String SLIDER_ID = "id";
    public static final String SLIDER_IMAGE = "slide_img";
    public static final String SLIDER_LINK = "slide_url";

    public static final String APP_NAME = "app_name";
    public static final String APP_IMAGE = "app_logo";
    public static final String APP_VERSION = "app_version";
    public static final String APP_AUTHOR = "app_author";
    public static final String APP_CONTACT = "app_contact";
    public static final String APP_EMAIL = "app_email";
    public static final String APP_WEBSITE = "app_website";
    public static final String APP_DESC = "app_description";
    public static final String APP_WELCOME = "app_welcome_msg";
    public static final String APP_PRIVACY_POLICY = "app_privacy_policy";
    public static final String APP_PACKAGE_NAME = "package_name";

    public static int AD_COUNT = 0;
    public static int AD_COUNT_SHOW ;

    public static boolean isBanner = false, isInterstitial = false;
    public static String adMobBannerId, adMobInterstitialId, adMobPublisherId;

}
