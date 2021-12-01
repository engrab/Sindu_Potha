package com.slsindupotha;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends AppCompatActivity {

    protected MyApplication myApplication;
//    public InterstitialAd interstitialAd;
    public Handler customHandler;
    public Boolean isRunning = true;
    private final String TAG = "check";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getInstance();
//        interstitialAd = new InterstitialAd(this, "724621651433439_728401104388827");
        customHandler = new Handler();

    }



    public MyApplication getApp() {
        if (myApplication == null) {
            myApplication = (MyApplication) getApplication();
        }
        return myApplication;
    }

    class Task implements Runnable {
        @Override
        public void run() {
            if (isRunning) {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        interAD();

                    }
                });
                // Do really cool stuff
                // and even cooler stuff
            } else {
                // it is paused
                Log.i("hello", "paused");
            }
            customHandler.postDelayed(this, 90000);
        }
    }

    public void interAD() {
//        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//                // Interstitial ad displayed callback
//                Log.e(TAG, "Interstitial ad displayed.");
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                // Interstitial dismissed callback
//                Log.e(TAG, "Interstitial ad dismissed.");
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Ad error callback
//                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Interstitial ad is loaded and ready to be displayed
//                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//                // Show the ad
//                interstitialAd.show();
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//                Log.d(TAG, "Interstitial ad clicked!");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Ad impression logged callback
//                Log.d(TAG, "Interstitial ad impression logged!");
//            }
//        };
//
//        // For auto play video ads, it's recommended to load the ad
//        // at least 30 seconds before it is shown
//        interstitialAd.loadAd(
//                interstitialAd.buildLoadAdConfig()
//                        .withAdListener(interstitialAdListener)
//                        .build());

    }

}
