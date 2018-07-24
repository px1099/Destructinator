package com.destructinator.taskcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tasks.db";
    public static final String TABLE_NAME = "tasks_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "DAY";
    public static final String COL_4 = "MONTH";
    public static final String COL_5 = "YEAR";
    public static final String COL_6 = "IMPORTANCE";
    public static final String COL_7 = "REQUIRED_TIME";
    public static final String COL_8 = "NOTE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_2 + " TEXT," +
                COL_3 + " INTEGER," +
                COL_4 + " INTEGER," +
                COL_5 + " INTEGER," +
                COL_6 + " INTEGER," +
                COL_7 + " INTEGER," +
                COL_8 + " TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title,String day,String month,String year,
                              String importance,String req_time,String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_vals = new ContentValues();
        content_vals.put(COL_2,title);
        content_vals.put(COL_3,day);
        content_vals.put(COL_4,month);
        content_vals.put(COL_5,year);
        content_vals.put(COL_6,importance);
        content_vals.put(COL_7,req_time);
        content_vals.put(COL_8,note);
        long result = db.insert(TABLE_NAME,null ,content_vals);
        return (result != -1);
    }


}
