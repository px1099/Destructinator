package com.example.a7567_770114gl.taskcalendar_lc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShowInfo extends AppCompatActivity{
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        Intent intent = getIntent();
        position = intent.getIntExtra("pos", 0);
        showTaskInfo(position);
    }

    private void showTaskInfo(int position){
        final Task t = Task.getTaskList().get(position);
        setTitle(t.task_name);
        final View dateLayout_info = findViewById(R.id.calendar_layout_task_info);
        final EditText desciption_info_edditext = (EditText) findViewById(R.id.description_edittext_task_info);
        if (desciption_info_edditext != null){
            desciption_info_edditext.setText(t.description);
            desciption_info_edditext.setEnabled(false);
        }
        //TODO: Delete information of task
        Button delete_button = (Button) findViewById(R.id.delete_button_task_info);
        if (delete_button != null){
            delete_button.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    t.removeTask();
                    //TODO: OPEN LISTACTIVITY WITH CONTEXT AS SHOWINFO
                    Intent intent = new Intent(ShowInfo.this, ListActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        //DEADLINE
        MyTaskCalendar.setDateText(t.deadline, dateLayout_info);
        EditText dd_edittext = (EditText) dateLayout_info.findViewById(R.id.day_edittext_calendar_layout);
        EditText mm_edittext = (EditText) dateLayout_info.findViewById(R.id.month_edittext_calendar_layout);
        EditText yyyr_edittext = (EditText) dateLayout_info.findViewById(R.id.year_edittext_calendar_layout);
        EditText hour_edittext = (EditText) dateLayout_info.findViewById(R.id.hour_edittext_calendar_layout);
        EditText min_ediitext = (EditText) dateLayout_info.findViewById(R.id.min_editText_calendar_layout);

        dd_edittext.setEnabled(false);
        mm_edittext.setEnabled(false);
        yyyr_edittext.setEnabled(false);
        hour_edittext.setEnabled(false);
        min_ediitext.setEnabled(false);
    }

    public void edit_mode(View v){
        Intent intent = new Intent (this, NewTaskActivity.class);
        intent.putExtra("pos", position);
        startActivity(intent);
    }
}
