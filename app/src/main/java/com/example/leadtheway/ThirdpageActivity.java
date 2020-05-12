package com.example.leadtheway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ThirdpageActivity extends AppCompatActivity {

    Button startTime;
    Button endTime;
    TextView starttxt;
    TextView endtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdpage);
        startTime = (Button)findViewById(R.id.startTime);
        endTime = (Button)findViewById(R.id.endTime);
        starttxt = (TextView)findViewById(R.id.txtStartTime);
        endtxt = (TextView)findViewById(R.id.txtEndTime);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mStartTime = Calendar.getInstance();
                int hour = mStartTime.get(Calendar.HOUR_OF_DAY);
                int minute = mStartTime.get(Calendar.MINUTE);

                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(ThirdpageActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        starttxt.setText(hourOfDay + ":"+ minute);
                    }
                },hour,minute,true);
                timePicker.setTitle("Please Select The Start Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE,"Set",timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Cancel",timePicker);

                timePicker.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mStartTime = Calendar.getInstance();
                int hour = mStartTime.get(Calendar.HOUR_OF_DAY);
                int minute = mStartTime.get(Calendar.MINUTE);

                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(ThirdpageActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endtxt.setText(hourOfDay + ":"+ minute);
                    }
                },hour,minute,true);
                timePicker.setTitle("Please Select The Start Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE,"Set",timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Cancel",timePicker);

                timePicker.show();
            }
        });




    }
}
