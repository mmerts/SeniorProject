package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FourthPageActivity extends AppCompatActivity {

    private String userCountryChoice;
    private String userCityChoice;
    private String userDateChoice;
    private int startTime;
    private int endTime;
    private String userInterest;
    private String userTransportation;

    Button makemyPlan;

    ValueEventListener listener;
    ListView listView;
    Button leadthewaybutton;
    DatabaseReference mdatabase;
    ArrayAdapter<String> adapterplacelist;
    ArrayList<String> PlaceArrayList = new ArrayList<>();
    ArrayList<String> SelectedPlacesList = new ArrayList<>();
    String selectedPlaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_page);
        SelectedPlacesList.clear();
        //get data from previous activity.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userCountryChoice = bundle.getString("countryChoice");
            userCityChoice = bundle.getString("CityChoice");
            userDateChoice = bundle.getString("DateChoice");

            startTime = bundle.getInt("startTime");
            endTime = bundle.getInt("endTime");
            userInterest = bundle.getString("userInterest");
            userTransportation = bundle.getString("userTransport");
        }

        //connection to firebase database userinterest child.
        mdatabase = FirebaseDatabase.getInstance().getReference().child(userInterest).child(userCityChoice);

        makemyPlan = (Button) findViewById(R.id.buttonplanview);

        //Select id of clicked item in listview
        makemyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPlaceId = "";
                if (SelectedPlacesList.size() != 0) {
                    int i = 0;
                    for (; i < SelectedPlacesList.size() - 1; i++) {
                        selectedPlaceId += "" + (PlaceArrayList.indexOf(SelectedPlacesList.get(i)) + 1) + ",";

                    }
                    selectedPlaceId += "" + (PlaceArrayList.indexOf(SelectedPlacesList.get(i)) + 1);
                    System.out.println("ppppppp" + selectedPlaceId);
                } else {
                    selectedPlaceId = "0";
                }
                //Pass data from this activity to fifth page activity
                Intent intent = new Intent(FourthPageActivity.this, FifthPage.class);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                intent.putExtra("userInterest", userInterest);
                intent.putExtra("userTransport", userTransportation);
                intent.putExtra("selectedPlace", selectedPlaceId);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.placelist);
        leadthewaybutton = (Button) findViewById(R.id.buttonplanview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        adapterplacelist = new ArrayAdapter<String>(this, R.layout.rowlayout, PlaceArrayList);
        listView.setAdapter(adapterplacelist);

        mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String places = dataSnapshot.getValue(userInterest.getClass()).toString();
                PlaceArrayList.add(places);
                adapterplacelist.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //selected item add to arraylist in order to reach their id.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedplaces = ((TextView) view).getText().toString();
                if (SelectedPlacesList.contains(selectedplaces)) {
                    SelectedPlacesList.remove(selectedplaces);
                } else {
                    SelectedPlacesList.add(selectedplaces);
                }
            }
        });

    }
}
