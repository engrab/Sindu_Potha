package com.slsindupotha;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.adapter.StoryAdapter;
import com.db.DataBaseRlmaxAll;
import com.item.ItemStory;

import com.util.Constant;
import com.util.IsRTL;
import com.util.ItemOffsetDecoration;
import com.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class SearchActivity extends BaseActivity {

    ArrayList<ItemStory> mListItem;
    public RecyclerView recyclerView;
    StoryAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    String Search;
    Toolbar toolbar;
   // LinearLayout mAdViewLayout;
    DataBaseRlmaxAll dataBaseRlmaxAll;
//    private AdView adView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);
//        adView = new AdView(this, "724621651433439_729021097660161", AdSize.BANNER_HEIGHT_50);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        dataBaseRlmaxAll = new DataBaseRlmaxAll(this);
        int font=pref.getInt("font",0);
        int mode=pref.getInt("mode",1);
        if(mode==1){
            setActivityBackgroundColor(Color.BLACK);
        }
        IsRTL.ifSupported(SearchActivity.this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_search));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.RobotoTextViewStyle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );

        Intent intent = getIntent();
        Search = intent.getStringExtra("search");
        mListItem = new ArrayList<>();
        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.vertical_courses_list);
        if(mode==1){
            recyclerView.setBackgroundColor(Color.BLACK);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(SearchActivity.this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

//        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
//        jsObj.addProperty("method_name", "get_search_stories");
//        jsObj.addProperty("search", Search);
//        if (JsonUtils.isNetworkAvailable(SearchActivity.this)) {
//            new getSearch(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
//        }
       mListItem = dataBaseRlmaxAll.getStorySearch(Search);
        if(!mListItem.isEmpty()){
            displayData();
        }

       // mAdViewLayout = findViewById(R.id.adView);
        LinearLayout adContainer = findViewById(R.id.adView_story_banner);

        // Add the ad view to your activity layout
//        adContainer.addView(adView);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Ad error callback
//                Log.e("SSSAA", "Interstitial ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
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
//        });
//
//        // Request an ad
//        adView.loadAd();
      //  BannerAds.ShowBannerAds(SearchActivity.this, mAdViewLayout);
    }

    @SuppressLint("StaticFieldLeak")
    private class getSearch extends AsyncTask<String, Void, String> {

        String base64;

        private getSearch(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemStory objItem = new ItemStory();
                        objItem.setId(Integer.parseInt(objJson.getString(Constant.STORY_ID)));
                        objItem.setStoryTitle(objJson.getString(Constant.STORY_TITLE));
                        objItem.setstoryImage(objJson.getString(Constant.STORY_IMAGE));
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }


    private void displayData() {
        adapter = new StoryAdapter(SearchActivity.this, mListItem);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }


    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
       final MenuItem searchMenuItem = menu.findItem(R.id.search);
       searchMenuItem.expandActionView();
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                mListItem = dataBaseRlmaxAll.getStorySearch(arg0);
                if(!mListItem.isEmpty()){
                    displayData();
                }
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                mListItem = dataBaseRlmaxAll.getStorySearch(arg0);
                if(!mListItem.isEmpty()){
                    displayData();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    @Override
    protected void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
        super.onDestroy();
    }
}
