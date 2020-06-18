package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class SecondpageActivity extends AppCompatActivity {

    DatabaseReference mdatabase;
    Button buttonthirdpage, Countrybutton, Citybutton, selectDate;
    DatePickerDialog datePickerDialog;
    TextView showDate;
    Calendar calendar;   // to get the day
    TextView notfindCity;
    int year, month, day; // when date picked in datetime picker
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Spinner mSpinnerCountry, mSpinnerCity;
    String country, city;
    ValueEventListener listener;
    ArrayAdapter<String> adaptercountry;
    ArrayAdapter<String> adaptercity;

    ArrayList<String> spinnerDataListCountry;
    ArrayList<String> spinnerDataListCity;
    private String userDateChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        mSpinnerCountry = findViewById(R.id.spinnercountry);
        mSpinnerCity = findViewById(R.id.spinnercity);
        notfindCity = findViewById(R.id.not_find_city);

        notfindCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondpageActivity.this, GiveFeedBack.class));
            }
        });

        Citybutton = (Button) findViewById(R.id.buttonselectcity);
        Countrybutton = (Button) findViewById(R.id.buttonselectcountry);
        buttonthirdpage = (Button) findViewById(R.id.buttonNext);

        // pick date in calendar
        showDate = (TextView) findViewById(R.id.datetxt);
        selectDate = (Button) findViewById(R.id.buttonDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(SecondpageActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                userDateChoice = day + "/" + (month + 1) + "/" + year;
                                showDate.setText(userDateChoice);
                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });

        //Connecting to database and get Countries
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Countries");
        spinnerDataListCountry = new ArrayList<>();
        adaptercountry = new ArrayAdapter<String>(SecondpageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerDataListCountry);
        mSpinnerCountry.setAdapter(adaptercountry);
        retrieveDataCountry();

        //choose item from spinner and automatically update other spinner with respect to country choice.
        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Countrybutton.setText(parent.getItemAtPosition(position).toString());
                country = parent.getItemAtPosition(position).toString(); // hold country information

                mdatabase = FirebaseDatabase.getInstance().getReference().child("Cities").child(country);
                spinnerDataListCity = new ArrayList<>();
                adaptercity = new ArrayAdapter<String>(SecondpageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerDataListCity);
                mSpinnerCity.setAdapter(adaptercity);
                retrieveDataCity();

                //pass item into the spinner
                mSpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int positioncity, long l) {

                        Citybutton.setText(parent.getItemAtPosition(positioncity).toString());
                        city = parent.getItemAtPosition(positioncity).toString(); // hold country information

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        //when user press the third page
        buttonthirdpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenThirdPage();
            }
        });
    }

    //Pass the data between activities for example, countrychoice, city choice and date choice
    public void OpenThirdPage() {

        Intent intent = new Intent(this, ThirdpageActivity.class);
        intent.putExtra("countryChoice", country);
        intent.putExtra("CityChoice", city);
        intent.putExtra("DateChoice", userDateChoice);
        startActivity(intent);
    }
    // Retrive Country data from database
    public void retrieveDataCountry() {
        listener = mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String data = item.getValue().toString();
                    spinnerDataListCountry.add(data);
                }
                adaptercountry.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//retrieve city with respect the country.
    public void retrieveDataCity() {
        listener = mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String data = item.getValue().toString();
                    spinnerDataListCity.add(data);

                }
                adaptercity.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

