package com.slsindupotha;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.db.DataBaseRlmaxAll;
import com.db.DatabaseHelper;
import com.db.DatabaseHelperRecent;

import com.item.ItemStory;

import com.util.IsRTL;
import com.squareup.picasso.Picasso;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class StoryDetailsActivity extends BaseActivity {

    TextView txtName, txtDate, txtView;
    WebView webView;
    LinearLayout lin;
    Toolbar toolbar;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ItemStory objBean;
    int Id;
//    private AdView adView;
    Menu menu;
    DatabaseHelper databaseHelper;
    boolean isFromNotification = false;
    DatabaseHelperRecent databaseHelperRecent;
    DataBaseRlmaxAll dataBaseRlmaxAll;

    final Handler handler = new Handler();
    ImageView ImgStory;
    int mode;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
//        AudienceNetworkAds.initialize(this);
//        adView = new AdView(this, "724621651433439_729074457654825", AdSize.BANNER_HEIGHT_50);
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.adView_story_banner);
//        adContainer.addView(adView);
//        adView.loadAd();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("mytag", "onCreate: story activity");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int font=pref.getInt("font",50);
        mode=pref.getInt("mode",1);
        toolbar.setTitleTextAppearance(this, R.style.RobotoTextViewStyle);
        dataBaseRlmaxAll = new DataBaseRlmaxAll(StoryDetailsActivity.this);
        databaseHelperRecent = new DatabaseHelperRecent(StoryDetailsActivity.this);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );

        Intent i = getIntent();
        Id = i.getIntExtra("Id",0);
        if (i.hasExtra("isNotification")) {
            isFromNotification = true;
        }


        lin=(LinearLayout)findViewById(R.id.changedlayout2);
        txtName = findViewById(R.id.text);
        txtDate = findViewById(R.id.textDate);
        txtView = findViewById(R.id.textView);
        ImgStory=findViewById(R.id.image);
        webView = findViewById(R.id.webDesc);

        txtView.setVisibility(View.INVISIBLE);

        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/malithi.ttf");
        txtName.setTypeface(face,Typeface.BOLD);
        txtDate.setTypeface(face,Typeface.BOLD);
//        txtView.setTypeface(face,Typeface.BOLD);




        if(mode==1){
            setActivityBackgroundColor(Color.BLACK);
            txtName.setTextColor(Color.WHITE);
            txtDate.setTextColor(Color.LTGRAY);
            txtView.setTextColor(Color.LTGRAY);
            lin.setBackgroundColor(Color.DKGRAY);
            toolbar.setBackgroundColor(getResources().getColor(R.color.dtitle));
        }
        mScrollView = findViewById(R.id.scrollView);
        if(mode==1){
        mScrollView.setBackgroundColor(getResources().getColor(R.color.black));}
        mProgressBar = findViewById(R.id.progressBar1);
        webView.setBackgroundColor(Color.TRANSPARENT);
        if(mode==1){
            webView.setBackgroundColor(Color.BLACK);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setTextZoom(50+font);// from thetre you can adjust font size
        objBean = new ItemStory();
        objBean = dataBaseRlmaxAll.getItemStoryIdAllData(Id);
        if(objBean != null){
            setResult();
        }
//        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
//        jsObj.addProperty("method_name", "get_single_story");
//        jsObj.addProperty("story_id", Id);
//        if (JsonUtils.isNetworkAvailable(StoryDetailsActivity.this)) {
//            new getStory(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
//        } else {
//            showToast(getString(R.string.connection_msg1));
//        }







       // BannerAds.ShowBannerAds(StoryDetailsActivity.this, mAdViewLayout);
//        LinearLayout adContainer = findViewById(R.id.adView);
//
//        // Add the ad view to your activity layout
//        adContainer.addView(adView);
//        adView.loadAd();


    }


    private void setResult() {
        txtName.setText(objBean.getStoryTitle());
        txtDate.setText(objBean.getStoryDate());
        txtView.setText(objBean.getStoryViews() +" "+getString(R.string.views_title));
        Picasso.get().load(objBean.getstoryImage()).placeholder(R.drawable.story_list_icon).error(R.drawable.story_list_icon).into(ImgStory);

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = objBean.getStoryDescription();

        boolean isRTL = Boolean.parseBoolean(getResources().getString(R.string.isRTL));
        String direction = isRTL ? "rtl" : "ltr";
        String text;
        if(mode==1){
            text = "<html dir=" + direction + "><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/malithi.ttf\")}body{font-family: MyFont;color:#cfcfcf;  background-color:#444444;font-size:18px;margin-left:0px;line-height:1.2}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";
        }else {
            text = "<html dir=" + direction + "><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/malithi.ttf\")}body{font-family: MyFont;color: #000000;font-size:18px;margin-left:0px;line-height:1.2}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";
        }
        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
        ContentValues fav_rec = new ContentValues();
        if (databaseHelperRecent.getFavouriteById(Id)) {
            databaseHelperRecent.removeFavouriteById(Id);
            fav_rec.put(DatabaseHelperRecent.KEY_ID, Id);
            fav_rec.put(DatabaseHelperRecent.KEY_TITLE, objBean.getStoryTitle());
            fav_rec.put(DatabaseHelperRecent.KEY_IMAGE, objBean.getstoryImage());
            databaseHelperRecent.addFavourite(DatabaseHelperRecent.TABLE_FAVOURITE_NAME, fav_rec, null);
        } else {
            fav_rec.put(DatabaseHelperRecent.KEY_ID, Id);
            fav_rec.put(DatabaseHelperRecent.KEY_TITLE, objBean.getStoryTitle());
            fav_rec.put(DatabaseHelperRecent.KEY_IMAGE, objBean.getstoryImage());
            databaseHelperRecent.addFavourite(DatabaseHelperRecent.TABLE_FAVOURITE_NAME, fav_rec, null);
        }

    }

    public void showToast(String msg) {
        Toast.makeText(StoryDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_story, menu);
        this.menu = menu;
        isFavourite();
        return true;
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_bookmark:
                ContentValues fav = new ContentValues();
                if (databaseHelper.getFavouriteById(Id)) {
                    databaseHelper.removeFavouriteById(Id);
                    menu.getItem(0).setIcon(R.drawable.ic_bookmark_border_white_24dp);
                    Toast.makeText(StoryDetailsActivity.this, getString(R.string.favourite_remove), Toast.LENGTH_SHORT).show();
                } else {
                    fav.put(DatabaseHelper.KEY_ID, Id);
                    fav.put(DatabaseHelper.KEY_TITLE, objBean.getStoryTitle());
                    fav.put(DatabaseHelper.KEY_IMAGE,objBean.getstoryImage());
                    databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                    menu.getItem(0).setIcon(R.drawable.ic_bookmark_white_24dp);
                    Toast.makeText(StoryDetailsActivity.this, getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        objBean.getStoryTitle() + "\n\n" +
                                Html.fromHtml(objBean.getStoryDescription()) + "\n" +
                                "https://play.google.com/store/apps/details?id=" +
                                getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    private void showAdWithDelay() {
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                // Check if interstitialAd has been loaded successfully
//                if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
//                    return;
//                }
//                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
//                if(interstitialAd.isAdInvalidated()) {
//                    return;
//                }
//                // Show the ad
//                interstitialAd.show();
//            }
//        }, 1000 * 60 * 5);
    }

    private void isFavourite() {
        if (databaseHelper.getFavouriteById(Id)) {
            menu.getItem(0).setIcon(R.drawable.ic_bookmark_white_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }
    }


    @Override
    protected void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (isFromNotification) {
            Intent intent = new Intent(StoryDetailsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        try {
            if (MainActivity.getInstance() != null) {
                MainActivity.getInstance().myMethod(false);
            } else {
                MainActivity mActivity = new MainActivity();
                mActivity.getInstance().myMethod(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(MainActivity.getInstance() != null) {
            MainActivity.getInstance().myMethod(true);
        }
        super.onPause();
    }
}
