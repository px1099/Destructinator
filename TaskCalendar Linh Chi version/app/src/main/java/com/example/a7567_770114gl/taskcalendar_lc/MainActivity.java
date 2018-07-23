package com.example.a7567_770114gl.taskcalendar_lc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show in activity_main
        setContentView(R.layout.activity_main);
    }
    //TODO: OPEN NEWTASKACTIVITY.CLASS
    public void openInputScreen(View view){
        Intent intent = new Intent (this, NewTaskActivity.class);
        startActivity(intent);
    }
    //TODO: OPEN LISTACTIVITY.CLASS
    public void openOutputScreen(View view){
        Intent intent = new Intent (this, ListActivity.class);
        startActivity(intent);
    }
}
