package com.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.adapter.CategoryAdapter;
import com.adapter.HomeRecentAdapter;
import com.adapter.StoryAdapter;
import com.db.DataBaseRlmaxAll;
import com.db.DatabaseHelperRecent;
import com.interfaces.OnBackPressed;
import com.item.ItemAbout;
import com.item.ItemCategory;
import com.item.ItemSlider;
import com.item.ItemStory;
import com.slsindupotha.ActivityRecent;
import com.slsindupotha.Add_NewSongs;
import com.slsindupotha.ChatRoom;
import com.slsindupotha.Feedback;
import com.slsindupotha.Luhudu;
import com.slsindupotha.Luhudu_Post_Ad;
import com.slsindupotha.MainActivity;
import com.slsindupotha.R;
import com.slsindupotha.MessageActivity;
import com.slsindupotha.MyApplication;
import com.slsindupotha.SplashActivity;
import com.util.API;
import com.util.APICallAync;
import com.util.EnchantedViewPager;
import com.util.ItemOffsetDecoration;
import com.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.ArrayList;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends BaseFragment implements OnBackPressed {

    TextView tvUpdateDate, version;
    RecyclerView recyclerViewLatest, recyclerViewCat, recyclerViewRecent;
    EnchantedViewPager mViewPager;
    NestedScrollView mScrollView;
    ProgressBar mProgressBar;
    ArrayList<ItemSlider> mSliderList;
    ArrayList<ItemAbout> mListItem;
    CircleIndicator circleIndicator;
    Button btnLatest, btnCategory, btnRecent,btn_check_new,btn_new_msg,btnLuhudu, btnLuhuduPostAd, btnChatroom, btnLottery, btnNewSong, btnUpdate;
    Button btnSync , btnMsg;
    int currentCount = 0;
    ArrayList<ItemStory> mLatestList, mRecent;
    ArrayList<ItemCategory> mCatList;
    CustomViewPagerAdapter mAdapter;
    CategoryAdapter homeCatAdapter;
    StoryAdapter homeLatestAdapter;
    HomeRecentAdapter AdapterRecent;
    LinearLayout lay_main;
    LinearLayout ad_view;
    RelativeLayout lay_cat_rec;
    DatabaseHelperRecent databaseHelperRecent;
    DataBaseRlmaxAll dataBaseRlmaxAll;
    JsonUtils jsonUtils;
    int columnWidth;
    int callBack  = 0;
    int callBackAbout  = 0;
    ItemAbout itemAbout;
    Context ctx;

    static final int REQ_CODE_VERSION_UPDATE = 530;
    InAppUpdateManager inAppUpdateManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        SplashActivity splashActivity = new SplashActivity();

        InAppUpdateManager.Builder((AppCompatActivity) getContext(), REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(Constants.UpdateMode.IMMEDIATE)
                .checkForAppUpdate();

        jsonUtils = new JsonUtils(requireActivity());
        columnWidth = (jsonUtils.getScreenWidth());
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0);
        int font=pref.getInt("font",0);
        int mode=pref.getInt("mode",1);
        mSliderList = new ArrayList<>();
        mLatestList = new ArrayList<>();
        mCatList = new ArrayList<>();
        mRecent = new ArrayList<>();
        databaseHelperRecent = new DatabaseHelperRecent(requireActivity());
        dataBaseRlmaxAll = new DataBaseRlmaxAll(requireActivity());
       // ad_view = rootView.findViewById(R.id.ad_view);
        //BannerAds.ShowBannerAds(requireActivity(), ad_view);
        mListItem = new ArrayList<>();
        mProgressBar = rootView.findViewById(R.id.progressBar);
        mScrollView = rootView.findViewById(R.id.scrollView);
        mViewPager = rootView.findViewById(R.id.viewPager);
        circleIndicator = rootView.findViewById(R.id.indicator_unselected_background);
        lay_main = rootView.findViewById(R.id.lay_main);
        recyclerViewLatest = rootView.findViewById(R.id.rv_latest_video);
        recyclerViewCat = rootView.findViewById(R.id.rv_cat_video);
        recyclerViewRecent = rootView.findViewById(R.id.rv_cat_video_rec);
        lay_cat_rec = rootView.findViewById(R.id.lay_cat_rec);
        tvUpdateDate = rootView.findViewById(R.id.tvUpdateDate);

        tvUpdateDate.setSelected(true);
        if(mode==1){
        recyclerViewCat.setBackgroundColor(Color.BLACK);
        recyclerViewLatest.setBackgroundColor(Color.BLACK);
        recyclerViewRecent.setBackgroundColor(Color.BLACK);}

        btnLuhudu = rootView.findViewById(R.id.btn_luhudu);
        btnLuhuduPostAd = rootView.findViewById(R.id.btn_Post_ad);
        btnChatroom = rootView.findViewById(R.id.btn_ChatRoom);
        btnLottery = rootView.findViewById(R.id.btn_Lottery);
        btnNewSong = rootView.findViewById(R.id.btn_NewSong);
        btnUpdate = rootView.findViewById(R.id.btn_Update);
        btnSync = rootView.findViewById(R.id.btn_sync);
        btnMsg = rootView.findViewById(R.id.btn_Msg);

        version = rootView.findViewById(R.id.textView3);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/nine.otf");
        version.setTypeface(typeface);

        Typeface tp1 = Typeface.createFromAsset(getContext().getAssets(),"fonts/nine.otf");
        btnMsg.setTypeface(tp1);

        Typeface tp2 = Typeface.createFromAsset(getContext().getAssets(),"fonts/nine.otf");
        btnUpdate.setTypeface(tp2);

        Log.d("mytag", "onCreate: Home ");

        btnLatest = rootView.findViewById(R.id.btn_latest_video);
        btnCategory = rootView.findViewById(R.id.btn_cat_video);
        btnRecent = rootView.findViewById(R.id.btn_cat_video_rec);
        btn_new_msg = rootView.findViewById(R.id.btn_new_msg);
        btn_check_new = rootView.findViewById(R.id.btn_check_new);
        recyclerViewLatest.setHasFixedSize(false);
        recyclerViewLatest.setNestedScrollingEnabled(false);
        recyclerViewLatest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerViewLatest.addItemDecoration(itemDecoration);
        recyclerViewCat.setHasFixedSize(false);
        recyclerViewCat.setNestedScrollingEnabled(false);
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCat.addItemDecoration(itemDecoration);
        recyclerViewRecent.setHasFixedSize(false);
        recyclerViewRecent.setNestedScrollingEnabled(false);
        recyclerViewRecent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewRecent.addItemDecoration(itemDecoration);
        if(dataBaseRlmaxAll.getAbout().size() == 0){
            btn_new_msg.setEnabled(false);
            checkStatusAbout();
        }else{
            btn_new_msg.setEnabled(true);
            mListItem = dataBaseRlmaxAll.getAbout();
             itemAbout = mListItem.get(0);
            btn_new_msg.setText("Message (1)");
        }
        btnLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) requireActivity()).highLightNavigation(2, getString(R.string.menu_latest));
                String categoryName = getString(R.string.menu_latest);
                FragmentManager fm = getFragmentManager();
                LatestFragment f1 = new LatestFragment();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Container, f1, categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);

            }
        });
        btn_check_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });
        if(itemAbout != null) {
            btn_new_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent browserIntent = new Intent(getActivity(), MessageActivity.class);
                    browserIntent.putExtra("welcomeMsg", itemAbout.getAppWelcome());
                    startActivity(browserIntent);
                }
            });

            btnMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent browserIntent = new Intent(getActivity(), MessageActivity.class);
                    browserIntent.putExtra("welcomeMsg", itemAbout.getAppWelcome());
                    startActivity(browserIntent);
                }
            });
        }
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openDialog();
                }


        });


        btnLuhudu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(getActivity(), Luhudu.class);
                startActivity(browserIntent);
            }
        });


        btnLuhuduPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(getActivity(), Luhudu_Post_Ad.class);
                startActivity(browserIntent);
            }
        });


        btnChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(getActivity(), ChatRoom.class);
                startActivity(browserIntent);
            }
        });


        btnNewSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(getActivity(), Feedback.class);
                startActivity(browserIntent);
            }
        });


        btnLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "නොමිලේ ලොතරැයි ජයග්‍රහණ සමග තෑගී දිනන්න. මෙම පහසුකම ලගදීම බලාපොරොත්තු වන්න !",
                        Toast.LENGTH_LONG).show();
            }
        });




        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Please Wait..!!!",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.slsindupotha"));
                startActivity(intent);
            }
        });



        if(getApp().getSettings().getDBDate().length()>0){
            tvUpdateDate.setText(getApp().getSettings().getDBDate());
            btn_check_new.setEnabled(true);
            btn_check_new.setText("Check New Songs");
        }else{
            tvUpdateDate.setText("Please wait...");
            tvUpdateDate.setTextColor(Color.GREEN);
            btn_check_new.setEnabled(false);
            btn_check_new.setText("Updating...");
            checkStatus();
            showToast();


        }

            btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) requireActivity()).highLightNavigation(1, getString(R.string.menu_category));
                String categoryName = getString(R.string.menu_category);
                FragmentManager fm = getFragmentManager();
                CategoryFragment f1 = new CategoryFragment();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Container, f1, categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);

            }
        });

        btnRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_rec = new Intent(requireActivity(), ActivityRecent.class);
                startActivity(intent_rec);
            }
        });

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "get_home");
        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            setHomeVideo();
         }else{
            setHomeVideo();
        }

        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, columnWidth / 2));
        mViewPager.useScale();
        mViewPager.removeAlpha();
        return rootView;


    }





    private  void checkStatusAbout(){
        final APICallAync apiCallAync =  getApp().getContactsManager();
        callBackAbout =apiCallAync.loadingAbout;
        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                callBackAbout =apiCallAync.loadingAbout;
               // Log.d("checkAa",""+callBackAbout);
                handler.postDelayed(this, 2000);

                if(callBackAbout == 1) {
                    handler.removeCallbacks(this);
                    mListItem = dataBaseRlmaxAll.getAbout();
                    itemAbout = mListItem.get(0);
                    btn_new_msg.setEnabled(true);
                    btn_new_msg.setText("Message (1)");
                }
            }

        };
        if(callBackAbout == 0 || callBackAbout == 2) {
           // Log.d("checkAa",""+callBackAbout);
            handler.post(task);
           // btn_new_msg.setEnabled(false);
        }
    }



    public void openDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_custom);
        TextView cancel_btn=dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
        TextView ok_btn=dialog.findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if (JsonUtils.isNetworkAvailable(requireActivity())) {
                    if(getApp().getContactsManager().loadingCat == 3){
                        getApp().getContactsManager().loadingCat = 0;
                    }
                    btn_check_new.setEnabled(false);
                    checkStatus();
                    showToast();

                    getApp().getContactsManager().getStoryAyncManully();
                } else {
                    showToast2();
                }
                dialog.dismiss();
            }
        });
        dialog.show();


    }



    public void showToast(){
            final Toast toast=Toast.makeText(getApp().getApplicationContext(),"කරුණාකර මොහොතක් රැඳී සිටින්න.!!! සියලුම අලුත් ගීත ඔබගේ දුරකථනයට ලැබෙමින් පවතී.. (මේ සඳහා විනාඩි 2ක ට ආසන්න කාලයක් ගත වේ.)",Toast.LENGTH_LONG);
            View toastView = toast.getView();
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);

//            TextView toastMessage = toastView.findViewById(android.R.id.message);
//            toastMessage.setTextSize(25);
//            toastMessage.setTextColor(Color.YELLOW);
//            //toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
//            toastMessage.setGravity(Gravity.CENTER);
//            toastMessage.setCompoundDrawablePadding(16);
//            toastView.setBackgroundColor(Color.BLUE);
//            toast.show();

    }


    public void showToast2(){


        final Toast toast=Toast.makeText(getApp().getApplicationContext(),"කරුණාකර DATA / INTERNET ON කරන්න.!!!",Toast.LENGTH_LONG);
        View toastView = toast.getView();
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);

        TextView toastMessage = toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(25);
        toastMessage.setTextColor(Color.YELLOW);
        //toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(16);
        toastView.setBackgroundColor(Color.BLUE);
        toast.show();

    }






    private  void checkStatus(){
        final APICallAync apiCallAync =  getApp().getContactsManager();
        callBack = apiCallAync.loadingCat;
        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                callBack = apiCallAync.loadingCat;
                handler.postDelayed(this, 2000);
                btn_check_new.setText("Updating ...");
                tvUpdateDate.setText("Please wait...");
                tvUpdateDate.setTextColor(Color.GREEN);
                if(callBack == 3) {
                    try {
                        tvUpdateDate.setText(getApp().getSettings().getDBDate());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    btn_check_new.setEnabled(true);
                    tvUpdateDate.setTextColor(Color.BLACK);
                    btn_check_new.setText("Check New Songs");
                    handler.removeCallbacks(this);
                }
            }

        };
        if(callBack ==0 || callBack == 2 ) {
            handler.post(task);
        }
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    private class CustomViewPagerAdapter extends PagerAdapter {
        private final LayoutInflater inflater;

        private CustomViewPagerAdapter() {
            // TODO Auto-generated constructor stub
            inflater = requireActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mSliderList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View imageLayout = inflater.inflate(R.layout.slider_adapter, container, false);
            assert imageLayout != null;

            RoundedImageView image = imageLayout.findViewById(R.id.imageView_slider_adapter);
            CardView cardView = imageLayout.findViewById(R.id.card_view);

            Glide.with(requireActivity()).load(mSliderList.get(position).getSliderImageUrl()).into(image);
            imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSliderList.get(position).getSliderName()));
                    startActivity(browserIntent);
                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void autoPlay(final ViewPager viewPager) {

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mAdapter != null && viewPager.getAdapter().getCount() > 0) {
                        int position = currentCount % mAdapter.getCount();
                        currentCount++;
                        viewPager.setCurrentItem(position);

                        autoPlay(viewPager);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "auto scroll pager error.", e);
                }
            }
        }, 2500);
    }

    private void setHomeVideo() {
            mSliderList = dataBaseRlmaxAll.getSlider();
        if (!mSliderList.isEmpty()) {
            mAdapter = new CustomViewPagerAdapter();
            mViewPager.setAdapter(mAdapter);
            circleIndicator.setViewPager(mViewPager);
            autoPlay(mViewPager);
        }


        if (getActivity() != null) {
            homeLatestAdapter = new StoryAdapter(getActivity(), dataBaseRlmaxAll.getStoryLatest());
            recyclerViewLatest.setAdapter(homeLatestAdapter);
        }

        if (getActivity() != null) {
            homeCatAdapter = new CategoryAdapter(getActivity(), dataBaseRlmaxAll.getCategoryLatest());

            Log.d("mytag", "setHomeVideo:  111");
            recyclerViewCat.setAdapter(homeCatAdapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mRecent = databaseHelperRecent.getFavourite();
        displayDataRecent();
        ((MainActivity) requireActivity()).setToolbarTitle(getString(R.string.menu_home));
    }

    private void displayDataRecent() {

        if (mRecent.size() >= 2) {
            lay_cat_rec.setVisibility(View.VISIBLE);
            recyclerViewRecent.setVisibility(View.VISIBLE);
        } else {
            lay_cat_rec.setVisibility(View.GONE);
            recyclerViewRecent.setVisibility(View.GONE);
        }

        AdapterRecent = new HomeRecentAdapter(getActivity(), mRecent);
        recyclerViewRecent.setAdapter(AdapterRecent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                inAppUpdateManager.checkForAppUpdate();

                Log.d("mytag" ,"Update flow failed! Result code: " + resultCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}