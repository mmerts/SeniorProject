package com.example.leadtheway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FourthPageActivity extends AppCompatActivity {

    private String userCountryChoice;
    private String userCityChoice;
    private String userDateChoice;
    private String startTime;
    private String endTime;
    private String userInterest;
    private String userTransportation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_page);





        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userCountryChoice = bundle.getString("countryChoice");
            userCityChoice = bundle.getString("cityChoice");
            userDateChoice = bundle.getString("DateChoice");
            startTime = bundle.getString("startTime");
            endTime = bundle.getString("endTime");
            userInterest = bundle.getString("userInterest");
            userTransportation = bundle.getString("userTransport");
        }
    }
}
