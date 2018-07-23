package com.example.a7567_770114gl.taskcalendar_lc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.widget.RadioButton;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Task {
    public String task_name;
    public Date deadline;
    public String description;
    public Date required_time;

    private static Calendar cal = Task.cal;

    //an array containing task(s)
    private static ArrayList<Task> task_list = new ArrayList<Task>();

    private int noti_id;

    private static final String pattern_deadline = new String("dd/mm/yyyy");
    private static final String pattern_required_time = new String ("hh:mm");

    private final int MAX_TASK_NAME = 50;
    private final int MAX_DESCRIPTION = 100;

    private static String CUSTOM_ACTION = "com.example.a7567_770114gl.taskcalendar_lc";

    public static ArrayList<Task> getTaskList(){
        return task_list;
    }

    //MUST BE IN ORDER (String task_name, Date deadline, String description, Date required_time)
    public Task (String task_name, Date deadline, String description, Date required_time){
        this.task_name = task_name;
        this.deadline = deadline;
        this.description = description;
        this.required_time = required_time;
        if (task_list == null){
            task_list = new ArrayList<Task>();
        }
        addToTaskList(this);

        noti_id = MyTaskCalendar.NOTI_ID;

        MyTaskCalendar.NOTI_ID++;

        setNotification();
    }
    //public constructor
    //MUST BE IN ORDER (String task_name, Date deadline, String description, Date required_time)
    public Task(Date deadline, String task_name, String description, Date required_time){
        this(task_name, deadline, "", required_time);
    }

    private void addToTaskList(Task cur_task){
        //sort based on deadline date, importance can be ignored first
        if (task_list.isEmpty()){
            task_list.add(cur_task);
            return;
        }
        for (int i = 0; i < task_list.size(); i++){
            Task task = task_list.get(i);
            if (task.deadline.compareTo(cur_task.deadline)>0){
                task_list.add(i, cur_task);
                return;
            }
        }
        task_list.add(cur_task);
        return;
    }

    public String getDeadlineDate(){
        DateFormat df = new SimpleDateFormat(pattern_deadline);
        String deadlineDate = df.format(deadline);
        //change Date to String based on the format "dd/mm/yyyy"
        return deadlineDate;
    }

    public String getRequiredTime(){
        DateFormat df = new SimpleDateFormat(pattern_required_time);
        String requiredTime = df.format(required_time);
        return requiredTime;
    }

    private void writeToFile(FileOutputStream outputStream){
        try{
            //TASK_NAME
            byte[] buffer = task_name.getBytes();

            int len = buffer.length;
            outputStream.write(buffer, 0, len);
            for (int i = 0; i < MAX_TASK_NAME - len + 1; i++){
                outputStream.write(0);
            }

            //DEADLINE DATE
            buffer = new byte[1]; //only have 2 digits, so we need to deduct 1900 to change the year
            cal.setTime(deadline);

            int value = cal.get(Calendar.DAY_OF_MONTH);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);

            value = cal.get(Calendar.MONTH);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);

            value = cal.get(Calendar.YEAR);
            buffer[0] = (byte) (value - 1900);
            outputStream.write(buffer);

            value = cal.get(Calendar.HOUR_OF_DAY);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);

            value = cal.get(Calendar.MINUTE);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);

            //DESCRIPTION
            buffer = description.getBytes();
            len = buffer.length;
            outputStream.write(buffer, 0, len);
            for (int i = 0; i < MAX_DESCRIPTION - len; i++){
                outputStream.write(0);
            }

            //REQUIRED_TIME
            cal.setTime(required_time);

            value = cal.get(Calendar.HOUR_OF_DAY);
            buffer[0] = (byte) (value);
            outputStream.write(buffer[0]);

            value = cal.get(Calendar.MINUTE);
            buffer[0] = (byte) (value);
            outputStream.write(buffer[0]);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void updateTaskList (Context context, String filename){
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for (Task t: Task.getTaskList()){
                t.writeToFile(outputStream);
            }
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //update task to the file named task_list
    public static void updateTaskList (Context context){
        updateTaskList(context, "task_list.dat");
    }

    public int getPosition(){
        return task_list.indexOf(this);
    }

    public int getNoti_ID(){
        return noti_id;
    }

    public void setNotification(){
        Task t = this;
        Context context = MyTaskCalendar.getContext();

        //TODO: OPEN NOTIFICATION RECEIVER
        Intent intent  = new Intent(context, NotificationReceiver.class);
        intent.setAction(Integer.toString(t.noti_id));

        intent.setType("text/plain");
        intent.putExtra("pos", t.getPosition());

        Date remind_time = get_remind_date_from_required_time();
        cal.setTime(remind_time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, t.noti_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private Date get_remind_date_from_required_time() {
        cal.setTime(this.deadline);

        Calendar temp = Calendar.getInstance();
        temp.setTime (this.required_time);

        cal.add (Calendar.HOUR_OF_DAY, -temp.get(Calendar.HOUR_OF_DAY));
        cal.add(Calendar.MINUTE, -temp.get(Calendar.MINUTE));

        return cal.getTime();
    }

    public void removeTask(){
        Task t = this;

        Context context = MyTaskCalendar.getContext();

        //TODO: OPEN NOTIFICATION RECEIVER
        Intent intent = new Intent (context, NotificationReceiver.class);
        intent.setAction(Integer.toString(t.noti_id));

        intent.setType("text/plain");
        intent.putExtra("pos", t.getPosition());

        Date remindTime = get_remind_date_from_required_time();
        cal.setTime(remindTime);

        PendingIntent.getBroadcast
                (context, t.noti_id, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();

        task_list.remove(this);
        updateTaskList(context);
    }
}
