package com.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.adapter.CategoryAdapter;
import com.db.DataBaseRlmaxAll;
import com.interfaces.OnBackPressed;
import com.item.ItemCategory;
import com.slsindupotha.R;
import com.slsindupotha.MyApplication;
import com.util.APICallAync;
import com.util.ItemOffsetDecoration;
import com.util.JsonUtils;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class CategoryFragment extends BaseFragment implements OnBackPressed {

    ArrayList<ItemCategory> mListItem;
    public RecyclerView recyclerView;
    CategoryAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    DataBaseRlmaxAll dataBaseRlmaxAll;
    int callBack  = 0;
    boolean myCondition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview, container, false);
        dataBaseRlmaxAll = new DataBaseRlmaxAll(getActivity());
        mListItem = new ArrayList<>();
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0);
        int font=pref.getInt("font",0);
        int mode=pref.getInt("mode",1);
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        lyt_not_found.setBackgroundColor(Color.BLACK);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        int columns = getResources().getInteger(R.integer.number_of_column);
        recyclerView.setHasFixedSize(true);
        if(mode==1){
            recyclerView.setBackgroundColor(Color.BLACK);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));
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

    private void displayData() {
        adapter = new CategoryAdapter(getActivity(), dataBaseRlmaxAll.getCategory());
        recyclerView.setAdapter(adapter);
        final APICallAync apiCallAync =  getApp().getContactsManager();
        callBack =apiCallAync.loadingCat;
        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                    callBack = apiCallAync.loadingCat;
                    handler.postDelayed(this, 2000);
                    adapter.updateList(dataBaseRlmaxAll.getCategory());
                    if (callBack == 3) {
                        adapter.updateList(dataBaseRlmaxAll.getCategory());
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
            lyt_not_found.setVisibility(View.VISIBLE);
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
