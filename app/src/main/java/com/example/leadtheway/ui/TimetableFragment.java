package com.example.leadtheway.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.leadtheway.FifthPage;
import com.example.leadtheway.FirebaseUtil;
import com.example.leadtheway.Museum;
import com.example.leadtheway.MuseumList;
import com.example.leadtheway.R;
import com.example.leadtheway.RetrieveActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimetableFragment extends Fragment {

    private static List<Museum> museumList;
    private FirebaseDatabase mFirebaseDatabase;// The entry point for accesing a Firebase Database
    private DatabaseReference mDatabaseReference;

    private ChildEventListener mChildListener;

    ListView listViewPlace;
    private ValueEventListener valueEventListener;
    int [] myArray = {4,2,3,1};

    String [] scheduleArray = {"10:00-12:00","13:00-14:00","16:00-17:00","19:00-21:00"};


    private Query query;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        museumList = new ArrayList<Museum>();


        listViewPlace = root.findViewById(R.id.listViewPlaces);

        FirebaseUtil.openFbReference("places");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        query = FirebaseUtil.mDatabaseReference.child("Amsterdam").child("Museum").orderByChild("id");
        getDataWithIdArray(myArray,4);
        query.addValueEventListener(valueEventListener);

        return root;
    }

    public void getDataWithIdArray(final int []id, final int arraySize){
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                museumList.clear();
                for(int i=0;i<arraySize;i++) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren())
                    {

                        Museum museum = snapshot.getValue(Museum.class);

                        if (museum.getId() == id[i])
                            museumList.add(museum);
                    }

                }
                MuseumList adapter = new MuseumList(getContext(), museumList);
                listViewPlace.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public static List<Museum> getMuseumList() {
        return museumList;
    }
}
