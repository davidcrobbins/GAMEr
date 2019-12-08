package com.example.gamer;

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

        EditText proximityThresholdText = findViewById(R.id.proximityThreshold);
        String threshold = proximityThresholdText.getText().toString();
        int result = Integer.parseInt(threshold);

        proximityThreshold = result;

    }
}
