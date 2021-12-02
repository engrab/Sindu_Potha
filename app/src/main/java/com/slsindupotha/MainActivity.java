package com.slsindupotha;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fragment.CategoryFragment;
import com.fragment.FavouriteFragment;
import com.fragment.HomeFragment;
import com.fragment.LatestFragment;
import com.tapdaq.sdk.adnetworks.TMMediationNetworks;
import com.util.API;
//import com.util.BannerAds;
import com.util.Constant;
import com.util.JsonUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ixidev.gdpr.GDPRChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

//import com.util.SinduPothaAppUpdate;

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.listeners.*;
import com.util.TapdaqAdsUtils;

public class MainActivity extends BaseActivity {
    private static MainActivity instance;
    private DrawerLayout drawerLayout;
    public FragmentManager fragmentManager;
//    private AdView adView;
    public Activity s;
    Toolbar toolbar;
    NavigationView navigationView;
    final Handler handler = new Handler();
    int mode;
    TMBannerAdView adView;

    //    private AppUpdateManager appUpdateManager;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    static final int REQ_CODE_VERSION_UPDATE = 530;
    InAppUpdateManager inAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TapdaqConfig config = Tapdaq.getInstance().config();

        config.setUserSubjectToGdprStatus(STATUS.TRUE); //GDPR declare if user is in EU
        config.setConsentStatus(STATUS.TRUE); //GDPR consent must be obtained from the user
        config.setAgeRestrictedUserStatus(STATUS.FALSE); //Is user subject to COPPA or GDPR age restrictions

        Tapdaq.getInstance().initialize(this, "61a896f208fe6c2d735d7485", "01286d5d-854d-48d7-a1cc-ed9ea5aa8a1c", config, new TapdaqInitListener());
//        config.registerTestDevices(TMMediationNetworks.FACEBOOK, Arrays.asList("65866e73-b1dd-480a-bf2e-027529390889"));
//        Tapdaq.getInstance().startTestActivity(MainActivity.this);
//        AudienceNetworkAds.initialize(this);
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.scheduleAtFixedRate(new Runnable() {
//            public void run() {
//                Log.i("hello", "world");
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                     interAD();
//
//                    }
//                });
//            }
//        }, 10, 30, TimeUnit.SECONDS);
        instance = this;
        myMethod(true);
        new Thread(new Task()).start();
//        adView = new AdView(this, "724621651433439_729021097660161", AdSize.BANNER_HEIGHT_50);
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
//
//        adContainer.addView(adView);
//        AdListener adListener = new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//                Log.d("mytag", "onError: " + adError.getErrorMessage());
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                Log.d("mytag", "Done done ");
//                adView.loadAd();
//                // Ad loaded callback
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Ad impression logged callback
//            }
//        };
//
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.RobotoTextViewStyle);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int font = pref.getInt("font", 0);
        mode = pref.getInt("mode", 1);

        fragmentManager = getSupportFragmentManager();
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        //drawerLayout.setBackgroundColor(R.color.pdlg_color_black);
        //mAdViewLayout = findViewById(R.id.adView);

        Log.d("mytag", "onCreate: main Activity ");

        HomeFragment homeFragment = new HomeFragment();
        loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
        if (mode == 1) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.dtitle));
            drawerLayout.setBackgroundColor(getResources().getColor(R.color.black));
//        navigationView.setBackgroundColor(getResources().getColor(R.color.black));
//        navigationView.setItemBackgroundResource(R.color.black);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_go_home:
                        HomeFragment homeFragment = new HomeFragment();
                        loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                        return true;
                    case R.id.menu_go_category:
                        CategoryFragment currentCategory = new CategoryFragment();
                        loadFrag(currentCategory, getString(R.string.menu_category), fragmentManager);
                        return true;
                    case R.id.menu_go_latest:
                        LatestFragment latestFragment = new LatestFragment();
                        loadFrag(latestFragment, getString(R.string.menu_latest), fragmentManager);
                        return true;
                    case R.id.menu_go_favourite:
                        FavouriteFragment favouriteFragment = new FavouriteFragment();
                        loadFrag(favouriteFragment, getString(R.string.menu_favourite), fragmentManager);
                        return true;
                    case R.id.menu_go_about:
                        Intent about = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(about);
                        return true;
                    case R.id.menu_go_setting:
                        Intent setting = new Intent(MainActivity.this, Setting.class);
                        startActivity(setting);
                       // finish();
                        return true;
                    case R.id.menu_go_more:
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.play_more_apps))));
                        return true;
                    case R.id.menu_go_rate:
                        RateApp();
                        return true;
                    case R.id.menu_go_share:
                        ShareApp();
                        return true;
                    case R.id.menu_go_privacy:
                        Intent privacy = new Intent(MainActivity.this, PrivacyActivity.class);
                        startActivity(privacy);
                        return true;

                    case R.id.addnew:
                        Intent addnewsong = new Intent(MainActivity.this, Feedback.class);
                        startActivity(addnewsong);
                        return true;
                    default:
                        return true;
                }
            }
        });

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "get_app_details");
        if (JsonUtils.isNetworkAvailable(MainActivity.this)) {
            new getAdSetting(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
        } else {
            showToast();
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }




    public static MainActivity getInstance() {
        return instance;
    }

    public void myMethod(boolean flag) {
        isRunning = flag;
        // do something...
    }
    private void loadBanner() {

        adView = (TMBannerAdView) findViewById(R.id.adBanner);
        adView.load(this, TMBannerAdSizes.STANDARD, new TMAdListener());

    }

    public class TapdaqInitListener extends TMInitListener {

        public void didInitialise() {
            super.didInitialise();
            // Ads may now be requested
            TapdaqAdsUtils.loadInterstitial(MainActivity.this);
            TapdaqAdsUtils.showInterstitial(MainActivity.this);
            loadBanner();
        }

        @Override
        public void didFailToInitialise(TMAdError error) {
            super.didFailToInitialise(error);
            //Tapdaq failed to initialise
        }
    }


    public void showToast() {
        Toast.makeText(myApplication.getApplicationContext(), "OFFLINE mode Activated.!!!", Toast.LENGTH_LONG).show();
//        View toastView = toast.getView();
//        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
//
//        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
//        toastMessage.setTextSize(25);
//        toastMessage.setTextColor(Color.YELLOW);
//        //toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
//        toastMessage.setGravity(Gravity.CENTER);
//        toastMessage.setCompoundDrawablePadding(16);
//        toastView.setBackgroundColor(Color.BLUE);
//        toast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
//        final MenuItem searchMenuItem = menu.findItem(R.id.search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
//
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                if (!hasFocus) {
//                    searchMenuItem.collapseActionView();
//                    searchView.setQuery("", false);
//                }
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String arg0) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                intent.putExtra("search", arg0);
//                startActivity(intent);
//                searchView.clearFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String arg0) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    private void ShareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg) + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void RateApp() {
        final String appName = getPackageName();//your application package name i.e play store application url
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="
                            + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + appName)));
        }
    }

    @Override
    public void onBackPressed() {

        //  ExitApp();
        tellFragments();
        // super.onBackPressed();
    }

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof CategoryFragment) {
                ((CategoryFragment) f).onBackPressed();
                HomeFragment homeFragment = new HomeFragment();
                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
            } else if (f != null && f instanceof LatestFragment) {
                ((LatestFragment) f).onBackPressed();
                HomeFragment homeFragment = new HomeFragment();
                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
            } else if (f != null && f instanceof FavouriteFragment) {
                ((FavouriteFragment) f).onBackPressed();
                HomeFragment homeFragment = new HomeFragment();
                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
            } else if (f != null && f instanceof HomeFragment) {
                ExitApp();
            }

        }
    }

    private void ExitApp() {
        openDialog();
    }


    public void openDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_page);
        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Please Wait..!!!",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.slsindupotha"));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        TextView ok_btn = dialog.findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        dialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    private class getAdSetting extends AsyncTask<String, Void, String> {

        String base64;

        private getAdSetting(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null == result || result.length() == 0) {
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson = jsonArray.getJSONObject(0);
                    Constant.isBanner = objJson.getBoolean("banner_ad");
                    Constant.isInterstitial = objJson.getBoolean("interstital_ad");
                    Constant.adMobBannerId = objJson.getString("banner_ad_id");
                    Constant.adMobInterstitialId = objJson.getString("interstital_ad_id");
                    Constant.adMobPublisherId = objJson.getString("publisher_id");
                    Constant.AD_COUNT_SHOW = objJson.getInt("interstital_ad_click");

                    new GDPRChecker()
                            .withContext(MainActivity.this)
                            .withPrivacyUrl(getString(R.string.privacy_url)) // your privacy url
                            .withPublisherIds(Constant.adMobPublisherId) // your admob account Publisher id
                            //  .withTestMode("9424DF76F06983D1392E609FC074596C")
                            .check();

                    //BannerAds.ShowBannerAds(MainActivity.this, mAdViewLayout);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        //  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.Container, f1, name);
        ft.commit();
        setToolbarTitle(name);
    }

    public void highLightNavigation(int position, String name) {
        navigationView.getMenu().getItem(position).setChecked(true);
        toolbar.setTitle(name);
    }

    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy(this);
        }
        super.onDestroy();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQ_CODE_VERSION_UPDATE) {
//            if (resultCode == Activity.RESULT_CANCELED) {
//                // If the update is cancelled by the user,
//                // you can request to start the update again.
//                inAppUpdateManager.checkForAppUpdate();
//
//                Log.d("mytag" ,"Update flow failed! Result code: " + resultCode);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        SinduPothaAppUpdate.setImmediateUpdateOnResume(appUpdateManager, this);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == SinduPothaAppUpdate.REQUEST_APP_UPDATE){
//            Log.d("ffff","dsddsd");
//        }
//    }



}
