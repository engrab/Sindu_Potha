package com.slsindupotha;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.adapter.StoryAdapter;
import com.db.DatabaseHelperRecent;
import com.item.ItemStory;

import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.util.IsRTL;
import com.util.ItemOffsetDecoration;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class ActivityRecent extends BaseActivity {
//    private AdView adView;
    ArrayList<ItemStory> mListItem;
    public RecyclerView recyclerView;
    StoryAdapter allVideoAdapter;
    Toolbar toolbar;
    LinearLayout adLayout;
    DatabaseHelperRecent databaseHelperRecent;
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
        IsRTL.ifSupported(ActivityRecent.this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("මා නැරඹු ගීත");
        loadBanner();
//        AudienceNetworkAds.initialize(this);
//        adView = new AdView(this, "724621651433439_729021350993469", AdSize.BANNER_HEIGHT_50);
        // Find the Ad Container
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.adView_story_banner);
        // Add the ad view to your activity layout
//        adContainer.addView(adView);
        // Request an ad
//        adView.loadAd();


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int font=pref.getInt("font",0);
        int mode=pref.getInt("mode",1);
        if(mode==1){
            setActivityBackgroundColor(Color.BLACK);
        }
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

        databaseHelperRecent = new DatabaseHelperRecent(ActivityRecent.this);

        mListItem = new ArrayList<>();
        recyclerView = findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        if(mode==1){
        recyclerView.setBackgroundColor(Color.BLACK);}
        recyclerView.setLayoutManager(new GridLayoutManager(ActivityRecent.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityRecent.this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        adLayout = findViewById(R.id.adView_story_banner);
     //   BannerAds.ShowBannerAds(ActivityRecent.this, adLayout);

    }
    @Override
    public void onResume() {
        super.onResume();
        mListItem = databaseHelperRecent.getFavouriteAll();
        displayDataRecent();
    }

    private void displayDataRecent() {

        allVideoAdapter = new StoryAdapter(ActivityRecent.this, mListItem);
        recyclerView.setAdapter(allVideoAdapter);
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
        if (adView != null) {
            adView.destroy(this);
        }
        super.onDestroy();
    }

}
