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
    private static final int PERMISSION_ID = 45;
    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LatLng currentPosition;
    int distance = 0;


    public static List<Integer> idArray;
    public static List<Museum> museumList;

    public  static String [] scheduleArray = {"10:00-10:30","10:30-11:00","13:00-13:11", "13:30-14:00","14:00-14:30","15:30-16:00","16:00-17:00"};

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
        idArray = new ArrayList<Integer>();

        listViewPlace = root.findViewById(R.id.listViewPlaces);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getScheduleTime();

                FirebaseUtil.openFbReference("places");
                mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
                query = FirebaseUtil.mDatabaseReference.child("Amsterdam").child("Museum").orderByChild("id");

                getDataWithIdArray(idArray, idArray.size());
                query.addValueEventListener(valueEventListener);
            }
        }, 500);




       listViewPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(getActivity(),PlaceDescription.class);
               Museum museum = (Museum) listViewPlace.getItemAtPosition(position);
               intent.putExtra("title",museum.getTitle());
               intent.putExtra("description",museum.getDescription());
               intent.putExtra("latitude", museum.getLatitude());
               intent.putExtra("longitude",museum.getLongitude());
               intent.putExtra("imageurl",museum.getImageUrl());
               startActivity(intent);
           }
       });
        //getAlert();
        return root;
    }

    public static List<Museum> getMuseumList() {
        return museumList;
    }

    public void getScheduleTime() {
        try {
            StringBuilder urlString = new StringBuilder("https://europe-west1-ltwapi.cloudfunctions.net/schedule?");
            urlString.append("start_time=");
            urlString.append(500);
            urlString.append("&");
            urlString.append("end_time=");
            urlString.append(1140);
            urlString.append("&");
            urlString.append("currLoc_lat=");
            urlString.append(currentPosition.latitude);
            urlString.append("&");
            urlString.append("currLoc_long=");
            urlString.append(currentPosition.longitude);

            URL url = new URL(urlString.toString());

            System.out.println("aaaaaaaaaaa " + urlString.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(12000);
            conn.setConnectTimeout(12000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                if ((line = reader.readLine()) != null) {
                    JSONArray jsonArray = new JSONArray(line);
                    for (int i=0; i < jsonArray.length(); i++) {
                        JSONArray temp = jsonArray.getJSONArray(i);
                        //System.out.println("*********************************************" + temp.getString(0) + ", " + temp.getInt(1));
                        idArray.add(Integer.parseInt(temp.getString(0)));
                    }
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("***********" + e.toString());
            Toast.makeText(getActivity(), "An error occurred!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataWithIdArray(final List<Integer> id, final int arraySize){
        listViewPlace.setAdapter(adapter);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                museumList.clear();
                for(int i=0;i<arraySize;i++) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren())
                    {
                        Museum museum = snapshot.getValue(Museum.class);
                        museum.setTimeSchedule(scheduleArray[i]);
                        if (museum.getId() == id.get(i)) {
                            museumList.add(museum);
                        }
                    }
                }
                adapter = new MuseumList(getContext(), museumList);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


    }
    private void getAlert() {
        Handler handler = new Handler();
        Runnable runnable;

        runnable = new Runnable() {
            @Override
            public void run() {
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        DateFormat format = new SimpleDateFormat("HH:mm");
                        final List<Date> start_time = new ArrayList<>(), end_time = new ArrayList<>();
                        for (int i=0; i<scheduleArray.length; i++) {
                            try {
                                start_time.add(format.parse(scheduleArray[i].split("-")[0]));
                                end_time.add(format.parse(scheduleArray[i].split("-")[1]));
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                        }

                        StringBuilder sb = new StringBuilder();
                        sb.append(Calendar.getInstance().getTime().getHours());
                        sb.append(":");
                        sb.append(Calendar.getInstance().getTime().getMinutes());

                        Date currentTime = new Date();

                        try {
                            currentTime = format.parse(sb.toString());
                        } catch (Exception e) {
                            System.err.println(e);
                        }


                        int i;
                        for (i=0; i<start_time.size()-1; i++) {
                            if(currentTime.before(start_time.get(i))){
                                if (museumList.size() != 0) {
                                    while (currentPosition == null) {
                                    }
                                    getDirection(museumList.get(i).getLatitude(), museumList.get(i).getLongitude());
                                }
                                final String temp = start_time.get(i).getHours() + ":" + start_time.get(i).getMinutes()+"0";
                                final int finalI = i;

                                        if (museumList.size() != 0 && distance != 0) {

                                            if(distance > 1000) {
                                                int km = distance /1000;
                                                int metre = distance %1000;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Warning!");
                                                String message = "\"Next Station is: \""+  museumList.get(finalI).getTitle() + " - " + temp + " -" + km+ " km "+metre+" metres"+" you are far away ";
                                                builder.setMessage(message);
                                                builder.show();
                                                //Toast.makeText(FifthPage.this, "Next Station: " + museumList.get(finalI).getTitle() + " - " + temp + " -" + km+ " km "+metre+" metres"+" you are far away ", Toast.LENGTH_LONG).show();
                                            }else
                                            {
                                                //Toast.makeText(FifthPage.this, "Next Station: " + museumList.get(finalI).getTitle() + " - " + temp + " -You are far away " + distance / 1000 + "km ", Toast.LENGTH_LONG).show();

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Warning!");
                                                String message = "Next Station is:" + museumList.get(finalI).getTitle() + " - " + temp + " -You are far away " + distance / 1000 + "km ";
                                                builder.setMessage(message);
                                                builder.show();
                                            }
                                            cancel();
                                        }

                                break;

                            }

                            else if (currentTime.after(end_time.get(i)) && currentTime.before(start_time.get(i+1))) {
                                if (museumList.size() != 0) {
                                    while (currentPosition == null) {
                                    }

                                    getDirection(museumList.get(i +1).getLatitude(), museumList.get(i +1).getLongitude());
                                }


                                final String temp = start_time.get(i+1).getHours() + ":" + start_time.get(i+1).getMinutes();
                                final int finalI = i;

                                        if (museumList.size() != 0 && distance != 0) {

                                            if(distance > 1000) {
                                                int km = distance /1000;
                                                int metre = distance %1000;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Warning!");
                                                String message = "\"Next Station is: \""+  museumList.get(finalI).getTitle() + " - " + temp + " -" + km+ " km "+metre+" metres"+" you are far away ";
                                                builder.setMessage(message);
                                                builder.show();
                                                //Toast.makeText(FifthPage.this, "Next Station: " + museumList.get(finalI).getTitle() + " - " + temp + " -" + km+ " km "+metre+" metres"+" you are far away ", Toast.LENGTH_LONG).show();

                                            }else
                                            {
                                                //Toast.makeText(FifthPage.this, "Next Station: " + museumList.get(finalI).getTitle() + " - " + temp + " -You are far away " + distance / 1000 + "km ", Toast.LENGTH_LONG).show();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Warning!");
                                                String message = "Next Station is:" + museumList.get(finalI).getTitle() + " - " + temp + " -You are far away " + distance / 1000 + "km ";
                                                builder.setMessage(message);
                                                builder.show();
                                            }
                                            cancel();
                                        }

                                break;
                            }else if(currentTime.after(end_time.get(i))){
                                if (museumList.size() != 0) {
                                    while (currentPosition == null) {
                                    }
                                    getDirection(museumList.get(i).getLatitude(), museumList.get(i).getLongitude());
                                }
                                final String temp = start_time.get(i).getHours() + ":" + start_time.get(i).getMinutes()+"0";
                                final int finalI = i;

                                        if (museumList.size() != 0 && distance != 0) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Warning!");
                                            String message = "Your travel finished.";
                                            builder.setMessage(message);
                                            builder.show();
                                            //Toast.makeText(FifthPage.this, "Next Station: " + museumList.get(finalI).getTitle() + " - " + temp + " -" + km+ " km "+metre+" metres"+" you are far away ", Toast.LENGTH_LONG).show();
                                            cancel();
                                        }

                                break;

                            }
                        }

                    }
                },0, 1000);
            }
        };
        handler.post(runnable);
    }
    private void getDirection(final String latitude, final String longitude) {
        String url;
        String str_origin = "origin=" + Double.toString(currentPosition.latitude) + "," + Double.toString(currentPosition.longitude);
        String str_dest = "destination=" + latitude + "," + longitude;
        String mode = "mode=bicycling";
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String key = "AIzaSyAzFWRC-8qhDaNLDl5kIo4xB6sIs3tUNhw";
        url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;
        System.out.println("++++++++++++++++++"+url);
        new DownloadTask().execute(url);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.e("Background Task", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
                distance = parser.distance;


            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
            );
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

}
