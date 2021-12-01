package com.slsindupotha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.util.JsonUtils;

public class Setting extends BaseActivity {

    TextView t1,t3,tvUpdateDate,tvSync,tvDatabaseText;
    SeekBar seek;
    Switch swt;
    int font=0;
    int dark;
    Toolbar toolbar;
    int callBack  = 0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.RobotoTextViewStyle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
        seek=(SeekBar)findViewById(R.id.seekBar);
        swt=(Switch)findViewById(R.id.switch1);
        t1=(TextView)findViewById(R.id.fontsize);
        tvUpdateDate = findViewById(R.id.tvUpdateDate);
        tvDatabaseText = findViewById(R.id.tvDatabaseText);
        tvSync = findViewById(R.id.tvSync);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        font=pref.getInt("font",50);
        dark=pref.getInt("mode",1);

        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );

        if(dark==1)
            swt.setChecked(true);

        if(dark==1){
            setActivityBackgroundColor(R.color.black);
            t1.setTextColor(Color.WHITE);
            tvDatabaseText.setTextColor(Color.WHITE);
            tvUpdateDate.setTextColor(Color.WHITE);
            seek.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            seek.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            swt.setTextColor(Color.WHITE);
        }
        else {
            seek.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            seek.getThumb().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }
        seek.setMax(100);
        //seek.setMin(0);

        seek.setProgress(font);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                font=progress;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("font",font);
                editor.putInt("mode",dark);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Font Size: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dark=1;
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("font",font);
                    editor.putInt("mode",dark);
                    editor.commit();
                    Toast.makeText(Setting.this, " Dark mode On", Toast.LENGTH_SHORT).show();
                    setActivityBackgroundColor(R.color.black);
                    t1.setTextColor(Color.WHITE);
                    tvDatabaseText.setTextColor(Color.WHITE);
                    tvUpdateDate.setTextColor(Color.WHITE);
                    seek.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    seek.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    swt.setTextColor(Color.WHITE);
                } else {
                    dark=0;
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("font",font);
                    editor.putInt("mode",dark);
                    editor.commit();
                    Toast.makeText(Setting.this, "Dark Mode Off", Toast.LENGTH_SHORT).show();
                    setActivityBackgroundColor(Color.WHITE);
                    t1.setTextColor(Color.BLACK);
                    tvDatabaseText.setTextColor(Color.BLACK);
                    tvUpdateDate.setTextColor(Color.BLACK);
                    seek.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    seek.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    swt.setTextColor(Color.BLACK);
                }
            }
        });

        if(myApplication.getSettings().getDBDate().length()>0){

            tvUpdateDate.setText(myApplication.getSettings().getDBDate());
            tvSync.setEnabled(true);
            tvSync.setText("Check New Songs");
        }else{
            tvUpdateDate.setText("Please wait...");
            tvSync.setEnabled(false);
            tvSync.setText("Updating...");
        }

        if (JsonUtils.isNetworkAvailable(Setting.this)) {
            tvSync.setVisibility(View.VISIBLE);
        }else{
            tvSync.setVisibility(View.GONE);
        }

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
    @Override
    public void onBackPressed() {
//        Intent in=new Intent(Setting.this,MainActivity.class);
//        startActivity(in);
//        finish();
        onBackPressed();
    }
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.tvSync){
            if(myApplication.getContactsManager().loadingCat == 3){
                myApplication.getContactsManager().loadingCat = 0;
            }
            tvSync.setEnabled(false);
            checkStatus();
            myApplication.getContactsManager().getStoryAyncManully();

        }

    }

    private  void checkStatus(){
        callBack = myApplication.getContactsManager().loadingCat;
        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                callBack = myApplication.getContactsManager().loadingCat;
                handler.postDelayed(this, 2000);
                Log.d("check :" , String.valueOf(callBack));
                tvUpdateDate.setText("Please wait...");
                tvSync.setText("Updating...");
                if(callBack == 3) {
                    tvSync.setEnabled(true);
                    tvSync.setText("Check New Songs");
                    tvUpdateDate.setText(myApplication.getSettings().getDBDate());
                    handler.removeCallbacks(this);
                }
            }

        };
        if(callBack ==0 || callBack == 2 ) {
            handler.post(task);

        }
    }
}
