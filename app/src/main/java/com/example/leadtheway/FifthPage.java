package com.example.leadtheway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.leadtheway.ui.MapFragment;
import com.example.leadtheway.ui.PlaceDescription;
import com.example.leadtheway.ui.TimetableFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

//After get input from user application logic started.
public class FifthPage extends AppCompatActivity {
    //to reach user current location all 4 rows are needed.
    private static final int PERMISSION_ID = 45;
    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LatLng currentPosition;

    int distance = 0;

    private int startTime;
    private int endTime;
    private String userInterest;
    private String userTransportation;


    public static List<Integer> idArray;
    public static List<String> scheduledArray;
    private String selectedplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications)
                .build();
        //get user last location to calling getLastLocation
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        //get data from fourth page activity to fifth page in order to send cloud in the url as a parameter.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startTime = bundle.getInt("startTime");
            endTime = bundle.getInt("endTime");
            userInterest = bundle.getString("userInterest");
            userTransportation = bundle.getString("userTransport");
            selectedplace = bundle.getString("selectedPlace");
        }
        System.out.println("oooooo " + selectedplace);
        idArray = new ArrayList<Integer>();
        scheduledArray = new ArrayList<String>();

        //this method send connection to cloud.Parse the result of Cloud service and fill up idarray which museum place id and scheduled array which is their time.
        //for example we get 1 and "16:45-17:45" it means that the places id is 1 we collect from database and user can travel in 16:45-17:45
        getScheduleTime(startTime, endTime, userTransportation);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.signOut:
                logout();
                Toast.makeText(this, "Sign out completed", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //user can logout
    private void logout() {
        TimetableFragment.museumList.clear();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(FifthPage.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Cloud function connection url created.method get start time end time and transportation choice of user. Also connection gives user current location as a parameter.
    public void getScheduleTime(int startTime, int endTime, String transportation) {
        try {
            StringBuilder urlString = new StringBuilder("https://europe-west1-ltwapi.cloudfunctions.net/schedule?");
            urlString.append("start_time=");
            urlString.append(startTime);
            urlString.append("&");
            urlString.append("end_time=");
            urlString.append(endTime);
            urlString.append("&");
            urlString.append("currLoc_lat=");
            urlString.append(String.format("%.4f", currentPosition.latitude));
            urlString.append("&");
            urlString.append("currLoc_long=");
            urlString.append(String.format("%.4f", currentPosition.longitude));
            urlString.append("&");
            urlString.append("id_array=");
            urlString.append(selectedplace);
            urlString.append("&");
            urlString.append("transportation=");
            urlString.append(transportation);
            urlString.append("&");
            urlString.append("avgdistance=");
            urlString.append(50);
            urlString.append("&");
            urlString.append("avgtime=");
            urlString.append(50);


            URL url = new URL(urlString.toString());

            System.out.println("aaaaaaaaaaa " + urlString.toString());
            //using http request get the response
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //if connection is successful parse the response array.
            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                if ((line = reader.readLine()) != null) {
                    JSONArray jsonArray = new JSONArray(line);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray temp = jsonArray.getJSONArray(i);
                        idArray.add(Integer.parseInt(temp.getString(0)));
                        scheduledArray.add(temp.getString(1));
                        Toast.makeText(this, "You can see the map..", Toast.LENGTH_LONG).show();
                    }
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("***********" + e.toString());
            Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show();
        }
    }
    // in order to find user current location
    private void getDirection(final String latitude, final String longitude) {
        String url;
        String str_origin = "origin=" + Double.toString(currentPosition.latitude) + "," + Double.toString(currentPosition.longitude);
        String str_dest = "destination=" + latitude + "," + longitude;
        String mode = "mode=TRANSIT";
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String key = "AIzaSyAzFWRC-8qhDaNLDl5kIo4xB6sIs3tUNhw";
        url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;
        System.out.println("++++++++++++++++++" + url);
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

    //to reach user last location
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
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    //this method did not work.It design for alert to the user when the time comes.For example if travelling place 1 finish in 16:45 user get alert about next location and how many meter away to next location.
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
                        for (int i = 0; i < scheduledArray.size(); i++) {
                            try {
                                start_time.add(format.parse(scheduledArray.get(i).split("-")[0]));
                                end_time.add(format.parse(scheduledArray.get(i).split("-")[1]));
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
                        for (i = 0; i < start_time.size() - 1; i++) {
                            if (currentTime.before(start_time.get(i))) {
                                if (TimetableFragment.museumList.size() != 0) {
                                    while (currentPosition == null) {
                                    }
                                    getDirection(TimetableFragment.museumList.get(i).getLatitude(), TimetableFragment.museumList.get(i).getLongitude());
                                }
                                final String temp = start_time.get(i).getHours() + ":" + start_time.get(i).getMinutes() + "0";
                                final int finalI = i;

                                if (TimetableFragment.museumList.size() != 0 && distance != 0) {

                                    if (distance > 1000) {
                                        int km = distance / 1000;
                                        int metre = distance % 1000;

                                        Toast.makeText(FifthPage.this, "Next Station: " + TimetableFragment.museumList.get(finalI).getTitle() + " - " + temp + " -" + km + " km " + metre + " metres" + " you are far away ", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(FifthPage.this, "Next Station: " + TimetableFragment.museumList.get(finalI).getTitle() + " - " + temp + " -You are far away " + distance / 1000 + "km ", Toast.LENGTH_LONG).show();


                                    }
                                    cancel();
                                }
                                break;
                            } else if (currentTime.after(end_time.get(i)) && currentTime.before(start_time.get(i + 1))) {
                                if (TimetableFragment.museumList.size() != 0) {
                                    while (currentPosition == null) {
                                    }

                                    getDirection(TimetableFragment.museumList.get(i + 1).getLatitude(), TimetableFragment.museumList.get(i + 1).getLongitude());
                                }
                                final String temp = start_time.get(i + 1).getHours() + ":" + start_time.get(i + 1).getMinutes();
                                final int finalI = i;

                                if (TimetableFragment.museumList.size() != 0 && distance != 0) {

                                    if (distance > 1000) {
                                        int km = distance / 1000;
                                        int metre = distance % 1000;

                                        Toast.makeText(FifthPage.this, "Next Station: " + TimetableFragment.museumList.get(finalI).getTitle() + " - " + temp + " -" + km + " km " + metre + " metres" + " you are far away ", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(FifthPage.this, "Next Station: " + TimetableFragment.museumList.get(finalI).getTitle() + " - " + temp + " -You are far away " + distance / 1000 + "km ", Toast.LENGTH_LONG).show();

                                    }
                                    cancel();
                                }
                                break;
                            } else if (currentTime.after(end_time.get(i))) {
                                if (TimetableFragment.museumList.size() != 0) {
                                    while (currentPosition == null) {
                                    }
                                    getDirection(TimetableFragment.museumList.get(i).getLatitude(), TimetableFragment.museumList.get(i).getLongitude());
                                }
                                final String temp = start_time.get(i).getHours() + ":" + start_time.get(i).getMinutes() + "0";
                                final int finalI = i;

                                if (TimetableFragment.museumList.size() != 0 && distance != 0) {
                                    Toast.makeText(FifthPage.this, "Next Station: " + TimetableFragment.museumList.get(finalI).getTitle() + " - " + temp + " -", Toast.LENGTH_LONG).show();
                                    cancel();
                                }

                                break;

                            }
                        }

                    }
                }, 0, 1000);
            }
        };
        handler.post(runnable);
    }

}
