package com.example.wanderly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "travel.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE places (" +

                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "location TEXT, " +
                "imageResId INTEGER, " +
                "description TEXT)");

        db.execSQL("CREATE TABLE favorites (" +
                "id INTEGER PRIMARY KEY, " + // 跟 places 表保持一致
                "name TEXT, " +
                "location TEXT, " +
                "imageResId INTEGER, " +
                "description TEXT)");


        insertPlace(db, "故宫", "北京", R.drawable.place1, "中国古代皇宫，保存最完整");
        insertPlace(db, "长城", "北京", R.drawable.place2, "中国古代军事防御工程，蜿蜒起伏");
        insertPlace(db, "西湖", "杭州", R.drawable.place3, "风景如画，诗人墨客赞誉之地");
        insertPlace(db, "黄山", "安徽", R.drawable.place4, "奇松怪石云海温泉闻名");
        insertPlace(db, "张家界", "湖南", R.drawable.place5, "世界自然遗产，峰林地貌");
        insertPlace(db, "三亚", "海南", R.drawable.place6, "中国最美海滨度假胜地");
        insertPlace(db, "丽江古城", "云南", R.drawable.place7, "少数民族风情，古老而浪漫");
        insertPlace(db, "布达拉宫", "西藏", R.drawable.place8, "雪域高原的地标，宗教圣地");
        insertPlace(db, "九寨沟", "四川", R.drawable.place9, "五彩湖泊、瀑布、原始森林");
        insertPlace(db, "敦煌莫高窟", "甘肃", R.drawable.place10, "佛教艺术宝库，壁画雕塑精美");
    }

    private void insertPlace(SQLiteDatabase db, String name, String location, int imageResId, String description) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("location", location);
        values.put("imageResId", imageResId);
        values.put("description", description);
        db.insert("places", null, values);
    }

    public void addFavorite(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", place.id);
        values.put("name", place.name);
        values.put("location", place.location);
        values.put("imageResId", place.imageResId);
        values.put("description", place.description);
        db.insertWithOnConflict("favorites", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果数据库版本变化了，可以在这里处理表结构变化
        db.execSQL("DROP TABLE IF EXISTS places");
        onCreate(db);
    }

    public Cursor getAllPlaces() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("places", null, null, null, null, null, null);
    }
}
