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
    Button buttonthirdpage, Countrybutton, Citybutton,selectDate;
    DatePickerDialog datePickerDialog;
    TextView showDate;
    Calendar calendar;
    int year,month,day;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;




    Spinner mSpinnerCountry,mSpinnerCity;
    String country, city;
    ValueEventListener listener;
    ArrayAdapter<String> adaptercountry;
    ArrayAdapter<String> adaptercity;

    ArrayList<String> spinnerDataListCountry;
    ArrayList<String> spinnerDataListCity;
    private String userDateChoice;
    private String userCountryChoice;
    private String userCityChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        mSpinnerCountry = findViewById(R.id.spinnercountry);
        mSpinnerCity = findViewById(R.id.spinnercity);


        Citybutton = (Button) findViewById(R.id.buttonselectcity);
        Countrybutton = (Button) findViewById(R.id.buttonselectcountry);
        buttonthirdpage = (Button) findViewById(R.id.buttonNext);


        showDate = (TextView) findViewById(R.id.datetxt);
        selectDate = (Button)findViewById(R.id.buttonDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                userDateChoice = day + "/" + (month+1) + "/" + year;
                datePickerDialog = new DatePickerDialog(SecondpageActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                showDate.setText(userDateChoice);
                            }}, year, month, day);
                datePickerDialog.show();

            }
        });





        mdatabase = FirebaseDatabase.getInstance().getReference().child("Countries");
        spinnerDataListCountry = new ArrayList<>();
        adaptercountry = new ArrayAdapter<String>(SecondpageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerDataListCountry);
        mSpinnerCountry.setAdapter(adaptercountry);
        retrieveDataCountry();








        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                // mdatabase = FirebaseDatabase.getInstance().getReference().child("Cities");
                Countrybutton.setText(parent.getItemAtPosition(position).toString());
                country = parent.getItemAtPosition(position).toString(); // hold country information

                mdatabase = FirebaseDatabase.getInstance().getReference().child("Cities").child(country);
                spinnerDataListCity = new ArrayList<>();
                adaptercity = new ArrayAdapter<String>(SecondpageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerDataListCity);
                mSpinnerCity.setAdapter(adaptercity);
                retrieveDataCity();



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



        //ArrayAdapter arraycountry = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countryoptions);
        //arraycountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mSpinnerCountry.setAdapter(arraycountry);
/*
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Countries");
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 c1 = dataSnapshot.child("Countryid1").getValue().toString();
                 c2 = dataSnapshot.child("Countryid2").getValue().toString();
                 c3 = dataSnapshot.child("Countryid3").getValue().toString();



            }





            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


*/
        buttonthirdpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenThirdPage();
            }
        });


//when click country/city button go to citybutton page and selection of country then city
 /*       Countrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondpageActivity.this, CityButton.class);
                startActivity(intent);
            }
        });
*/
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signout,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.signOut:
                logout();
                Toast.makeText(this,"Sign out completed",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SecondpageActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //City activity

    public void OpenThirdPage(){

        Intent intent = new Intent(this, ThirdpageActivity.class);
        intent.putExtra("countryChoice",userCountryChoice);
        intent.putExtra("CityChoice",userCityChoice);
        intent.putExtra("DateChoice",userDateChoice);
        startActivity(intent);
    }


    public void retrieveDataCountry(){
        listener = mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    userCountryChoice = item.getValue().toString();
                    spinnerDataListCountry.add(userCountryChoice);

                }
                adaptercountry.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void retrieveDataCity(){
        listener = mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    userCityChoice = item.getValue().toString();
                    spinnerDataListCity.add(userCityChoice);

                }
                adaptercity.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}

