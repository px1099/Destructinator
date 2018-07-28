package com.destructinator.taskcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TaskDatabaseHelper myDb;     // the task list database
    ListView mTaskListView;      // the list view to display database
    TasksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = TaskDatabaseHelper.getInstance(this);
        mTaskListView = findViewById(R.id.TaskList);
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Open new event activity when tap new event button
    public void newEvent(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    // Open new task activity when tap new task button
    public void newTask(View view) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
    }

    // Use this function to update task list
    private void updateUI() {
        ArrayList<Task> taskList = myDb.getAllTasks();
        if (mAdapter == null) {
            mAdapter = new TasksAdapter(this, taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    // delete the task from the database
    public void completeTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.textViewID);
        CheckBox box = parent.findViewById(R.id.checkBox);
        if(box.isChecked()) {
            box.toggle();
        }
        int completedID = Integer.parseInt(taskTextView.getText().toString());
        myDb.deleteData(completedID);
        updateUI();
        Toast.makeText(this,"Task completed",Toast.LENGTH_LONG).show();
    }

}
