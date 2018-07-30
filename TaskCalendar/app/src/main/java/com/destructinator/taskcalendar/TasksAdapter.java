package com.destructinator.taskcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class TasksAdapter extends ArrayAdapter<Task> {
    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context,0,tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }
        // Lookup view for data population
        TextView taskTitle = convertView.findViewById(R.id.textViewTaskTitle);
        TextView taskID = convertView.findViewById(R.id.textViewID);
        TextView taskDate = convertView.findViewById(R.id.textViewDate);
        FrameLayout frameLayout = convertView.findViewById(R.id.frame);
        // Populate the data into the template view using the data object
        taskID.setText(String.valueOf(task.id));
        taskTitle.setText(task.title);
        String date;
        String[] dates = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.set(task.year,task.month-1,task.day);
        date = dates[cal.get(Calendar.DAY_OF_WEEK)-1] + ", ";
        date = date + String.valueOf(task.day) + "/" + String.valueOf(task.month) + "/" + String.valueOf(task.year);
        int imp = task.imp;
        int color = 0xFF0000FF;
        switch (imp) {
            case 1:     color = 0xFF00FF00; break;
            case 2:     color = 0xFFFFFF00; break;
            case 3:     color = 0xFFFF0000; break;
            default:    color = 0xFF0000FF; break;
        }
        frameLayout.setBackgroundColor(color);
        int comparedDay = compareToday(task.day,task.month,task.year);
        switch (comparedDay) {
            case -1:
                color = 0xFFFF0000;
                date = date + " (Overdue)";
                break;
            case 0:
                color = 0xFF0000FF;
                date = "Today";
                break;
            default:
                color = 0xFF000000;
                break;
        }
        taskTitle.setTextColor(color);
        taskDate.setText(date);
        // Return the completed view to render on screen
        return convertView;
    }

    public int compareToday(int day, int month, int year) {
        Calendar today_cal = Calendar.getInstance();
        int today_day = today_cal.get(Calendar.DAY_OF_MONTH);
        int today_month = today_cal.get(Calendar.MONTH)+1;
        int today_year = today_cal.get(Calendar.YEAR);
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
