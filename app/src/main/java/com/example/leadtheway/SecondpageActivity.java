package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondpageActivity extends AppCompatActivity {

    DatabaseReference mdatabase;
    Button buttonthirdpage, buttoncountry;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        buttoncountry = (Button) findViewById(R.id.buttonselectcoutry);
        buttonthirdpage = (Button) findViewById(R.id.buttonNext);

        buttonthirdpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenThirdPage();
            }
        });


        //when click country/city button go to citybutton page and selection of country then city
        buttoncountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondpageActivity.this, CityButton.class);
                startActivity(intent);
            }
        });




    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signout,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.signOut:
                logout();
                Toast.makeText(this,"Sign out completed",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SecondpageActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //City activity







    public void OpenThirdPage(){

        Intent intent = new Intent(this, ThirdpageActivity.class);
        startActivity(intent);
    }



}

