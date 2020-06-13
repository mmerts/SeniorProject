package com.example.leadtheway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leadtheway.DirectionsJSONParser;
import com.example.leadtheway.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PlaceDescription extends AppCompatActivity {

    GoogleMap googleMap;
    private TextView textPlaceTitle;
    private TextView textPlaceDescription;
    private String title;
    private String description;
    private String latitude;
    private String longitude;
    int distance, duration;
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LatLng currentPosition;
    private static final int PERMISSION_ID = 45;
    ImageView imageView;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        readDisplayStateValue();

        textPlaceTitle = findViewById(R.id.placeTitle);
        textPlaceDescription = findViewById(R.id.p_Description);
        imageView = (ImageView) findViewById(R.id.image);

        displayPlace(textPlaceTitle, textPlaceDescription);
        showImage(imageUrl);
        MapView mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        //Map Initializer
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                mMap.setMyLocationEnabled(true);
                LatLng  pos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 14));
                mMap.addMarker(new MarkerOptions().position(pos).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDirection();
                    }
                }, 300);
            }
        });

    }

    void showImage(String url){
    if(url != null && url.isEmpty() == false){
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Picasso.with(this).load(url).resize(width,width*2/3).centerCrop().into(imageView);
    }
    }

    private void readDisplayStateValue() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        imageUrl = intent.getStringExtra("imageurl");
    }
    private void displayPlace(TextView textPlaceTitle, TextView textPlaceDescription) {

        textPlaceTitle.setText(title);
        textPlaceDescription.setText(description);

    }

    private void getDirection() {
        String url;
        String str_origin = "origin=" + Double.toString(currentPosition.latitude) + "," + Double.toString(currentPosition.longitude);
        String str_dest = "destination=" + latitude + "," + longitude;
        String mode = "mode=TRANSIT";
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String key = "AIzaSyAzFWRC-8qhDaNLDl5kIo4xB6sIs3tUNhw";
        url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;

        System.out.println("------------------------------------------------------" + url);

        new PlaceDescription.DownloadTask().execute(url);
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
            PlaceDescription.ParserTask parserTask = new PlaceDescription.ParserTask();
            parserTask.execute(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @SuppressLint("WrongThread")
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
                distance = parser.distance;
                duration = parser.duration;

                System.out.println("++++++++++++++++++++++++++++++++" + distance);
                TextView durationText = (TextView) findViewById(R.id.duration);
                TextView distanceText = (TextView) findViewById(R.id.distance);

                if(distance > 1000) {
                    int km = distance / 1000;
                    int metres = distance % 1000;
                    distanceText.setText("You are far way"+km+" km and "+metres+" metres.");
                }else{
                    distanceText.setText("You are far way"+distance+"metres.");
                }
                if(duration > 60)
                {
                    int minute = duration / 60;
                    int second = duration % 60;
                    durationText.setText("Your arrival time :"+minute+" minutes and "+second+" seconds.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points;
            PolylineOptions lineOptions;
            int[] colors = {Color.RED,Color.GREEN,Color.GRAY,Color.MAGENTA,Color.CYAN};
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(result.size() - 1);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }
                lineOptions.color(Color.MAGENTA);
                lineOptions.addAll(points);
                lineOptions.width(20);

                googleMap.addPolyline(lineOptions);
            }
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

}
