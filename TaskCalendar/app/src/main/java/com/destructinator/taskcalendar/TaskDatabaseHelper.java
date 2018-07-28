package com.destructinator.taskcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static TaskDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "TaskCalendar.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_TASKS = "tasks";

    // Task Table Columns
    public static final String KEY_TASK_ID = "ID";
    public static final String KEY_TASK_TITLE = "TITLE";
    private static final String KEY_TASK_DAY = "DAY";
    private static final String KEY_TASK_MONTH = "MONTH";
    private static final String KEY_TASK_YEAR = "YEAR";
    private static final String KEY_TASK_IMPORTANCE = "IMPORTANCE";
    private static final String KEY_TASK_REQ_TIME = "REQUIRED_TIME";
    private static final String KEY_TASK_NOTE = "NOTE";

    public static synchronized TaskDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new TaskDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " " +
                "(" +
                    KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_TASK_TITLE + " TEXT," +
                    KEY_TASK_DAY + " INTEGER," +
                    KEY_TASK_MONTH + " INTEGER," +
                    KEY_TASK_YEAR + " INTEGER," +
                    KEY_TASK_IMPORTANCE + " INTEGER," +
                    KEY_TASK_REQ_TIME + " INTEGER," +
                    KEY_TASK_NOTE + " TEXT" +
                ")";

        db.execSQL(CREATE_TASKS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    public void insertData(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE,task.title);
        values.put(KEY_TASK_DAY,task.day);
        values.put(KEY_TASK_MONTH,task.month);
        values.put(KEY_TASK_YEAR,task.year);
        values.put(KEY_TASK_IMPORTANCE,task.imp);
        values.put(KEY_TASK_REQ_TIME,task.req);
        values.put(KEY_TASK_NOTE,task.note);
        db.insert(TABLE_TASKS,null ,values);
        db.close();
    }

    public void updateData(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE,task.title);
        values.put(KEY_TASK_DAY,task.day);
        values.put(KEY_TASK_MONTH,task.month);
        values.put(KEY_TASK_YEAR,task.year);
        values.put(KEY_TASK_IMPORTANCE,task.imp);
        values.put(KEY_TASK_REQ_TIME,task.req);
        values.put(KEY_TASK_NOTE,task.note);
        db.update(TABLE_TASKS,values,KEY_TASK_ID + " = ?",new String[]{String.valueOf(task.id)});
        db.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS,KEY_TASK_ID + " = ?",new String[]{String.valueOf(id)});
        db.close();
    }

    /*
    public void deleteData(String task_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS,KEY_TASK_TITLE + " = ?",new String[]{task_name});
        db.close();
    }

    public void deleteData(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS,KEY_TASK_ID + " = ?",new String[]{String.valueOf(task.id)});
        db.close();
    }
    */

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS,null,null,null,null,null,
                KEY_TASK_IMPORTANCE + " DESC, " +
                        KEY_TASK_YEAR + " ASC, " +
                        KEY_TASK_MONTH + " ASC, " +
                        KEY_TASK_DAY + " ASC, " +
                        KEY_TASK_ID + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Task current_task = new Task();
                current_task.id = cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID));
                current_task.title = cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE));
                current_task.day = cursor.getInt(cursor.getColumnIndex(KEY_TASK_DAY));
                current_task.month = cursor.getInt(cursor.getColumnIndex(KEY_TASK_MONTH));
                current_task.year = cursor.getInt(cursor.getColumnIndex(KEY_TASK_YEAR));
                current_task.imp = cursor.getInt(cursor.getColumnIndex(KEY_TASK_IMPORTANCE));
                current_task.req = cursor.getInt(cursor.getColumnIndex(KEY_TASK_REQ_TIME));
                current_task.note = cursor.getString(cursor.getColumnIndex(KEY_TASK_NOTE));
                tasks.add(current_task);
            } while(cursor.moveToNext());
        }

        if (!cursor.isClosed())
            cursor.close();
        db.close();
        return tasks;
    }

    public Task getTask(int id) {
        Task task = new Task();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS,null,KEY_TASK_ID + " = ?",
                new String[]{String.valueOf(id)},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        task.id = cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID));
        task.title = cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE));
        task.day = cursor.getInt(cursor.getColumnIndex(KEY_TASK_DAY));
        task.month = cursor.getInt(cursor.getColumnIndex(KEY_TASK_MONTH));
        task.year = cursor.getInt(cursor.getColumnIndex(KEY_TASK_YEAR));
        task.imp = cursor.getInt(cursor.getColumnIndex(KEY_TASK_IMPORTANCE));
        task.req = cursor.getInt(cursor.getColumnIndex(KEY_TASK_REQ_TIME));
        task.note = cursor.getString(cursor.getColumnIndex(KEY_TASK_NOTE));
        if (!cursor.isClosed())
            cursor.close();
        db.close();
        return task;
    }
}
