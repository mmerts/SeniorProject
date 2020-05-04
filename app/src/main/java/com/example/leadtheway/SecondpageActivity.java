package com.example.leadtheway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondpageActivity extends AppCompatActivity {


    private Button buttonthirdpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);

        buttonthirdpage = (Button) findViewById(R.id.buttonNext);
        buttonthirdpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenThirdPage();

            }

        });

    }


    public void OpenThirdPage(){

        Intent intent = new Intent(this, ThirdpageActivity.class);
        startActivity(intent);
    }



}

