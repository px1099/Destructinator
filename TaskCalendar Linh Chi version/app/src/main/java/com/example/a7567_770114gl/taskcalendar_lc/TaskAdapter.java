package com.example.a7567_770114gl.taskcalendar_lc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
    }

    public TaskAdapter(Context context, int resource, List<Task> task_list){
        super(context, resource, task_list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = (View)((LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_task_list, null);
        Task t = getItem(position);
        if (t != null){
            TextView task_name = (TextView) v.findViewById(R.id.task_name_task_list);
            TextView deadline = (TextView) v.findViewById(R.id.deadline_textview_task_list);
            TextView required_time = (TextView) v.findViewById(R.id.required_time_date_textview_task_list);
            task_name.setText(t.task_name);
            //change from Date to String and setText
            deadline.setText(t.getDeadlineDate());
            required_time.setText(t.getRequiredTime());
        }
        return v;
    }
}
