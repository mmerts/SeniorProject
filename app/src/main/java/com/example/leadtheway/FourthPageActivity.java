package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    private String startTime;
    private String endTime;
    private String userInterest;
    private String userTransportation;

    ValueEventListener listener;
    ListView listView;
    Button leadthewaybutton;
    DatabaseReference mdatabase;
    ArrayAdapter<String> adapterplacelist;
    ArrayList<String> PlaceArrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_page);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            userCountryChoice = bundle.getString("countryChoice");
            userCityChoice = bundle.getString("CityChoice");
            userDateChoice = bundle.getString("DateChoice");
            startTime = bundle.getString("startTime");
            endTime = bundle.getString("endTime");
            userInterest = bundle.getString("userInterest");
            userTransportation = bundle.getString("userTransport");
        }


            mdatabase = FirebaseDatabase.getInstance().getReference().child("Historical Places(Museum)").child("Ankara");//BURAYA USER INTEREST VE CİTY PREFERENCE GELDİĞİNDE ÇALIŞMASI LAZIM BUNDLEDAN ÇEKEMİYOR.


            mdatabase = FirebaseDatabase.getInstance().getReference().child("Restaurant").child(userCityChoice);


            listView = (ListView) findViewById(R.id.placelist);
            adapterplacelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PlaceArrayList);
            listView.setAdapter(adapterplacelist);
            retrieveDataPlaces();


  /*          mdatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String places = dataSnapshot;

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

*/
        }


    public void retrieveDataPlaces(){
        listener = mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    String places = item.getValue().toString();
                    PlaceArrayList.add(places);

                }
                adapterplacelist.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
