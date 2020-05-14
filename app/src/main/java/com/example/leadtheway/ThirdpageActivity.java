package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ThirdpageActivity extends AppCompatActivity {

    Button startTime;
    Button endTime;
    TextView starttxt;
    TextView endtxt;

    TextView getCountryChoice;


    DatabaseReference mdatabase;
    Button interestbutton, transportbutton;
    Button buttonfourtpage;
    Spinner mSpinnerinterest, mSpinnertransport;
    String interest,transportation;
    ValueEventListener listener;

    ArrayAdapter<String> adapterinterest;
    ArrayAdapter<String> adaptertransportation;

    ArrayList<String> spinnerDataListInterest;
    ArrayList<String> spinnerDataListTransportation;
    private String userCountryChoice;
    private String userCityChoice;
    private String userDateChoice;
    private String startTime1;
    private String endTime1;
    private String userInterest;
    private String userTransportation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdpage);

        startTime = (Button)findViewById(R.id.startTime);
        endTime = (Button)findViewById(R.id.endTime);
        starttxt = (TextView)findViewById(R.id.txtStartTime);
        endtxt = (TextView)findViewById(R.id.txtEndTime);

        getCountryChoice =(TextView) findViewById(R.id.getCountryChoice);

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
                        startTime1 = hourOfDay + ":"+ minute;
                        starttxt.setText(startTime1);
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
                        endTime1 = hourOfDay + ":"+ minute;
                        endtxt.setText(endTime1);
                    }
                },hour,minute,true);
                timePicker.setTitle("Please Select The Start Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE,"Set",timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Cancel",timePicker);

                timePicker.show();
            }
        });







    mSpinnerinterest = findViewById(R.id.spinnerinterest);
    mSpinnertransport = findViewById(R.id.spinnertransport);

    interestbutton = (Button) findViewById(R.id.buttoninterest);
    transportbutton = (Button) findViewById(R.id.buttontransportation);
    buttonfourtpage  = (Button) findViewById(R.id.buttonfourtpage);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Interest");
        spinnerDataListInterest = new ArrayList<>();
        adapterinterest = new ArrayAdapter<String>(ThirdpageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerDataListInterest);
        mSpinnerinterest.setAdapter(adapterinterest);
        retrieveDataInterest();

        mSpinnerinterest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int positioninterest, long id) {
                // mdatabase = FirebaseDatabase.getInstance().getReference().child("Cities");
                //interestbutton.setText(parent.getItemAtPosition(positioninterest).toString());
                interest = parent.getItemAtPosition(positioninterest).toString(); // hold country information

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        mdatabase = FirebaseDatabase.getInstance().getReference().child("Transportation");
        spinnerDataListTransportation = new ArrayList<>();
        adaptertransportation = new ArrayAdapter<String>(ThirdpageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerDataListTransportation);
        mSpinnertransport.setAdapter(adaptertransportation);
        retrieveDataTransport();



        mSpinnertransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                // mdatabase = FirebaseDatabase.getInstance().getReference().child("Cities");
                transportbutton.setText(parent.getItemAtPosition(position).toString());
                transportation = parent.getItemAtPosition(position).toString(); // hold country information

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });





        buttonfourtpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFourthPage();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userCountryChoice = bundle.getString("countryChoice");
            getCountryChoice.setText(userCountryChoice);
            userCityChoice = bundle.getString("cityChoice");
            userDateChoice = bundle.getString("DateChoice");
        }

    }

    private void OpenFourthPage() {
        Intent intent = new Intent(this, FourthPageActivity.class);
        intent.putExtra("countryChoice",userCountryChoice);
        intent.putExtra("CityChoice",userCityChoice);
        intent.putExtra("DateChoice",userDateChoice);

        intent.putExtra("startTime",startTime1);
        intent.putExtra("endTime",endTime1);
        intent.putExtra("userInterest",userInterest);
        intent.putExtra("userTransport",userTransportation);
        startActivity(intent);
    }

    public void retrieveDataInterest(){
        listener = mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    userInterest = item.getValue().toString();
                    spinnerDataListInterest.add(userInterest);

                }
                adapterinterest.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrieveDataTransport(){
        listener = mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    userTransportation = item.getValue().toString();
                    spinnerDataListTransportation.add(userTransportation);

                }
                adaptertransportation.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
