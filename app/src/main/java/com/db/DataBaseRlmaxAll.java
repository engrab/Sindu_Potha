package com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.item.ItemAbout;
import com.item.ItemCategory;
import com.item.ItemSlider;
import com.item.ItemStory;

import java.util.ArrayList;


/**
 * Created by RLMAX
 */

public class DataBaseRlmaxAll extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "Rlmax.db";
    private static DataBaseRlmaxAll instance = null;

    public DataBaseRlmaxAll(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS item_category (_id INTEGER PRIMARY KEY AUTOINCREMENT,catId TEXT, catName TEXT, catImageUrl TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS item_story (_id INTEGER PRIMARY KEY AUTOINCREMENT,storyId INTEGER,catId TEXT, storyTitle TEXT," +
                " storyDescription TEXT,storyDate TEXT ,storyViews TEXT,storyImage TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS item_slider (_id INTEGER PRIMARY KEY AUTOINCREMENT,SliderId TEXT, SliderName TEXT, SliderImageUrl TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS item_about (_id INTEGER PRIMARY KEY AUTOINCREMENT,appName TEXT, appLogo TEXT, appVersion TEXT, appAuthor TEXT, appContact TEXT, appEmail TEXT, appWebsite TEXT, appDescription TEXT,appWelcome TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        dropAllTables(database);
        onCreate(database);
    }

    private void dropAllTables(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS item_category");
        database.execSQL("DROP TABLE IF EXISTS item_story");
        database.execSQL("DROP TABLE IF EXISTS item_slider");
        database.execSQL("DROP TABLE IF EXISTS item_about");

    }

    public synchronized void addCategoryOrUpdate(ItemCategory item) {
        ItemCategory categoryExist = getItemCategoryId(item.getCategoryId());
        if (categoryExist != null) {
            updateCategory(categoryExist.getCategoryId(), item);
            //  Log.d("categoryExist :" , categoryExist.getCategoryId());
        } else {
            addItemCategory(item);
        }
    }

    public synchronized void addStoryOrUpdate(ItemStory item) {
        ItemStory storyExist = getItemStoryId(item.getId());
        if (storyExist != null) {
            updateStory(storyExist.getId(), item);
            // Log.d("storyExist :" , storyExist.getId());
        } else {
            addItemStory(item);
        }
    }

    public synchronized void addSliderOrUpdate(ItemSlider item) {
        ItemSlider sliderExist = getItemSliderId(item.getSliderId());
        if (sliderExist != null) {
            updateSlider(sliderExist.getSliderId(), item);
            //  Log.d("categoryExist :" , sliderExist.getCategoryId());
        } else {
            addItemSlider(item);
        }
    }

    public synchronized void addAboutOrUpdate(ItemAbout item) {
        ItemAbout aboutExist = getItemAboutId(item.getAppEmail());
        if (aboutExist != null) {
            updateAbout(aboutExist.getId(), item);
            //  Log.d("categoryExist :" , sliderExist.getCategoryId());
        } else {
            addIAbout(item);
        }
    }

    public void deleteStory(ItemStory itemStory) {
        int checkTable = Integer.parseInt(getStoryCount());
        ArrayList<ItemStory> arrayList = getStory(checkTable, 0);
        for (int w = 0; w < checkTable; w++) {
            try {
                ItemStory ss = arrayList.get(w);
                if (ss.getId() != itemStory.getId()) {
                    deleteStroyItem(ss.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void deleteStroyItem(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String whereClause = "storyId=?";
        String whereArgs[] = {String.valueOf(id)};
        database.delete("item_story", whereClause, whereArgs);
        // database.execSQL("DELETE FROM  WHERE storyId = '" + id + "' ");
        database.close();
    }

    private synchronized void addItemCategory(ItemCategory item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("catId", item.getCategoryId());
        contentValues.put("catName", item.getCategoryName());
        contentValues.put("catImageUrl", item.getCategoryImage());
        database.insert("item_category", null, contentValues);
    }

    private synchronized void addItemStory(ItemStory item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("storyId", item.getId());
        contentValues.put("catId", item.getCatId());
        contentValues.put("storyTitle", item.getStoryTitle());
        contentValues.put("storyDescription", item.getStoryDescription());
        contentValues.put("storyDate", item.getStoryDate());
        contentValues.put("storyViews", item.getStoryViews());
        contentValues.put("storyImage", item.getstoryImage());
        database.insert("item_story", null, contentValues);
    }

    private synchronized void addItemSlider(ItemSlider item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SliderId", item.getSliderId());
        contentValues.put("SliderName", item.getSliderName());
        contentValues.put("SliderImageUrl", item.getSliderImageUrl());
        database.insert("item_slider", null, contentValues);
    }

    private synchronized void addIAbout(ItemAbout item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("appName", item.getAppName());
        contentValues.put("appLogo", item.getAppLogo());
        contentValues.put("appVersion", item.getAppVersion());
        contentValues.put("appAuthor", item.getAppAuthor());
        contentValues.put("appContact", item.getAppContact());
        contentValues.put("appEmail", item.getAppEmail());
        contentValues.put("appWebsite", item.getAppWebsite());
        contentValues.put("appDescription", item.getAppDescription());
        contentValues.put("appWelcome", item.getAppWelcome());
        database.insert("item_about", null, contentValues);
    }

    private synchronized void updateCategory(String id, ItemCategory item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("catId", item.getCategoryId());
        contentValues.put("catName", item.getCategoryName());
        contentValues.put("catImageUrl", item.getCategoryImage());
        String query = "catId = " + id;
        database.update("item_category", contentValues, query, null);
        database.close();
    }

    private synchronized void updateStory(int id, ItemStory item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("storyId", item.getId());
        contentValues.put("catId", item.getCatId());
        contentValues.put("storyTitle", item.getStoryTitle());
        contentValues.put("storyDescription", item.getStoryDescription());
        contentValues.put("storyDate", item.getStoryDate());
        contentValues.put("storyViews", item.getStoryViews());
        contentValues.put("storyImage", item.getstoryImage());
        String query = "storyId = " + id;
        database.update("item_story", contentValues, query, null);
    }

    private synchronized void updateSlider(String id, ItemSlider item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SliderId", item.getSliderId());
        contentValues.put("SliderName", item.getSliderName());
        contentValues.put("SliderImageUrl", item.getSliderImageUrl());
        String query = "SliderId = " + id;
        database.update("item_slider", contentValues, query, null);
    }

    private synchronized void updateAbout(int id, ItemAbout item) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("appName", item.getAppName());
        contentValues.put("appLogo", item.getAppLogo());
        contentValues.put("appVersion", item.getAppVersion());
        contentValues.put("appAuthor", item.getAppAuthor());
        contentValues.put("appContact", item.getAppContact());
        contentValues.put("appEmail", item.getAppEmail());
        contentValues.put("appWebsite", item.getAppWebsite());
        contentValues.put("appDescription", item.getAppDescription());
        contentValues.put("appWelcome", item.getAppWelcome());
        String query = "_id = " + id;
        database.update("item_about", contentValues, query, null);
    }


    public ItemCategory getItemCategoryId(String id) {
        ItemCategory itemCategory = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from item_category where catId = '" + id + "'", null);
            if (cursor.moveToNext()) {
                itemCategory = new ItemCategory();
                itemCategory.setCategoryId(cursor.getString(cursor.getColumnIndex("catId")));

            }
            cursor.close();
            //  database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemCategory;
    }

    public ItemStory getItemStoryId(int id) {
        ItemStory itemStory = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from item_story where storyId = '" + id + "'", null);
            if (cursor.moveToNext()) {
                itemStory = new ItemStory();
                itemStory.setId(cursor.getInt(cursor.getColumnIndex("storyId")));

            }
            cursor.close();
            //database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemStory;
    }

    public ItemStory getItemStoryIdAllData(int id) {
        ItemStory itemStory = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from item_story where storyId = '" + id + "'", null);
            if (cursor.moveToNext()) {
                itemStory = new ItemStory();
                itemStory.setId(cursor.getInt(cursor.getColumnIndex("storyId")));
                itemStory.setStoryTitle(cursor.getString(cursor.getColumnIndex("storyTitle")));
                itemStory.setStoryDescription(cursor.getString(cursor.getColumnIndex("storyDescription")));
                itemStory.setStoryDate(cursor.getString(cursor.getColumnIndex("storyDate")));
                itemStory.setStoryViews(cursor.getString(cursor.getColumnIndex("storyViews")));
                itemStory.setstoryImage(cursor.getString(cursor.getColumnIndex("storyImage")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemStory;
    }

    public ItemSlider getItemSliderId(String id) {
        ItemSlider itemSlider = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from item_slider where SliderId = '" + id + "'", null);
            if (cursor.moveToNext()) {
                itemSlider = new ItemSlider();
                itemSlider.setSliderId(cursor.getString(cursor.getColumnIndex("SliderId")));

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemSlider;
    }

    public ItemAbout getItemAboutId(String id) {
        ItemAbout itemCategory = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from item_about where appEmail = '" + id + "'", null);
            if (cursor.moveToNext()) {
                itemCategory = new ItemAbout();
                itemCategory.setId(cursor.getInt(cursor.getColumnIndex("_id")));

            }
            cursor.close();
            //  database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemCategory;
    }

    public ArrayList<ItemCategory> getCategory() {
        ArrayList<ItemCategory> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from item_category", null);
            while (cursor.moveToNext()) {
                ItemCategory user = new ItemCategory();
                user.setCategoryId(cursor.getString(cursor.getColumnIndex("catId")));
                user.setCategoryName(cursor.getString(cursor.getColumnIndex("catName")));
                user.setCategoryImage(cursor.getString(cursor.getColumnIndex("catImageUrl")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public ArrayList<ItemCategory> getCategoryLatest() {
        ArrayList<ItemCategory> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from item_category ORDER BY random() LIMIT 5", null);
            while (cursor.moveToNext()) {
                ItemCategory user = new ItemCategory();
                user.setCategoryId(cursor.getString(cursor.getColumnIndex("catId")));
                user.setCategoryName(cursor.getString(cursor.getColumnIndex("catName")));
                user.setCategoryImage(cursor.getString(cursor.getColumnIndex("catImageUrl")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public ArrayList<ItemAbout> getAbout() {
        ArrayList<ItemAbout> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.enableWriteAheadLogging();
            Cursor cursor = database.rawQuery("select * from item_about", null);
            while (cursor.moveToNext()) {
                ItemAbout user = new ItemAbout();
                user.setAppName(cursor.getString(cursor.getColumnIndex("appName")));
                user.setAppLogo(cursor.getString(cursor.getColumnIndex("appLogo")));
                user.setAppVersion(cursor.getString(cursor.getColumnIndex("appVersion")));
                user.setAppAuthor(cursor.getString(cursor.getColumnIndex("appAuthor")));
                user.setAppContact(cursor.getString(cursor.getColumnIndex("appContact")));
                user.setAppEmail(cursor.getString(cursor.getColumnIndex("appEmail")));
                user.setAppWebsite(cursor.getString(cursor.getColumnIndex("appWebsite")));
                user.setAppDescription(cursor.getString(cursor.getColumnIndex("appDescription")));
                user.setAppWelcome(cursor.getString(cursor.getColumnIndex("appWelcome")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public ArrayList<ItemStory> getStory(int min, int max) {
        ArrayList<ItemStory> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from item_story WHERE _id > '" + max + "'  ORDER BY storyId DESC LIMIT '" + min + "' ", null);
            while (cursor.moveToNext()) {
                ItemStory user = new ItemStory();
                user.setId(cursor.getInt(cursor.getColumnIndex("storyId")));
                user.setCatId(cursor.getString(cursor.getColumnIndex("catId")));
                user.setStoryTitle(cursor.getString(cursor.getColumnIndex("storyTitle")));
                user.setStoryDescription(cursor.getString(cursor.getColumnIndex("storyDescription")));
                user.setStoryDate(cursor.getString(cursor.getColumnIndex("storyDate")));
                user.setStoryViews(cursor.getString(cursor.getColumnIndex("storyViews")));
                user.setstoryImage(cursor.getString(cursor.getColumnIndex("storyImage")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public ArrayList<ItemStory> getStoryByCatId(String id) {
        ArrayList<ItemStory> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from item_story where catId = '" + id + "'", null);
            while (cursor.moveToNext()) {
                ItemStory user = new ItemStory();
                user.setId(cursor.getInt(cursor.getColumnIndex("storyId")));
                user.setCatId(cursor.getString(cursor.getColumnIndex("catId")));
                user.setStoryTitle(cursor.getString(cursor.getColumnIndex("storyTitle")));
                user.setStoryDescription(cursor.getString(cursor.getColumnIndex("storyDescription")));
                user.setStoryDate(cursor.getString(cursor.getColumnIndex("storyDate")));
                user.setStoryViews(cursor.getString(cursor.getColumnIndex("storyViews")));
                user.setstoryImage(cursor.getString(cursor.getColumnIndex("storyImage")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public ArrayList<ItemStory> getStoryLatest() {
        ArrayList<ItemStory> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from item_story ORDER BY storyId DESC LIMIT 5", null);
            while (cursor.moveToNext()) {
                ItemStory user = new ItemStory();
                user.setId(cursor.getInt(cursor.getColumnIndex("storyId")));
                user.setCatId(cursor.getString(cursor.getColumnIndex("catId")));
                user.setStoryTitle(cursor.getString(cursor.getColumnIndex("storyTitle")));
                user.setStoryDescription(cursor.getString(cursor.getColumnIndex("storyDescription")));
                user.setStoryDate(cursor.getString(cursor.getColumnIndex("storyDate")));
                user.setStoryViews(cursor.getString(cursor.getColumnIndex("storyViews")));
                user.setstoryImage(cursor.getString(cursor.getColumnIndex("storyImage")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public ArrayList<ItemStory> getStorySearch(String title) {
        ArrayList<ItemStory> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from item_story WHERE storyTitle LIKE '% " + title + "%'", null);
            while (cursor.moveToNext()) {
                ItemStory user = new ItemStory();
                user.setId(cursor.getInt(cursor.getColumnIndex("storyId")));
                user.setCatId(cursor.getString(cursor.getColumnIndex("catId")));
                user.setStoryTitle(cursor.getString(cursor.getColumnIndex("storyTitle")));
                user.setStoryDescription(cursor.getString(cursor.getColumnIndex("storyDescription")));
                user.setStoryDate(cursor.getString(cursor.getColumnIndex("storyDate")));
                user.setStoryViews(cursor.getString(cursor.getColumnIndex("storyViews")));
                user.setstoryImage(cursor.getString(cursor.getColumnIndex("storyImage")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public ArrayList<ItemSlider> getSlider() {
        ArrayList<ItemSlider> chapterList = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from item_slider", null);
            while (cursor.moveToNext()) {
                ItemSlider user = new ItemSlider();
                user.setSliderId(cursor.getString(cursor.getColumnIndex("SliderId")));
                user.setSliderName(cursor.getString(cursor.getColumnIndex("SliderName")));
                user.setSliderImageUrl(cursor.getString(cursor.getColumnIndex("SliderImageUrl")));
                chapterList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public String getSliderCount() {
        String s = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            long count = DatabaseUtils.queryNumEntries(db, "item_slider");
            s = Long.toString(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getCateCount() {
        String s = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            long count = DatabaseUtils.queryNumEntries(db, "item_category");
            s = Long.toString(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getStoryCount() {
        String s = "0";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            long count = DatabaseUtils.queryNumEntries(db, "item_story");
            s = Long.toString(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public void clearSliderAll() {
        try {
            SQLiteDatabase database = getReadableDatabase();
            database.delete("item_slider", null, null);
        } catch (SQLException e) {
        }
    }
}


