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
    public static final String COL_7 = "TIME";
    public static final String COL_8 = "NOTE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DAY INTEGER,MONTH INTEGER,YEAR INTEGER,IMPORTANCE INTEGER,TIME INTEGER,NOTE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title,String day,String month,String year,
                              String importance,String time,String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,day);
        contentValues.put(COL_4,month);
        contentValues.put(COL_5,year);
        contentValues.put(COL_6,importance);
        contentValues.put(COL_7,time);
        contentValues.put(COL_8,note);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


}
