package com.destructinator.taskcalendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
// import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static DatabaseHelper myDb;
    ListView mTaskListView;
    ArrayAdapter<String> mAdapter;
    // public static final String EXTRA_MESSAGE = "com.destructinator.taskcalendar.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        mTaskListView = findViewById(R.id.TaskList);
        updateUI();
    }

    // Called when tap new event button
    public void newEvent(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    // Called when tap new task button
    public void newTask(View view) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COL_1, DatabaseHelper.COL_2},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DatabaseHelper.COL_2);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.task_item,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = myDb.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME,
                DatabaseHelper.COL_2 + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    /*
    public void showToast(View view) {
        Toast.makeText(getApplicationContext(), "Your toast message.", Toast.LENGTH_SHORT).show();
    }
    */
}
