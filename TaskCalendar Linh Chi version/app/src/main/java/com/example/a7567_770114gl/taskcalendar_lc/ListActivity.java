package com.example.a7567_770114gl.taskcalendar_lc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_output_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TaskAdapter adapter =
            new TaskAdapter(this, R.layout.activity_task_list, Task.getTaskList());

        ListView task_list_listview = (ListView) findViewById(R.id.deadline_listview_output_task);
        task_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: OPEN THE SHOWINFO.CLASS
                Intent intent = new Intent(MyTaskCalendar.getContext(), ShowInfo.class);
                intent.putExtra("pos", position);

                startActivity(intent);
                finish();
            }
        });
        task_list_listview.setAdapter(adapter);
    }
}
