package com.destructinator.taskcalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    ImageButton btn;
    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;
    TextView year_text, month_text, day_text;
    EditText task_title, req_time_text, note_text;
    RadioGroup importance_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        // change action bar title
        setTitle("Add New Task");
        // set up
        year_text = findViewById(R.id.TextViewYear);
        month_text = findViewById(R.id.TextViewMonth);
        day_text = findViewById(R.id.TextViewDay);
        task_title = findViewById(R.id.TaskName);
        req_time_text = findViewById(R.id.RequiredTime);
        note_text = findViewById(R.id.Note);
        importance_group = findViewById(R.id.ImportanceRadioGroup);
        setUpCurrentDate();
        showDialogOnButtonClick();
        // open database
        myDb = MainActivity.myDb;
    }

    // create an action bar button for this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // add data to the database and end the activity when the action bar button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ConfirmButton) {
            myDb.insertData(
                    task_title.getText().toString(),
                    day_text.getText().toString(),
                    month_text.getText().toString(),
                    year_text.getText().toString(),
                    getImportance(),
                    req_time_text.getText().toString(),
                    note_text.getText().toString());
            Toast.makeText(this,"Task created",Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // return the importance of the selected radio button
    public String getImportance() {
        String result;
        int selected_importance = importance_group.getCheckedRadioButtonId();
        switch (selected_importance) {
            case R.id.ImportanceRadio1:     result = "1";   break;
            case R.id.ImportanceRadio2:     result = "2";   break;
            case R.id.ImportanceRadio3:     result = "3";   break;
            default:                        result = "0";   break;
        }
        return result;
    }

    // Open calendar picker dialog when click the image calendar button
    public void showDialogOnButtonClick() {
        btn = findViewById(R.id.CalendarButton);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, datePickerListner, year_x, month_x, day_x);
        return null;
    }

    // set up the date variables based on the picked date in the dialog
    private DatePickerDialog.OnDateSetListener datePickerListner
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            year_text.setText(String.valueOf(year_x));
            month_text.setText(String.valueOf(month_x));
            day_text.setText(String.valueOf(day_x));
        }
    };

    // set up the initial date as the date of the day
    public void setUpCurrentDate() {
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        year_text.setText(String.valueOf(year_x));
        month_text.setText(String.valueOf(month_x + 1));
        day_text.setText(String.valueOf(day_x));
    }
}
