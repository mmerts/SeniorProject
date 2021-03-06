package com.example.leadtheway.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment {

    GoogleMap googleMap;
    int distance, duration;

    private static final int PERMISSION_ID = 45;
    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LatLng currentPosition;
    private String museumlongitude;
    private String museumlatitude;
    private String museumTitle;
    private LatLng pos;
    //to draw path for all places in museumlist.
    private ArrayList<Pair<String, String>> waypoints;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        //Map Initialize
        MapView mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        //Map Initializer
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();

        //SetMarker
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
               // if connection completed successfully, map set all marker.
                // Set all marker when museumlist array fill.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14));
                        for (int i = 0; i < TimetableFragment.getMuseumList().size(); i++) {
                            museumlongitude = TimetableFragment.getMuseumList().get(i).getLongitude();
                            museumlatitude = TimetableFragment.getMuseumList().get(i).getLatitude();
                            museumTitle = TimetableFragment.getMuseumList().get(i).getTitle();
                            pos = new LatLng(Double.parseDouble(museumlatitude), Double.parseDouble(museumlongitude));
                            googleMap.addMarker(new MarkerOptions().position(pos).title(museumTitle).snippet(Integer.toString(i + 1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).setDraggable(true);
                        }
                        getDirection();
                    }
                }, 300);
            }
        });

        return root;
    }

    // to draw the path current location to destination
    private void getDirection() {
        //waypoints.add(new Pair<String, String>("41.118575","28.971152"));
        //waypoints.add(new Pair<String, String>("41.168575","28.971152"));
        //waypoints.add(new Pair<String, String>("41.198575","28.971152"));
        String url;
        String str_origin = "origin=" + String.format("%.4f", currentPosition.latitude) + "," + String.format("%.4f", currentPosition.longitude);
        StringBuilder str_dest = new StringBuilder("destination=");
        String mode = "mode=walking";
        StringBuilder waypoint = new StringBuilder("waypoints=");
        for (int i = 0; i < TimetableFragment.getMuseumList().size(); i++) {
            if (i == 0) {
                waypoint.append(TimetableFragment.getMuseumList().get(i).getLatitude() + "," + TimetableFragment.getMuseumList().get(i).getLongitude());
            } else if (i == TimetableFragment.getMuseumList().size() - 1)
                str_dest.append(TimetableFragment.getMuseumList().get(i).getLatitude() + "," + TimetableFragment.getMuseumList().get(i).getLongitude());
            else {
                waypoint.append("|").append(TimetableFragment.getMuseumList().get(i).getLatitude() + "," + TimetableFragment.getMuseumList().get(i).getLongitude());
            }
        }
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + waypoint;
        String output = "json";
        String key = "AIzaSyAzFWRC-8qhDaNLDl5kIo4xB6sIs3tUNhw";
        url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;

        System.out.println("------------------------------------------------------" + url);

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
                duration = parser.duration;

                //System.out.println("------------------------------------------------------" + distance);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points;
            PolylineOptions lineOptions;
            int[] colors = {Color.RED, Color.GREEN, Color.GRAY, Color.MAGENTA, Color.CYAN};
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
