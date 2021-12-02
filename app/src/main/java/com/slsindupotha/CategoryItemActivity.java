package com.slsindupotha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

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

import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.util.IsRTL;
import com.util.ItemOffsetDecoration;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * Created by laxmi.
 */
public class CategoryItemActivity extends BaseActivity {

    ArrayList<ItemStory> mListItem;
    public RecyclerView recyclerView;
    StoryAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    String Id, Name;
    // LinearLayout mAdViewLayout;
//   private AdView adView;
    DataBaseRlmaxAll dataBaseRlmaxAll;
    int callBack = 0;
    TMBannerAdView adView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void loadBanner() {

        adView = (TMBannerAdView) findViewById(R.id.adBanner);
        adView.load(this, TMBannerAdSizes.STANDARD, new TMAdListener());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);
        IsRTL.ifSupported(CategoryItemActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadBanner();
//        AudienceNetworkAds.initialize(this);
        dataBaseRlmaxAll = new DataBaseRlmaxAll(this);
//        adView = new AdView(this, "724621651433439_729073764321561", AdSize.BANNER_HEIGHT_50);
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.adView_story_banner);
        // Add the ad view to your activity layout
//        adContainer.addView(adView);

//        adView.loadAd();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int font = pref.getInt("font", 0);
        int mode = pref.getInt("mode", 1);
        toolbar.setTitleTextAppearance(this, R.style.RobotoTextViewStyle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Intent intent = getIntent();
        Id = intent.getStringExtra("Id");
        Name = intent.getStringExtra("name");
        setTitle(Name);
        mListItem = new ArrayList<>();
        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.vertical_courses_list);
        if (mode == 1) {
            recyclerView.setBackgroundColor(Color.BLACK);
        }
        // lyt_not_found.setBackgroundColor(Color.BLACK);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryItemActivity.this, LinearLayoutManager.VERTICAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(CategoryItemActivity.this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

//        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
//        jsObj.addProperty("method_name", "get_stories_by_cat_id");
//        jsObj.addProperty("cat_id", Id);
//        if (JsonUtils.isNetworkAvailable(CategoryItemActivity.this)) {
//            new getCategory(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
//        }
        mListItem = dataBaseRlmaxAll.getStoryByCatId(Id);
        //if(!mListItem.isEmpty()){
        displayData();
        // }


    }


    private void displayData() {
        adapter = new StoryAdapter(CategoryItemActivity.this, mListItem);
        recyclerView.setAdapter(adapter);

        callBack = myApplication.getContactsManager().loadingCat;
        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                callBack = myApplication.getContactsManager().loadingCat;
                handler.postDelayed(this, 2000);
                adapter.updateList(dataBaseRlmaxAll.getStoryByCatId(Id));
                if (callBack == 3) {
                    adapter.updateList(dataBaseRlmaxAll.getStoryByCatId(Id));
                    showProgress(false);
                    handler.removeCallbacks(this);
                }
            }

        };
        if (callBack == 2) {
            showProgress(true);
            handler.post(task);

        }

        if (adapter.getItemCount() == 0) {
            //  lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    private void showProgress(boolean show) {
        if (show) {

            progressBar.setVisibility(View.VISIBLE);
            //  recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search:
                Intent intent = new Intent(CategoryItemActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy(this);
        }
        super.onDestroy();
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
//                Intent intent = new Intent(CategoryItemActivity.this, SearchActivity.class);
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
}
