package com.example.leadtheway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonsecondpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonsecondpage = (Button) findViewById(R.id.buttonmain);
        buttonsecondpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenSecondPage();

            }

        });

    }




    public void OpenSecondPage(){

        Intent intent = new Intent(this, SecondpageActivity.class);
        startActivity(intent);
    }



}

