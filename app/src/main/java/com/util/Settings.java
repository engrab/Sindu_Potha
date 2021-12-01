package com.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by RLMAX
 */


public class Settings {

    private final String PREF_NAME = "com.utils.settings.a";
    private Context context;

    public Settings(Context context) {
        this.context = context;
    }

    private SharedPreferences getPref() {
        return context.getSharedPreferences(PREF_NAME, 0);
    }

    public void setCat(int value) {
        getPref().edit().putInt("CatCount", value).commit();
    }

    public int getCat() {
        int value = getPref().getInt("CatCount", 0);
        return value;
    }

    public void setStory(int value) {
        getPref().edit().putInt("StoryCount", value).commit();
    }

    public int getStory() {
        int value = getPref().getInt("StoryCount", 0);
        return value;
    }

    public void setSlider(int value) {
        getPref().edit().putInt("SliderCount", value).commit();
    }

    public int getSlider() {
        int value = getPref().getInt("SliderCount", 0);
        return value;
    }

    public void setDate(String value) {
        getPref().edit().putString("SetDate", value).commit();
    }

    public String getDate() {
        String value = getPref().getString("SetDate", "");
        return value;
    }

    public void setDBDate(String value) {
        getPref().edit().putString("SetDBDate", value).commit();
    }

    public String getDBDate() {
        String value = getPref().getString("SetDBDate", "");
        return value;
    }

}
