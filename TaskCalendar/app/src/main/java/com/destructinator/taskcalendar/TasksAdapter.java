package com.destructinator.taskcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class TasksAdapter extends ArrayAdapter<Task> {
    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context,0,tasks);
    }

    private static final int RED = 0xFFFF0000;
    private static final int YELLOW = 0xFFFFFF00;
    private static final int GREEN = 0xFF00FF00;
    private static final int BLUE = 0xFF0000FF;
    private static final int DARK_GREEN = 0xFF00B050;
    private static final int BLACK = 0xFF000000;

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
        String date_string;
        String[] dates = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        date_string = dates[task.date.get(Calendar.DAY_OF_WEEK)-1] + ", ";
        int local_d, local_m, local_y;
        local_d = task.date.get(Calendar.DAY_OF_MONTH);
        local_m = task.date.get(Calendar.MONTH) + 1;
        local_y = task.date.get(Calendar.YEAR);
        date_string = date_string + local_d + "/" + local_m + "/" + local_y;
        int imp = task.imp;
        int color;
        switch (imp) {
            case 1:     color = GREEN; break;
            case 2:     color = YELLOW; break;
            case 3:     color = RED; break;
            default:    color = BLUE; break;
        }
        frameLayout.setBackgroundColor(color);
        int comparedDay = compareTodayAndNextWeek(task.date);
        switch (comparedDay) {
            case 0:
                color = RED;
                date_string = date_string + " (Overdue)";
                break;
            case 1:
                color = BLUE;
                date_string = "Today";
                break;
            case 2:
                color = DARK_GREEN;
                date_string = "Next " + date_string;
                break;
            default:
                color = BLACK;
                break;
        }
        taskTitle.setTextColor(color);
        taskDate.setText(date_string);
        // Return the completed view to render on screen
        return convertView;
    }

    public int compareTodayAndNextWeek(Calendar date) {
        Calendar today_date = Calendar.getInstance();
        Calendar next_week_date = Calendar.getInstance();
        next_week_date.add(Calendar.DATE, 7);
        if (compareDate(date, today_date) < 0) {
            return 0;
        }
        else if (compareDate(date, today_date) == 0) {
            return 1;
        }
        else if ((compareDate(date, today_date) > 0) && (compareDate(date, next_week_date) <= 0)) {
            return 2;
        }
        else {
            return 3;
        }
    }

    public int compareDate(Calendar c1, Calendar c2) {
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
            return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
            return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
    }
}
