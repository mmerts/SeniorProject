package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    boolean active = true;

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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            userCountryChoice = bundle.getString("countryChoice");
            userCityChoice = bundle.getString("CityChoice");
            userDateChoice = bundle.getString("DateChoice");

            startTime = bundle.getInt("startTime");
            endTime = bundle.getInt("endTime");
            userInterest = bundle.getString("userInterest");
            userTransportation = bundle.getString("userTransport");
        }


            mdatabase = FirebaseDatabase.getInstance().getReference().child(userInterest).child(userCityChoice); //BURAYA USER INTEREST VE CİTY PREFERENCE GELDİĞİNDE ÇALIŞMASI LAZIM BUNDLEDAN ÇEKEMİYOR.

            makemyPlan = (Button)findViewById(R.id.buttonplanview);
            makemyPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<SelectedPlacesList.size();i++) {
                        selectedPlaceId += ""+PlaceArrayList.indexOf(SelectedPlacesList.get(i))+",";
                    }
                    Intent intent =new Intent(FourthPageActivity.this, FifthPage.class);
                    intent.putExtra("selectedPlace",selectedPlaceId);
                    intent.putExtra("startTime",startTime);
                    intent.putExtra("endTime",endTime);
                    intent.putExtra("userInterest",userInterest);
                    intent.putExtra("userTransport",userTransportation);

                    startActivity(intent);
                }
            });
        System.out.println("ccccccccc : "+startTime);

            listView = (ListView) findViewById(R.id.placelist);
            leadthewaybutton = (Button) findViewById(R.id.buttonplanview);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            adapterplacelist = new ArrayAdapter<String>(this, R.layout.rowlayout, PlaceArrayList);
            listView.setAdapter(adapterplacelist);
       //     retrieveDataPlaces();

       mdatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String places = dataSnapshot.getValue(userInterest.getClass()).toString();
                    //String key = dataSnapshot.getKey();
                    //System.out.println(key);
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
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedplaces = ((TextView)view).getText().toString();
                    if(SelectedPlacesList.contains(selectedplaces)){
                        SelectedPlacesList.remove(selectedplaces);
                    }
                    else {
                        SelectedPlacesList.add(selectedplaces);
                    }
                }
            });

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

    public void showSelectedItems(View view) {
        String s = "";
        for(String item:SelectedPlacesList){
            s += "--" + item + "/n";
        }
        Toast.makeText(this, "You have selected " + s, Toast.LENGTH_LONG).show();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }





}
