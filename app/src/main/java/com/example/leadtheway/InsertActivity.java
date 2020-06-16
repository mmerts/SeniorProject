package com.example.leadtheway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txtTitle;
    EditText txtExpectedTime;
    EditText txtOpenClosedTime;
    EditText txtLongitude;
    EditText txtLatitude;
    public static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);



        FirebaseUtil.openFbReference("places");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtExpectedTime = (EditText)findViewById(R.id.txtPrice);
        txtOpenClosedTime = (EditText)findViewById(R.id.txtDescription);
        txtLongitude = (EditText)findViewById(R.id.longitude);
        txtLatitude = (EditText)findViewById(R.id.latitude);

    }

    @Override
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
                saveMuseum();
                Toast.makeText(this,"Places saved",Toast.LENGTH_LONG).show();
                clean();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void retriveActivity() {
        Intent intent = new Intent(InsertActivity.this, RetrieveActivity.class);
        startActivity(intent);
    }

    private void clean() {
    txtTitle.setText("");
    txtExpectedTime.setText("");
    txtOpenClosedTime.setText("");
    txtTitle.requestFocus();
    }

    private void saveMuseum() {
        String title = txtTitle.getText().toString();
        String expectedTime = txtExpectedTime.getText().toString();
        String openClosedTime = txtOpenClosedTime.getText().toString();
        String longitude = txtLongitude.getText().toString();
        String latitude = txtLatitude.getText().toString();
       // Museum museum = new Museum(counter,title,expectedTime,openClosedTime,"",longitude,latitude,"");
        //mDatabaseReference.child("Amsterdam").child("Museum").push().setValue(museum);
        counter++;
    }
}
