package com.slsindupotha;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.db.DataBaseRlmaxAll;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.item.ItemAbout;
import com.util.API;
import com.util.Constant;
import com.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.util.Settings;
//import com.util.SinduPothaAppUpdate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class SplashActivity extends BaseActivity {

    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 2000;
    String str_package;
    ArrayList<ItemAbout> mListItem;
    DataBaseRlmaxAll dataBaseRlmaxAll;
    TextView tv1;
    private AppUpdateManager appUpdateManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    static final int REQ_CODE_VERSION_UPDATE = 530;
    InAppUpdateManager inAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        InAppUpdateManager.Builder((AppCompatActivity) this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(Constants.UpdateMode.IMMEDIATE)
                .checkForAppUpdate();


        //  appUpdateManager = AppUpdateManagerFactory.create(this);
        //  SinduPothaAppUpdate.setImmediateUpdate(appUpdateManager,this);
        dataBaseRlmaxAll = new DataBaseRlmaxAll(getApplicationContext());
        mListItem = new ArrayList<>();
        final JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "get_app_details");
        final JsonObject jsObj1 = (JsonObject) new Gson().toJsonTree(new API());
        jsObj1.addProperty("method_name", "get_home");
        final JsonObject jsObj4 = (JsonObject) new Gson().toJsonTree(new API());
        jsObj4.addProperty("method_name", "get_health_all");
        JsonObject jsObj5 = (JsonObject) new Gson().toJsonTree(new API());
        jsObj5.addProperty("method_name", "get_app_details");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        tv1 = (TextView) findViewById(R.id.textView2);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/nine.otf");
        tv1.setTypeface(typeface);


        String formattedDate = df.format(c);
        if (JsonUtils.isNetworkAvailable(SplashActivity.this)) {
            if (dataBaseRlmaxAll.getCategory().size() != 0 && Integer.parseInt(dataBaseRlmaxAll.getStoryCount()) != 0 && dataBaseRlmaxAll.getSlider().size() != 0) {
                new dbHealth(API.toBase64(jsObj4.toString())).execute(Constant.API_URL);
                if (myApplication.getSettings().getDate().length() > 0) {

                    if (!formattedDate.equals(myApplication.getSettings().getDate())) {
                        myApplication.getContactsManager().getSliderAync(API.toBase64(jsObj1.toString()));
                        myApplication.getContactsManager().getCatAyncManully();
                        myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
                    } else if (dataBaseRlmaxAll.getAbout().size() == 0) {
                        myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
                    }
                } else {
                    myApplication.getContactsManager().getSliderAync(API.toBase64(jsObj1.toString()));
                    myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
                }
                new MyTaskDev(API.toBase64(jsObj.toString())).execute(Constant.API_URL);

            } else {
                new dbHealth(API.toBase64(jsObj4.toString())).execute(Constant.API_URL);
                myApplication.getContactsManager().getSliderAync(API.toBase64(jsObj1.toString()));
                new MyTaskDev(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
                myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
            }

        } else {
            if (dataBaseRlmaxAll.getCategory().size() != 0 && Integer.parseInt(dataBaseRlmaxAll.getStoryCount()) != 0 && dataBaseRlmaxAll.getSlider().size() != 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }, SPLASH_DURATION);


            } else {
                // showToast(getString(R.string.connection_msg1));
                new AlertDialog.Builder(this)
                        .setTitle("No Internet")
                        .setMessage("පළමු අවස්ථාවේදී පමණක් අන්තර්ජාල සම්බන්ධතාව අවශ්\u200Dයයි. කරුණාකර Data හෝ Wifi ON කර නැවත උත්සාහ කරන්න. ඉන් පසු ඔබට Offline ලෙස ඇප් එක භාවිතා කල හැක.")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (JsonUtils.isNetworkAvailable(SplashActivity.this)) {
                                    new dbHealth(API.toBase64(jsObj4.toString())).execute(Constant.API_URL);
                                    myApplication.getContactsManager().getSliderAync(API.toBase64(jsObj1.toString()));
                                    new MyTaskDev(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
                                } else {
                                    finish();
                                }
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        }

    }


    @SuppressLint("StaticFieldLeak")
    private class MyTaskDev extends AsyncTask<String, Void, String> {

        String base64;

        private MyTaskDev(String base64) {
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
                // POP-UP BOX
                openDialog();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has("status")) {
                            final PrettyDialog dialog = new PrettyDialog(SplashActivity.this);
                            dialog.setTitle(getString(R.string.dialog_error))
                                    .setTitleColor(R.color.dialog_text)
                                    .setMessage(getString(R.string.restart_msg))
                                    .setMessageColor(R.color.dialog_text)
                                    .setAnimationEnabled(false)
                                    .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            dialog.setCancelable(false);
                            dialog.show();
                        } else {
                            str_package = objJson.getString(Constant.APP_PACKAGE_NAME);

                            Constant.isBanner = objJson.getBoolean("banner_ad");
                            Constant.isInterstitial = objJson.getBoolean("interstital_ad");
                            Constant.adMobBannerId = objJson.getString("banner_ad_id");
                            Constant.adMobInterstitialId = objJson.getString("interstital_ad_id");
                            Constant.adMobPublisherId = objJson.getString("publisher_id");
                            Constant.AD_COUNT_SHOW = objJson.getInt("interstital_ad_click");

                            if (str_package.equals(getPackageName())) {

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            } else {
                                final PrettyDialog dialog = new PrettyDialog(SplashActivity.this);
                                dialog.setTitle(getString(R.string.dialog_error))
                                        .setTitleColor(R.color.dialog_text)
                                        .setMessage(getString(R.string.license_msg))
                                        .setMessageColor(R.color.dialog_text)
                                        .setAnimationEnabled(false)
                                        .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                dialog.setCancelable(false);
                                dialog.show();
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Integer.parseInt(dataBaseRlmaxAll.getCateCount());
                if (myApplication.getSettings().getStory() != Integer.parseInt(dataBaseRlmaxAll.getStoryCount())) {
                    myApplication.getContactsManager().getStoryAync();
                }

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class dbHealth extends AsyncTask<String, Void, String> {

        String base64;

        private dbHealth(String base64) {
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
                final JsonObject jsObj4 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj4.addProperty("method_name", "get_health_all");
                new dbHealth(API.toBase64(jsObj4.toString())).execute(Constant.API_URL);
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONObject mainJsonHealth = mainJson.getJSONObject(Constant.ARRAY_NAME);
                    int category = Integer.parseInt(mainJsonHealth.getString("category_count"));
                    int storeis = Integer.parseInt(mainJsonHealth.getString("stories_count"));
                    int slider = Integer.parseInt(mainJsonHealth.getString("slider_count"));

                    new Settings(getApplicationContext()).setCat(category);
                    new Settings(getApplicationContext()).setStory(storeis);
                    new Settings(getApplicationContext()).setSlider(slider);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    if (myApplication.getSettings().getDate().length() > 0) {
                        if (!formattedDate.equals(myApplication.getSettings().getDate())) {
                            new Settings(getApplicationContext()).setDate(formattedDate);
                        }
                    } else {
                        new Settings(getApplicationContext()).setDate(formattedDate);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void openDialog() {

        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.retry_page);
        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Cancel BUTTON
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+94755634842"));
                startActivity(intent);

            }
        });
        TextView ok_btn = dialog.findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // OK BUTTON

                final JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
                jsObj.addProperty("method_name", "get_app_details");
                final JsonObject jsObj1 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj1.addProperty("method_name", "get_home");
                final JsonObject jsObj4 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj4.addProperty("method_name", "get_health_all");
                JsonObject jsObj5 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj5.addProperty("method_name", "get_app_details");
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                if (JsonUtils.isNetworkAvailable(SplashActivity.this)) {
                    if (dataBaseRlmaxAll.getCategory().size() != 0 && Integer.parseInt(dataBaseRlmaxAll.getStoryCount()) != 0 && dataBaseRlmaxAll.getSlider().size() != 0) {
                        new dbHealth(API.toBase64(jsObj4.toString())).execute(Constant.API_URL);
                        if (myApplication.getSettings().getDate().length() > 0) {

                            if (!formattedDate.equals(myApplication.getSettings().getDate())) {
                                myApplication.getContactsManager().getSliderAync(API.toBase64(jsObj1.toString()));
                                myApplication.getContactsManager().getCatAyncManully();
                                myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
                            } else if (dataBaseRlmaxAll.getAbout().size() == 0) {
                                myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
                            }
                        } else {
                            myApplication.getContactsManager().getSliderAync(API.toBase64(jsObj1.toString()));
                            myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
                        }
                        new MyTaskDev(API.toBase64(jsObj.toString())).execute(Constant.API_URL);

                    } else {
                        new dbHealth(API.toBase64(jsObj4.toString())).execute(Constant.API_URL);
                        myApplication.getContactsManager().getSliderAync(API.toBase64(jsObj1.toString()));
                        new MyTaskDev(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
                        myApplication.getContactsManager().getAboutAync(API.toBase64(jsObj5.toString()));
                    }

                } else {
                    if (dataBaseRlmaxAll.getCategory().size() != 0 && Integer.parseInt(dataBaseRlmaxAll.getStoryCount()) != 0 && dataBaseRlmaxAll.getSlider().size() != 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }, SPLASH_DURATION);


                    } else {

                        Toast.makeText(SplashActivity.this, "පළමු අවස්ථාවේදී පමණක් අන්තර්ජාල සම්බන්ධතාව අවශ්\u200Dයයි. කරුණාකර Data හෝ Wifi ON කර නැවත උත්සාහ කරන්න. ඉන් පසු ඔබට Offline ලෙස ඇප් එක භාවිතා කල හැක.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        if (!SplashActivity.this.isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.show();

                }
            });
        }

    }

    public void showToast(String msg) {
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }


}
