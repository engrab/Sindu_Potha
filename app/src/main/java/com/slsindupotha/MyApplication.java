package com.slsindupotha;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import androidx.multidex.MultiDex;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;
import com.util.APICallAync;
import com.util.Settings;

import org.json.JSONObject;


import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

//import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;


public class MyApplication extends Application {

    private static MyApplication mInstance;
    public  int chekc = 0;
    private Settings setting;
    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId("6e013081-af6b-400c-901c-b4a279e9b001");
        OneSignal.setNotificationOpenedHandler(
                new OneSignal.OSNotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenedResult result) {

                        OSNotification notification = result.getNotification();
                        JSONObject data = notification.getAdditionalData();
                        String customKey;
                        if (data != null) {
                            customKey = data.optString("story_id", null);
                            if (customKey != null) {
                                if (!customKey.equals("0")) {
                                    Intent intent = new Intent(MyApplication.this, StoryDetailsActivity.class);
                                    intent.putExtra("Id", customKey);
                                    intent.putExtra("isNotification", true);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(MyApplication.this, SplashActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });

        mInstance = this;
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()

                                .setDefaultFontPath("fonts/malithi.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        //TODO debug
        //AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
        // Initialize the Audience Network SDK
//        AudienceNetworkAds.initialize(this);
    }

    public Settings getSettings() {
        if (setting == null) {
            setting = new Settings(this);
        }
        return setting;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public APICallAync getContactsManager() {
        return APICallAync.getInstance(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



//    private class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationWillShowInForegroundHandler {
//
//
//        @Override
//        public void notificationWillShowInForeground(OSNotificationReceivedEvent notificationReceivedEvent) {
//            OSNotification notification = notificationReceivedEvent.getNotification();
//            JSONObject data = notification.getAdditionalData();
//            String customKey;
//            if (data != null) {
//                customKey = data.optString("story_id", null);
//                if (customKey != null) {
//                    if (!customKey.equals("0")) {
//                        Intent intent = new Intent(MyApplication.this, StoryDetailsActivity.class);
//                        intent.putExtra("Id", customKey);
//                        intent.putExtra("isNotification", true);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(MyApplication.this, SplashActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                }
//            }
//        }
//    }
}