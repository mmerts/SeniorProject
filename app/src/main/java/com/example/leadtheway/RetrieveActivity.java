package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrieveActivity extends AppCompatActivity {


    List<Museum> museumList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ChildEventListener mChildListener;

    ListView listViewPlace;
    private ValueEventListener valueEventListener;
    int [] myArray = {4,2,3,1};
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        museumList = new ArrayList<Museum>();


        listViewPlace = (ListView) findViewById(R.id.listViewPlaces);

        FirebaseUtil.openFbReference("places");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;

        query = FirebaseUtil.mDatabaseReference.child("Amsterdam").child("Museum").orderByChild("id");
        getDataWithIdArray(myArray,4);
        query.addValueEventListener(valueEventListener);



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
                MuseumList adapter = new MuseumList(RetrieveActivity.this, museumList);
                listViewPlace.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

}
