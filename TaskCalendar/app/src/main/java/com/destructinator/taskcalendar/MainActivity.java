package com.destructinator.taskcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TaskDatabaseHelper myDb;     // the task list database
    ListView mTaskListView;      // the list view to display database
    TasksAdapter mAdapter;
    SwitchCompat mSwitch;
    Calendar today_date, next_week_date;
    String day_string;
    int sort_option;

    // Sort option
    static boolean isTouched = false;
    private static final int SORT_BY_DATE = 0;
    private static final int SORT_BY_IMP = 1;

    // set up switch
    private View.OnTouchListener mSwitchTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            isTouched = true;
            return false;
        }
    };

    CompoundButton.OnCheckedChangeListener mSwitchChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isTouched) {
                isTouched = false;
                if (isChecked) {
                    sort_option = SORT_BY_IMP;
                    updateUI();
                }
                else {
                    sort_option = SORT_BY_DATE;
                    updateUI();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = TaskDatabaseHelper.getInstance(this);
        mTaskListView = findViewById(R.id.TaskList);
        mSwitch = findViewById(R.id.SortSwitch);
        mSwitch.setOnTouchListener(mSwitchTouchListener);
        mSwitch.setOnCheckedChangeListener(mSwitchChangeListener);
        sort_option = SORT_BY_DATE;
        updateDay();
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDay();
        updateUI();
    }

    // Open new task activity when tap new task button
    public void newTask(View view) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
    }

    // Use this function to update task list
    private void updateUI() {
        ArrayList<Task> taskList = myDb.getAllTasks(sort_option);
        ArrayList<Task> sortedList = new ArrayList<>();
        int numTask = taskList.size();
        // int numTaskToday = 0;
        // int numTaskOverdue = 0;
        Task task;
        for (int i = 0; i < numTask; i++) {
            task = taskList.get(i);
            if (compareDate(task.date, today_date) < 0) {
                sortedList.add(task);
                // numTaskOverdue += 1;
            }
        }
        for (int i = 0; i < numTask; i++) {
            task = taskList.get(i);
            if (compareDate(task.date, today_date) == 0) {
                sortedList.add(task);
                // numTaskToday += 1;
            }
        }
        for (int i = 0; i < numTask; i++) {
            task = taskList.get(i);
            if ((compareDate(task.date, today_date) > 0) && (compareDate(task.date, next_week_date) <= 0)) {
                sortedList.add(task);
            }
        }
        for (int i = 0; i < numTask; i++) {
            task = taskList.get(i);
            if (compareDate(task.date, next_week_date) > 0) {
                sortedList.add(task);
            }
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
        today_date = Calendar.getInstance();
        next_week_date = Calendar.getInstance();
        next_week_date.add(Calendar.DATE,7);
        int day_of_week = today_date.get(Calendar.DAY_OF_WEEK);
        int today_d = today_date.get(Calendar.DAY_OF_MONTH);
        int today_m = today_date.get(Calendar.MONTH) + 1;
        int today_y = today_date.get(Calendar.YEAR);
        String[] dates = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        day_string = "  Task List  |  " + dates[day_of_week-1] + ", " + today_d + "/" + today_m + "/" + today_y;
        setTitle(day_string);
    }

    public int compareDate(Calendar c1, Calendar c2) {
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
            return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
            return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
    }

}
