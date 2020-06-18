package com.example.leadtheway.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.leadtheway.DirectionsJSONParser;
import com.example.leadtheway.FifthPage;
import com.example.leadtheway.FirebaseUtil;
import com.example.leadtheway.Museum;
import com.example.leadtheway.MuseumList;
import com.example.leadtheway.R;
import com.example.leadtheway.ThirdpageActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimetableFragment extends Fragment {


    public static List<Museum> museumList;


    ListView listViewPlace;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private Query query;
    private ValueEventListener valueEventListener;
    public static MuseumList adapter;

    public TimetableFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timetable, container, false);

        museumList = new ArrayList<Museum>();

        listViewPlace = root.findViewById(R.id.listViewPlaces);


        FirebaseUtil.openFbReference("places");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        //museum id query
        query = FirebaseUtil.mDatabaseReference.child("Amsterdam").child("Museum").orderByChild("id");
        //according to id array collect relevant museum object from database according musum id.
        getDataWithIdArray(FifthPage.idArray, FifthPage.idArray.size());
        query.addValueEventListener(valueEventListener);

        //Pass data to placeDescription Class.
        listViewPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlaceDescription.class);
                Museum museum = (Museum) listViewPlace.getItemAtPosition(position);
                intent.putExtra("title", museum.getTitle());
                intent.putExtra("description", museum.getDescription());
                intent.putExtra("latitude", museum.getLatitude());
                intent.putExtra("longitude", museum.getLongitude());
                intent.putExtra("imageurl", museum.getImageUrl());
                intent.putExtra("RestaurantName", museum.getRestaurantName());
                intent.putExtra("Rest_lat", museum.getRestaurantlatitude());
                intent.putExtra("Rest_long", museum.getRestaurantlongitude());
                startActivity(intent);
            }
        });
        //getAlert();
        return root;
    }

    public static List<Museum> getMuseumList() {
        return museumList;
    }
    //Collect museums according to their id from database.
    public void getDataWithIdArray(final List<Integer> id, final int arraySize) {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                museumList.clear();
                for (int i = 0; i < arraySize; i++) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Museum museum = snapshot.getValue(Museum.class);
                        museum.setTimeSchedule(FifthPage.scheduledArray.get(i));
                        if (museum.getId() == id.get(i)) {
                            museumList.add(museum);
                        }
                    }
                }
                adapter = new MuseumList(getContext(), museumList);
                listViewPlace.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


    }


}
