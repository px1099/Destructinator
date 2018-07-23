package com.example.a7567_770114gl.taskcalendar_lc;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Calendar;
import java.util.Date;

public class NewTaskActivity extends AppCompatActivity {
    //VIEW DEDADLINE AND REQUIRED TIME
    private View deadline, requiredTime;
    //TASK NAME AND DESCRIPTION
    EditText task_name_edittext, description_edittext;
    //DEADLINE
    EditText hour_date_edittext, min_date_edittext, dd_date_edittext, mm_date_edittext, yyyy_date_edittext;
    //REQUIRED TIME
    EditText hour_time_edittext_required_time, min_time_edittext_required_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_task);

        //set up all details
        init();

        Intent intent = getIntent();

        final int pos = intent.getIntExtra("pos", -1);
        if (pos == -1){
            //retrieve the information from the view
            MyTaskCalendar.setDateText(Calendar.getInstance().getTime(), deadline);
            //set up
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            final Task t = Task.getTaskList().get(pos);
            setText(t);
            Button submit = (Button) findViewById(R.id.submit_button_input_task);
            submit.setOnClickListener(new View.OnClickListener(){
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    //check if something happen, having warning message
                    edit_submit(v, t);
                    //TODO: OPEN SHOWINFO.CLASS
                    Intent intent1 = new Intent (NewTaskActivity.this, ShowInfo.class);
                    intent1.putExtra("pos", pos);

                    startActivity(intent1);
                    finish();
                }
            });
            setTitle("Edit Task");
        }
    }
    //set everything, Constructor
    public void init(){
        //TASK NAME
        task_name_edittext = (EditText) findViewById(R.id.task_name_edittext_input_task);
        //DESCRIPTION
        description_edittext = (EditText) findViewById(R.id.description_edittext_input_task);
        //DEADLINE
        deadline = findViewById(R.id.calendar_layout_input_task);

        dd_date_edittext = (EditText) findViewById(R.id.day_edittext_calendar_layout);
        mm_date_edittext = (EditText) findViewById(R.id.month_edittext_calendar_layout);
        yyyy_date_edittext = (EditText) findViewById(R.id.year_edittext_calendar_layout);
        hour_date_edittext = (EditText) findViewById(R.id.hour_edittext_calendar_layout);
        min_date_edittext = (EditText) findViewById(R.id.min_editText_calendar_layout);

        //REQUIRED TIME
        requiredTime = findViewById(R.id.time_required_input_task);

        hour_time_edittext_required_time = (EditText) requiredTime.findViewById(R.id.hour_required_edittext_required_time);
        min_time_edittext_required_time = (EditText) requiredTime.findViewById(R.id.min_required_edditext_required_time);
    }

    private void setText(Task t){
        task_name_edittext.setText(t.task_name);

        description_edittext.setText(t.description);

        MyTaskCalendar.setDateText(t.deadline, deadline);

        MyTaskCalendar.cal.setTime(t.required_time);
        hour_time_edittext_required_time.setText(String.valueOf(MyTaskCalendar.cal.get(Calendar.HOUR_OF_DAY)));
        min_time_edittext_required_time.setText(String.valueOf(MyTaskCalendar.cal.get(Calendar.MINUTE)));
    }

    public void submit (View view) {
        Date deadline = getDeadline();
        if (deadline == null) {
            MyTaskCalendar.printWarning("Invalid Date");
            return;
        }
        if (task_name_edittext.getText().toString().isEmpty()){
            MyTaskCalendar.printWarning("You haven't entered your task name");
            return;
        }
        Date required_time = getRequiredTime();
        if (required_time == null){
            MyTaskCalendar.printWarning("Invalid required time");
            return;
        }
        Task new_task = new Task
                (task_name_edittext.getText().toString(),
                deadline,
                description_edittext.getText().toString(),
                required_time);
        Task.updateTaskList(this);

        //TODO: OPEN LISTACTIVITY.CLASS
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    public void edit_submit(View v, Task t){
        Date deadline = getDeadline();
        if (deadline == null) {
            MyTaskCalendar.printWarning("Invalid Date");
            return;
        }
        if (task_name_edittext.getText().toString().isEmpty()){
            MyTaskCalendar.printWarning("You haven't entered your task name");
            return;
        }
        Date required_time = getRequiredTime();
        if (required_time == null){
            MyTaskCalendar.printWarning("Invalid required time");
            return;
        }
        t.task_name = task_name_edittext.getText().toString();
        t.deadline = deadline;
        t.description = description_edittext.getText().toString();
        t.required_time = required_time;
    }
    public Date getDeadline(){
        int dd, mm, yyyy, hour, min;
        Date deadline;
        try{
            dd = Integer.parseInt(dd_date_edittext.getText().toString());
            mm = Integer.parseInt(mm_date_edittext.getText().toString());
            yyyy = Integer.parseInt(yyyy_date_edittext.getText().toString());
            hour = Integer.parseInt(hour_date_edittext.getText().toString());
            min = Integer.parseInt(min_date_edittext.getText().toString());
            if (!isValidDate(dd, mm, yyyy, hour, min, 1)) return null;
            //WHY -1?
            deadline = new Date(yyyy - 1900, mm-1, dd, hour, min);
            return deadline;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Date getRequiredTime(){
        int hour, min;
        Date required_time;
        try{
            hour = Integer.parseInt(hour_time_edittext_required_time.getText().toString());
            min = Integer.parseInt(min_time_edittext_required_time.getText().toString());
            if (!isValidDate(0, 0, 0, hour, min, 2))
                return null;
            required_time = new Date(0, 0, 0, hour, min);
            return required_time;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Boolean isValidDate(int day, int month, int year, int hour, int min, int index) {
        switch (index) {
            case 1:
                if (hour > 24 || hour < 0) return false;
                if (min > 60 || min < 0) return false;
                month++;
                if (day <= 0 || month <= 0 || month > 12 || year < 0) return false;
                if ((year % 4 == 0 || year % 400 == 0) && month == 2) {
                    if (day > 29) return false;
                } else if (day > 28 && month == 2) return false;
                if (month == 1 || month == 3 || month == 5 ||
                        month == 7 || month == 8 || month == 10 || month == 12) {
                    if (day > 31) return false;
                } else {
                    if (day > 30) return false;
                }
            case 2:
                if (hour < 0 && min < 0) return false;
        }
        return true;
    }
}
