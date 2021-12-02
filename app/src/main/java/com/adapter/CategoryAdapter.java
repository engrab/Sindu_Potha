package com.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.HomeAdsCount;
import com.item.ItemCategory;
import com.slsindupotha.CategoryItemActivity;
import com.slsindupotha.R;
import com.util.Constant;
import com.util.JsonUtils;

import com.squareup.picasso.Picasso;
import com.util.TapdaqAdsUtils;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemRowHolder> {

    private ArrayList<ItemCategory> dataList;
    private final Context mContext;
    // private InterstitialAd mInterstitial;
    private ProgressDialog pDialog;
    int mode;

    public CategoryAdapter(Context context, ArrayList<ItemCategory> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    public void updateList(ArrayList<ItemCategory> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_category, parent, false);
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

        final ItemCategory singleItem = dataList.get(position);
        holder.text.setText(singleItem.getCategoryName());
        Picasso.get().load(singleItem.getCategoryImage()).placeholder(R.drawable.header_top_logo).error(R.drawable.header_top_logo).into(holder.image);
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


                Intent intent = new Intent(mContext, CategoryItemActivity.class);
                intent.putExtra("Id", String.valueOf(singleItem.getCategoryId()));
                intent.putExtra("name", singleItem.getCategoryName());
                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        LinearLayout lyt_parent;
        LinearLayout ltv;
        CardView cd;

        ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ltv = itemView.findViewById(R.id.changelayout);
            cd = itemView.findViewById(R.id.card_view);
            if (mode == 1) {
                lyt_parent.setBackgroundColor(Color.BLACK);
                text.setTextColor(Color.LTGRAY);
                ltv.setBackgroundColor(Color.DKGRAY);
                cd.setBackgroundColor(Color.GRAY);
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
