package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CityButton extends AppCompatActivity {

    private DatabaseReference mdatabase;
Button country1, country2, country3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_button);



        country1 = (Button)findViewById(R.id.select1);
        country2 = (Button)findViewById(R.id.select2);
        country3 = (Button)findViewById(R.id.select3);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Countries");
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String c1 = dataSnapshot.child("Countryid1").getValue().toString();
                String c2 = dataSnapshot.child("Countryid2").getValue().toString();
                String c3 = dataSnapshot.child("Countryid3").getValue().toString();
                country1.setText(c1);

                country2.setText(c2);
                country3.setText(c3);
            }





            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        country1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdatabase = FirebaseDatabase.getInstance().getReference().child("Cities");
                mdatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String c1 = dataSnapshot.child("Countryid1").getValue().toString();
                        String c2 = dataSnapshot.child("Countryid2").getValue().toString();
                        String c3 = dataSnapshot.child("Countryid3").getValue().toString();
                        country1.setText(c1);

                        country2.setText(c2);
                        country3.setText(c3);
                    }





                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }


        );





    }




}
