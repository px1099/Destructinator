package com.example.a7567_770114gl.taskcalendar_lc;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

public class MyTaskCalendar extends Application{
    public final int MAX_TASK_NAME = 50;
    private final int MAX_DESCRIPTION = 50;
    public static int NOTI_ID = 1;
    public static Calendar cal;
    private static MyTaskCalendar instance;

    private static MyTaskCalendar getInstance(){
        return instance;
    }
    public static Context getContext(){
        return instance.getApplicationContext();
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        cal = Calendar.getInstance();
        instance = this;
        super.onCreate();
        loadTaskList();
    }

    private void loadTaskList(){
        FileInputStream in;
        String filename = "task.dat";
        try{
            in = openFileInput(filename);
            readFromFile(in);
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readFromFile(FileInputStream inputStream){
        int bufferSize = MAX_TASK_NAME + MAX_DESCRIPTION + 9;

        byte[] buffer = new byte[bufferSize];
        int count = 0;
        String task_name, description;
        int dd, mm, yyyy, hour, min, hh_required_time, mm_required_time;

        try{
            while (inputStream.available() > 0){
                inputStream.read(buffer, 0, bufferSize);

                task_name = new String(buffer, 0, MAX_TASK_NAME).trim();

                int t = MAX_TASK_NAME + 1;

                dd = buffer[t++];
                mm = buffer[t++];
                yyyy = buffer[t++];
                hour = buffer[t++];
                min = buffer[t++];

                description = new String(buffer, t, MAX_DESCRIPTION);

                t += MAX_DESCRIPTION;

                hh_required_time = buffer[t++];
                mm_required_time = buffer[t++];

                //MUST BE IN ORDER (String task_name, Date deadline, String description, Date required_time)
                Task temp = new Task
                        (task_name, new Date(yyyy, mm, dd, hour, min), description, new Date(0, 0, 0, hh_required_time, mm_required_time));

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //change from Edditext, retrieved information and change to text
    public static void setDateText(Date date, View v){
        if (cal == null) cal = Calendar.getInstance();
        //GET DEADLINE
        EditText hour_deadline_edittext = (EditText) v.findViewById(R.id.hour_edittext_calendar_layout);
        EditText min_deadline_edittext = (EditText) v.findViewById(R.id.min_editText_calendar_layout);
        EditText day_deadline_edittext = (EditText) v.findViewById(R.id.day_edittext_calendar_layout);
        EditText month_deadline_edditext = (EditText) v.findViewById(R.id.month_edittext_calendar_layout);
        EditText year_deadline_edittext = (EditText) v.findViewById(R.id.year_edittext_calendar_layout);

        cal.setTime(date);

        //RETRIEVE THE INFORMATION FROM THE EDIT TEXT
        hour_deadline_edittext.setText(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
        min_deadline_edittext.setText(Integer.toString(cal.get(Calendar.MINUTE)));
        day_deadline_edittext.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        month_deadline_edditext.setText(Integer.toString(cal.get(Calendar.MONTH)));
        year_deadline_edittext.setText(Integer.toString(cal.get(Calendar.YEAR)));
    }

    public static void printWarning (String message){
        Toast.makeText(MyTaskCalendar.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
