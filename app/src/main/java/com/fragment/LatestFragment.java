package com.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.adapter.StoryAdapter;
import com.db.DataBaseRlmaxAll;
import com.interfaces.OnBackPressed;
import com.item.ItemStory;
import com.slsindupotha.R;
import com.slsindupotha.MyApplication;
import com.util.APICallAync;
import com.util.ItemOffsetDecoration;
import com.util.JsonUtils;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class LatestFragment extends BaseFragment implements OnBackPressed {

    ArrayList<ItemStory> mListItem;
    public RecyclerView recyclerView;
    StoryAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    DataBaseRlmaxAll  dataBaseRlmaxAll;
    int callBack  = 0;
    private static final int  LIST_LIMIT = 500;
    private LinearLayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview, container, false);
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0);
        int font=pref.getInt("font",0);
        int mode=pref.getInt("mode",1);
        dataBaseRlmaxAll = new DataBaseRlmaxAll(getActivity());
        mListItem = new ArrayList<>();
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);

        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        if(mode==1){
        recyclerView.setBackgroundColor(Color.BLACK);}
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);







        displayData();
        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            if(getApp().getContactsManager().loadingCat ==0 || getApp().getContactsManager().loadingCat ==3) {
                if (getApp().getSettings().getStory() != Integer.parseInt(dataBaseRlmaxAll.getStoryCount())) {
                    getApp().getContactsManager().getStoryAync();
                }
            }
        }

        return rootView;
    }

    private ArrayList<ItemStory> latestSongs(int min, int max){
        mListItem.addAll(dataBaseRlmaxAll.getStory(min,max));
        return mListItem;
    }
    private ArrayList<ItemStory> prevloadPreviousSongs(int min , int max){

        mListItem.addAll(dataBaseRlmaxAll.getStory(min,max));
        return mListItem;
    }

    private void displayData() {
        Log.d("Check ID Size :", String.valueOf(Integer.parseInt(dataBaseRlmaxAll.getStoryCount())));
        adapter = new StoryAdapter(getActivity(),latestSongs(LIST_LIMIT,0));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                    adapter.updateList(prevloadPreviousSongs(LIST_LIMIT,mListItem.size()));
                   // Log.d("Check ID Size :", String.valueOf(mListItem.size()));
                }
            }
        });

        final APICallAync apiCallAync =  getApp().getContactsManager();
        callBack = apiCallAync.loadingCat;
        final Handler handler = new Handler();

        final Runnable task = new Runnable() {
            @Override
            public void run() {
              //  callBack = getApp().getContactsManager().loadingCat;
                callBack = apiCallAync.loadingCat;
                handler.postDelayed(this, 2000);
                if(mListItem.size() > LIST_LIMIT-1){
                }else{
                    adapter.updateList(latestSongs(LIST_LIMIT,(mListItem.size()+5)));
                    //Log.d("Check ID Size :", String.valueOf(Integer.parseInt(dataBaseRlmaxAll.getStoryCount())));
                }
                if(callBack == 3) {

                    showProgress(false);
                    handler.removeCallbacks(this);
                }
            }

        };
        if(callBack ==2 ) {
            showProgress(true);
            handler.post(task);

        }
            if (adapter.getItemCount() == 0) {
                lyt_not_found.setVisibility(View.GONE);
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
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
