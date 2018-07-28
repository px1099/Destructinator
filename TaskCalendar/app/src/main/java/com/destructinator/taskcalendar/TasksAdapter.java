package com.destructinator.taskcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
        String date = String.valueOf(task.day) + "/" + String.valueOf(task.month) + "/" + String.valueOf(task.year);
        taskDate.setText(date);
        int imp = task.imp;
        int color = 0xFF0000FF;
        switch (imp) {
            case 1: color = 0xFF00FF00; break;
            case 2: color = 0xFFFFFF00; break;
            case 3: color = 0xFFFF0000; break;
            default: color = 0xFF0000FF; break;
        }
        frameLayout.setBackgroundColor(color);
        // Return the completed view to render on screen
        return convertView;
    }
}
