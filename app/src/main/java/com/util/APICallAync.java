package com.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.db.DataBaseRlmaxAll;
import com.slsindupotha.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.item.ItemAbout;
import com.item.ItemCategory;
import com.item.ItemSlider;
import com.item.ItemStory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by RLMAX
 */


public class APICallAync {


    public int loadingCat = 0;
    public int loadingAbout = 0;
    private boolean loadingStory = false;
    private static APICallAync apiCallAync;
    private Settings settings;
    private DataBaseRlmaxAll dataBaseRlmaxAll;


    public static APICallAync getInstance(Context context) {
        if (apiCallAync == null) {
            apiCallAync = new APICallAync(context);
        }
        return apiCallAync;
    }

    public int loading(){
        return loadingCat;
    }

    public APICallAync(Context applicationContext) {

        dataBaseRlmaxAll = new DataBaseRlmaxAll(applicationContext.getApplicationContext());
        settings = new Settings(applicationContext.getApplicationContext());
    }

    public void getCatAync(){
        new getCategory().execute(Constant.API_URL);
    }

    public void getStoryAync(){
        new GetStory().execute(Constant.API_URL);
    }

    public void getCatAyncManully(){
        new getCategoryManual().execute(Constant.API_URL);
    }

    public void getStoryAyncManully(){
        new GetStoryManual().execute(Constant.API_URL);
    }

    public void getSliderAync(String base64){
        new homeSlider(base64).execute(Constant.API_URL);
    }
    public void getAboutAync(String base64){
        new MyTaskAbout(base64).execute(Constant.API_URL);
    }


    @SuppressLint("StaticFieldLeak")
    public class getCategory extends AsyncTask<String, Void, String> {

        private getCategory() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            int b = 0;
            if(null == dataBaseRlmaxAll.getCateCount()){
              b = 0;
            }else{
                b = Integer.parseInt(dataBaseRlmaxAll.getCateCount());
            }
            int a = settings.getCat();
            String result = null;

            for(int c = 0; a>b; c++){
                JsonObject jsObj3 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj3.addProperty("method_name", "get_category_new");
                jsObj3.addProperty("a_limit",""+b);
                jsObj3.addProperty("b_limit",""+a);
                String bb =  API.toBase64(jsObj3.toString());
                result  = JsonUtils.getJSONString(params[0],bb);
                if (null == result || result.length() == 0) {
                    // lyt_not_found.setVisibility(View.VISIBLE);
                } else {
                    try {
                        JSONObject mainJson = new JSONObject(result);
                        JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                        JSONObject objJson;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objJson = jsonArray.getJSONObject(i);
                            ItemCategory objItem = new ItemCategory();
                            objItem.setCategoryId(objJson.getString(Constant.CATEGORY_CID));
                            objItem.setCategoryName(objJson.getString(Constant.CATEGORY_NAME));
                            objItem.setCategoryImage(objJson.getString(Constant.CATEGORY_IMAGE));
                            dataBaseRlmaxAll.addCategoryOrUpdate(objItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                b = Integer.parseInt(dataBaseRlmaxAll.getCateCount());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null == result || result.length() == 0) {

            } else {
              //  Log.d("CheckLocalDB",dataBaseRlmaxAll.getCateCount());
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class GetStory extends AsyncTask<String, Integer, String> {


        public  GetStory() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingCat = 2;
            if(settings.getCat() != Integer.parseInt(dataBaseRlmaxAll.getCateCount())){
                getCatAync();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            int b = 0;
            if(dataBaseRlmaxAll.getStoryCount().equals("0")){
                b = 0;
            }else{
                b = Integer.parseInt(dataBaseRlmaxAll.getStoryCount());
            }
            int a = settings.getStory();
            String result = null;

            for(int c = 0; a>b; c++){
            JsonObject jsObj3 = (JsonObject) new Gson().toJsonTree(new API());
            jsObj3.addProperty("method_name", "get_latest_stories_new");
//            jsObj3.addProperty("a_limit",""+b);
//            jsObj3.addProperty("b_limit",""+a);
            String bb =  API.toBase64(jsObj3.toString());
                result  = JsonUtils.getJSONString(params[0],bb);
            if (null == result || result.length() == 0) {

            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemStory objItem = new ItemStory();
                        objItem.setId(Integer.parseInt(objJson.getString(Constant.STORY_ID)));
                        objItem.setCatId(objJson.getString(Constant.CATEGORY_CID));
                        objItem.setStoryDate(objJson.getString(Constant.STORY_DATE));
                        objItem.setStoryDescription(objJson.getString(Constant.STORY_DESC));
                        objItem.setStoryTitle(objJson.getString(Constant.STORY_TITLE));
                        objItem.setStoryViews(objJson.getString(Constant.STORY_VIEW));
                        objItem.setstoryImage(objJson.getString(Constant.STORY_IMAGE));
                        dataBaseRlmaxAll.addStoryOrUpdate(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

              b = Integer.parseInt(dataBaseRlmaxAll.getStoryCount());
            }
            if(a<b){
                JsonObject jsObj3 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj3.addProperty("method_name", "get_latest_stories_new");
                String bb =  API.toBase64(jsObj3.toString());
                result  = JsonUtils.getJSONString(params[0],bb);
                if (null == result || result.length() == 0) {

                } else {
                    try {
                        JSONObject mainJson = new JSONObject(result);
                        JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                        JSONObject objJson;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objJson = jsonArray.getJSONObject(i);
                            ItemStory objItem = new ItemStory();
                            objItem.setId(Integer.parseInt(objJson.getString(Constant.STORY_ID)));
                            objItem.setCatId(objJson.getString(Constant.CATEGORY_CID));
                            objItem.setStoryDate(objJson.getString(Constant.STORY_DATE));
                            objItem.setStoryDescription(objJson.getString(Constant.STORY_DESC));
                            objItem.setStoryTitle(objJson.getString(Constant.STORY_TITLE));
                            objItem.setStoryViews(objJson.getString(Constant.STORY_VIEW));
                            objItem.setstoryImage(objJson.getString(Constant.STORY_IMAGE));
                            dataBaseRlmaxAll.deleteStory(objItem);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

               // b = Integer.parseInt(dataBaseRlmaxAll.getStoryCount());

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null == result || result.length() == 0) {

            } else {
                loadingCat = 3;
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                String formattedDate = df.format(c);
                settings.setDBDate(formattedDate);
               // Log.d("CheckLocalDB",dataBaseRlmaxAll.getStoryCount());
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }
    @SuppressLint("StaticFieldLeak")
    private class homeSlider extends AsyncTask<String, Void, String> {

        String base64;

        private homeSlider(String base64) {
            this.base64 = base64;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataBaseRlmaxAll.clearSliderAll();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null == result || result.length() == 0) {

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONObject mainJsonSlider = mainJson.getJSONObject(Constant.ARRAY_NAME);
                    JSONArray jsonArray = mainJsonSlider.getJSONArray("slider_list");
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemSlider objItem = new ItemSlider();
                        objItem.setSliderId(objJson.getString(Constant.SLIDER_ID));
                        objItem.setSliderImageUrl(objJson.getString(Constant.SLIDER_IMAGE));
                        objItem.setSliderName(objJson.getString(Constant.SLIDER_LINK));
                        dataBaseRlmaxAll.addSliderOrUpdate(objItem);
                    }
                    JSONArray jsonArrayCat = mainJsonSlider.getJSONArray("categories");
                    JSONObject objJsonCat;
                    for (int i = 0; i < jsonArrayCat.length(); i++) {
                        objJsonCat = jsonArrayCat.getJSONObject(i);

                        ItemCategory objItem = new ItemCategory();
                        objItem.setCategoryId(objJsonCat.getString(Constant.CATEGORY_CID));
                        objItem.setCategoryImage(objJsonCat.getString(Constant.CATEGORY_IMAGE));
                        objItem.setCategoryName(objJsonCat.getString(Constant.CATEGORY_NAME));
                        dataBaseRlmaxAll.addCategoryOrUpdate(objItem);
                    }

                    JSONArray jsonArrayLatest = mainJsonSlider.getJSONArray("latest_stories");
                    JSONObject objJsonLatest;
                    for (int i = 0; i < jsonArrayLatest.length(); i++) {
                        objJsonLatest = jsonArrayLatest.getJSONObject(i);

                        ItemStory objItem = new ItemStory();
                        objItem.setId(Integer.parseInt(objJsonLatest.getString(Constant.STORY_ID)));
                        objItem.setCatId(objJsonLatest.getString(Constant.CATEGORY_CID));
                        objItem.setStoryDate(objJsonLatest.getString(Constant.STORY_DATE));
                        objItem.setStoryDescription(objJsonLatest.getString(Constant.STORY_DESC));
                        objItem.setStoryTitle(objJsonLatest.getString(Constant.STORY_TITLE));
                        objItem.setStoryViews(objJsonLatest.getString(Constant.STORY_VIEW));
                        objItem.setstoryImage(objJsonLatest.getString(Constant.STORY_IMAGE));
                        dataBaseRlmaxAll.addStoryOrUpdate(objItem);

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //// Manually SYNC

    @SuppressLint("StaticFieldLeak")
    public class getCategoryManual extends AsyncTask<String, Void, String> {

        private getCategoryManual() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            int b = 0;
            if(null == dataBaseRlmaxAll.getCateCount()){
                b = 0;
            }else{
              //  b = Integer.parseInt(dataBaseRlmaxAll.getCateCount());
            }
            int a = settings.getCat();
            String result = null;

            for(int c = 0; a>b; c++){
                JsonObject jsObj3 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj3.addProperty("method_name", "get_category_new");
                jsObj3.addProperty("a_limit",""+0);
                jsObj3.addProperty("b_limit",""+a);
                String bb =  API.toBase64(jsObj3.toString());
                result  = JsonUtils.getJSONString(params[0],bb);
                if (null == result || result.length() == 0) {
                    // lyt_not_found.setVisibility(View.VISIBLE);
                } else {
                    try {
                        JSONObject mainJson = new JSONObject(result);
                        JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                        JSONObject objJson;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objJson = jsonArray.getJSONObject(i);
                            ItemCategory objItem = new ItemCategory();
                            objItem.setCategoryId(objJson.getString(Constant.CATEGORY_CID));
                            objItem.setCategoryName(objJson.getString(Constant.CATEGORY_NAME));
                            objItem.setCategoryImage(objJson.getString(Constant.CATEGORY_IMAGE));
                            dataBaseRlmaxAll.addCategoryOrUpdate(objItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                b = Integer.parseInt(dataBaseRlmaxAll.getCateCount());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null == result || result.length() == 0) {

            } else {
                //  Log.d("CheckLocalDB",dataBaseRlmaxAll.getCateCount());
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class GetStoryManual extends AsyncTask<String, Integer, String> {


        public  GetStoryManual() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingCat = 2;
           // if(settings.getCat() != Integer.parseInt(dataBaseRlmaxAll.getCateCount())){
                getCatAyncManully();
           // }

        }

        @Override
        protected String doInBackground(String... params) {
            int b = 0;
            if(dataBaseRlmaxAll.getStoryCount().equals("0")){
                b = 0;
            }else{
               // b = Integer.parseInt(dataBaseRlmaxAll.getStoryCount());
            }
            int a = settings.getStory();
            String result = null;

            for(int c = 0; a>b; c++){
                JsonObject jsObj3 = (JsonObject) new Gson().toJsonTree(new API());
                jsObj3.addProperty("method_name", "get_latest_stories_new");
                jsObj3.addProperty("a_limit",""+0);
                jsObj3.addProperty("b_limit",""+a);
                String bb =  API.toBase64(jsObj3.toString());
                result  = JsonUtils.getJSONString(params[0],bb);
                if (null == result || result.length() == 0) {

                } else {
                    try {
                        JSONObject mainJson = new JSONObject(result);
                        JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                        JSONObject objJson;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objJson = jsonArray.getJSONObject(i);
                            ItemStory objItem = new ItemStory();
                            objItem.setId(Integer.parseInt(objJson.getString(Constant.STORY_ID)));
                            objItem.setCatId(objJson.getString(Constant.CATEGORY_CID));
                            objItem.setStoryDate(objJson.getString(Constant.STORY_DATE));
                            objItem.setStoryDescription(objJson.getString(Constant.STORY_DESC));
                            objItem.setStoryTitle(objJson.getString(Constant.STORY_TITLE));
                            objItem.setStoryViews(objJson.getString(Constant.STORY_VIEW));
                            objItem.setstoryImage(objJson.getString(Constant.STORY_IMAGE));
                            dataBaseRlmaxAll.addStoryOrUpdate(objItem);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                b = Integer.parseInt(dataBaseRlmaxAll.getStoryCount());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null == result || result.length() == 0) {

            } else {
                loadingCat = 3;
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                String formattedDate = df.format(c);
                settings.setDBDate(formattedDate);
                // Log.d("CheckLocalDB",dataBaseRlmaxAll.getStoryCount());
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTaskAbout extends AsyncTask<String, Void, String> {

        String base64;

        private MyTaskAbout(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingAbout = 2;
         //   mProgressBar.setVisibility(View.VISIBLE);
           // mScrollView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
          //  mProgressBar.setVisibility(View.GONE);
          //  mScrollView.setVisibility(View.VISIBLE);
            if (null == result || result.length() == 0) {
           //     showToast(getString(R.string.no_data));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemAbout itemAbout = new ItemAbout();
                        itemAbout.setAppName(objJson.getString(Constant.APP_NAME));
                        itemAbout.setAppLogo(objJson.getString(Constant.APP_IMAGE));
                        itemAbout.setAppVersion(objJson.getString(Constant.APP_VERSION));
                        itemAbout.setAppAuthor(objJson.getString(Constant.APP_AUTHOR));
                        itemAbout.setAppEmail(objJson.getString(Constant.APP_EMAIL));
                        itemAbout.setAppWebsite(objJson.getString(Constant.APP_WEBSITE));
                        itemAbout.setAppContact(objJson.getString(Constant.APP_CONTACT));
                        itemAbout.setAppDescription(objJson.getString(Constant.APP_DESC));
                        itemAbout.setAppWelcome(objJson.getString(Constant.APP_WELCOME));
                       dataBaseRlmaxAll.addAboutOrUpdate(itemAbout);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadingAbout = 1;
            }
        }
    }
}
