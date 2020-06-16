package com.example.leadtheway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GiveFeedBack extends AppCompatActivity {

    EditText feedback_country;
    EditText feedback_city;
    Button send_feedback;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    public static int counter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feed_back);

        FirebaseUtil.openFbReference("feedback");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        feedback_country = (EditText) findViewById(R.id.feedback_Country);
        feedback_city = (EditText) findViewById(R.id.feedback_city);
        send_feedback = (Button) findViewById(R.id.send_feedback);

        send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFeedback();
            }
        });
    }

    private void pushFeedback() {
        String county = feedback_country.getText().toString();
        String city = feedback_city.getText().toString();
        Feedback feedBack = new Feedback(counter,county,city);
        mDatabaseReference.push().setValue(feedBack);
        counter++;
    }
}
