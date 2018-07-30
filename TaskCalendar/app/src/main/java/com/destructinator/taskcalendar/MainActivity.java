package com.destructinator.taskcalendar;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TaskDatabaseHelper myDb;     // the task list database
    ListView mTaskListView;      // the list view to display database
    TasksAdapter mAdapter;
    int day_of_week, today_day, today_month, today_year;
    String day_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = TaskDatabaseHelper.getInstance(this);
        mTaskListView = findViewById(R.id.TaskList);
        updateDay();
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDay();
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
        ArrayList<Task> sortedList = new ArrayList<>();
        int numTask = taskList.size();
        int numTaskToday = 0;
        int numTaskOverdue = 0;
        int i, d, m, y;
        Task task;
        for (i = 0; i < numTask; i++) {
            task = taskList.get(i);
            d = task.day;
            m = task.month;
            y = task.year;
            if (compareDay(d, m, y) < 0) {
                sortedList.add(task);
                numTaskOverdue += 1;
            }
        }
        for (i = 0; i < numTask; i++) {
            task = taskList.get(i);
            d = task.day;
            m = task.month;
            y = task.year;
            if (compareDay(d, m, y) == 0) {
                sortedList.add(task);
                numTaskToday += 1;
            }
        }
        for (i = 0; i < numTask; i++) {
            task = taskList.get(i);
            d = task.day;
            m = task.month;
            y = task.year;
            if (compareDay(d, m, y) > 0)
                sortedList.add(task);
        }
        if (mAdapter == null) {
            mAdapter = new TasksAdapter(this, sortedList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(sortedList);
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
        Toast.makeText(this,"Task completed",Toast.LENGTH_SHORT).show();
    }

    // edit a task
    public void editTask(View view) {
        TextView taskID = view.findViewById(R.id.textViewID);
        int id = Integer.parseInt(taskID.getText().toString());
        Intent intent = new Intent(this, EditTaskActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", id);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void updateDay() {
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);
        today_month = cal.get(Calendar.MONTH)+1;
        today_year = cal.get(Calendar.YEAR);
        day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        String[] dates = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        day_string = " " + dates[day_of_week-1] + ", " + today_day + "/" + today_month + "/" + today_year;
        setTitle(day_string);
    }

    public int compareDay(int day, int month, int year) {
        if (year > today_year)              return 1;
        else if (year < today_year)         return -1;
        else {
            if (month > today_month)        return 1;
            else if (month < today_month)   return -1;
            else {
                if (day > today_day)        return 1;
                else if (day < today_day)   return -1;
                else                        return 0;
            }
        }
    }
}
