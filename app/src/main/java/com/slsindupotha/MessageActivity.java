package com.slsindupotha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.db.DatabaseHelper;


public class MessageActivity extends BaseActivity {
    TextView txtName, txtDate, txtView;
    WebView webView;
    LinearLayout lin;
    Toolbar toolbar;
    ScrollView mScrollView;
    int Id;
    LinearLayout mAdViewLayout;
    Menu menu;
    DatabaseHelper databaseHelper;
    boolean isFromNotification = false;
    ProgressBar mProgressBar;
    ImageView ImgStory;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
      //  IsRTL.ifSupported(MessageActivity.this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int font=pref.getInt("font",50);
        mode=pref.getInt("mode",1);
        toolbar.setTitleTextAppearance(this, R.style.RobotoTextViewStyle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setTitle("Messages");
        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );

        Intent i = getIntent();
        Id = i.getIntExtra("Id",0);
        if (i.hasExtra("isNotification")) {
            isFromNotification = true;
        }

        mAdViewLayout = findViewById(R.id.adView_story_banner);
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


        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
   //     webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);



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
        //WebSettings webSettings = webView.getSettings();
       // webSettings.setJavaScriptEnabled(true);
       // webSettings.setTextZoom(50+font);// from thetre you can adjust font size


        txtName.setText("NEW MESSAGE..!!!");

        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";
        String htmlText = getIntent().getStringExtra("welcomeMsg");
        String htmlText2 = getString(R.string.about_desc);

    //    boolean isRTL = Boolean.parseBoolean(getResources().getString(R.string.isRTL));
        //String direction = isRTL ? "rtl" : "ltr";
        String text;
        if(mode==1){
            text = "<html><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/malithi.ttf\")}body{font-family: MyFont;color:#cfcfcf;  background-color:#444444;font-size:18px;margin-left:0px;line-height:1.2}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + htmlText2
                    + "</body></html>";
        }else {
            text = "<html><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/malithi.ttf\")}body{font-family: MyFont;color: #000000;font-size:18px;margin-left:0px;line-height:1.2}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + htmlText2
                    + "</body></html>";
        }
        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);


       // BannerAds.ShowBannerAds(MessageActivity.this, mAdViewLayout);
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (isFromNotification) {
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }

    }
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }
}
