package com.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.HomeAdsCount;
//import com.facebook.ads.AudienceNetworkAds;
//import com.facebook.ads.InterstitialAd;
import com.item.ItemStory;
import com.slsindupotha.R;
import com.slsindupotha.StoryDetailsActivity;
import com.util.Constant;
import com.util.JsonUtils;

import com.squareup.picasso.Picasso;
import com.util.TapdaqAdsUtils;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ItemRowHolder> {

    private ArrayList<ItemStory> dataList;
    private Context mContext;
    //  private InterstitialAd mInterstitial;
    private ProgressDialog pDialog;
    int mode;

    public StoryAdapter(Context context, ArrayList<ItemStory> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    public void updateList(ArrayList<ItemStory> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_story, parent, false);
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
        int font = pref.getInt("font", 0);
        mode = pref.getInt("mode", 1);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, final int position) {

//        AudienceNetworkAds.initialize(mContext);
//        InterstitialAd interstitialAd = new InterstitialAd(mContext, mContext.getString(R.string.ad_unit_Interstitial_facebook_));
//        interstitialAd.loadAd();

        final ItemStory singleItem = dataList.get(position);
        holder.text.setText(singleItem.getStoryTitle());
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/malithi.ttf");
        holder.text.setTypeface(tf, Typeface.BOLD);

        Picasso.get().load(singleItem.getstoryImage()).placeholder(R.drawable.story_list_icon).error(R.drawable.story_list_icon).into(holder.image);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeAdsCount.AD_COUNT = HomeAdsCount.AD_COUNT + 1;
                Log.d("mytag", "onClick: pos story" + HomeAdsCount.AD_COUNT);


//                interstitialAd.loadAd();
                if (HomeAdsCount.AD_COUNT == 7) {

                   TapdaqAdsUtils.showInterstitial((Activity) mContext);

                    HomeAdsCount.AD_COUNT = 0;
                    Log.d("mytag", "onClick: after " + HomeAdsCount.AD_COUNT);
                }


                Intent intent = new Intent(mContext, StoryDetailsActivity.class);
                intent.putExtra("Id", singleItem.getId());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        LinearLayout lyt_parent;
        LinearLayout ltv;

        ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.textsong);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ltv = itemView.findViewById(R.id.changelayout);
            if (mode == 1) {
                lyt_parent.setBackgroundColor(Color.BLACK);
                text.setTextColor(Color.LTGRAY);
                ltv.setBackgroundColor(Color.DKGRAY);

            } else {

            }
        }
    }

    private void Loading() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getResources().getString(R.string.loading_msg));
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
