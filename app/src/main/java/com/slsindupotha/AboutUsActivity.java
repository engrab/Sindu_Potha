package com.slsindupotha;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Toast;

import com.db.DataBaseRlmaxAll;
import com.item.ItemAbout;
import com.util.Constant;
import com.util.IsRTL;
import com.util.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;



public class AboutUsActivity extends BaseActivity {

    TextView txtAppName, txtVersion, txtCompany, txtEmail, txtWebsite, txtContact;
    ImageView imgAppLogo;
    ArrayList<ItemAbout> mListItem;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    WebView webView;
    LinearLayout linearLayout_email, linearLayout_website, linearLayout_phone;
    JsonUtils jsonUtils;
    DataBaseRlmaxAll dataBaseRlmaxAll;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        IsRTL.ifSupported(AboutUsActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_about));
        setSupportActionBar(toolbar);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int font=pref.getInt("font",0);
        int mode=pref.getInt("mode",1);
        toolbar.setTitleTextAppearance(this, R.style.RobotoTextViewStyle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );

        txtAppName = findViewById(R.id.text_app_name);
        txtVersion = findViewById(R.id.text_version);
        txtCompany = findViewById(R.id.text_company);
        txtEmail = findViewById(R.id.text_email);
        txtWebsite = findViewById(R.id.text_website);
        txtContact = findViewById(R.id.text_contact);
        imgAppLogo = findViewById(R.id.app_logo_about_us);
        webView = findViewById(R.id.webView);
        dataBaseRlmaxAll = new DataBaseRlmaxAll(this);
        mScrollView = findViewById(R.id.scrollView);
        if(mode==1){
        mScrollView.setBackgroundColor(getResources().getColor(R.color.black));}
        mProgressBar = findViewById(R.id.progressBar);

        mListItem = new ArrayList<>();
        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        linearLayout_email = findViewById(R.id.linearLayout_email_about_us);
        linearLayout_website = findViewById(R.id.linearLayout_web_about_us);
        linearLayout_phone = findViewById(R.id.linearLayout_contact_about_us);
        setResult();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTaskAbout extends AsyncTask<String, Void, String> {

        String base64;

        private MyTaskAbout(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            if (null == result || result.length() == 0) {
                showToast(getString(R.string.no_data));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemAbout itemAbout = new ItemAbout();
                        itemAbout.setAppName(objJson.getString(Constant.APP_NAME));
                        itemAbout.setAppLogo(objJson.getString(Constant.APP_IMAGE));
                        itemAbout.setAppVersion(objJson.getString(Constant.APP_VERSION));
                        itemAbout.setAppAuthor(objJson.getString(Constant.APP_AUTHOR));
                        itemAbout.setAppEmail(objJson.getString(Constant.APP_EMAIL));
                        itemAbout.setAppWebsite(objJson.getString(Constant.APP_WEBSITE));
                        itemAbout.setAppContact(objJson.getString(Constant.APP_CONTACT));
                        itemAbout.setAppDescription(objJson.getString(Constant.APP_DESC));
                        mListItem.add(itemAbout);
                    }
                    setResult();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void setResult() {
        mListItem = dataBaseRlmaxAll.getAbout();
        if(mListItem.size() >0) {
            final ItemAbout itemAbout = mListItem.get(0);
            txtAppName.setText(itemAbout.getAppName());
            txtVersion.setText(itemAbout.getAppVersion());
            txtCompany.setText(itemAbout.getAppAuthor());
            txtEmail.setText(itemAbout.getAppEmail());
            txtWebsite.setText(itemAbout.getAppWebsite());
            txtContact.setText(itemAbout.getAppContact());
            Picasso.get().load(Constant.IMAGE_PATH + itemAbout.getAppLogo()).into(imgAppLogo);

            String mimeType = "text/html";
            String encoding = "utf-8";
            String htmlText = itemAbout.getAppDescription();

            boolean isRTL = Boolean.parseBoolean(getResources().getString(R.string.isRTL));
            String direction = isRTL ? "rtl" : "ltr";
            String text = "<html dir=" + direction + "><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Montserrat-Medium_0.otf\")}body{font-family: MyFont;color:#8b8b8b;font-size:14px;line-height:1.6}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

            linearLayout_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{itemAbout.getAppEmail()
                        });
                        emailIntent.setData(Uri.parse("mailto:?subject="));
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        jsonUtils.alertBox(getResources().getString(R.string.wrong));
                    }
                }
            });

            linearLayout_website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String url = itemAbout.getAppWebsite();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        jsonUtils.alertBox(getResources().getString(R.string.wrong));
                    }
                }
            });

            linearLayout_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + itemAbout.getAppContact()));
                        startActivity(callIntent);
                    } catch (Exception e) {
                        jsonUtils.alertBox(getResources().getString(R.string.wrong));
                    }
                }
            });
        }
    }


    public void showToast(String msg) {
        Toast.makeText(AboutUsActivity.this, msg, Toast.LENGTH_LONG).show();
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
}
