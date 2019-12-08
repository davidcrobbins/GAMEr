package com.example.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProximityThresholdActivity extends AppCompatActivity {
    private int proximityThreshold;

    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);


        Button accept = findViewById(R.id.accept);



        accept.setOnClickListener(unused -> {

                    EditText proximityThresholdText = findViewById(R.id.proximityThreshold);
                    String threshold = proximityThresholdText.getText().toString();
                    proximityThreshold = Integer.parseInt(threshold);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                });


    }
}
