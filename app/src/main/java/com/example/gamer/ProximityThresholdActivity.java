package com.example.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class ProximityThresholdActivity extends AppCompatActivity {
    private static int proximityThreshold;

    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);


        Button accept = findViewById(R.id.accept);



        accept.setOnClickListener(unused -> {

                    EditText proximityThresholdText = findViewById(R.id.proximityThreshold);
                    String threshold = proximityThresholdText.getText().toString();
                    proximityThreshold = Integer.parseInt(threshold);
                    Log.d("proximity", "" + ProximityThresholdActivity.getProximityThreshold());
                    Log.d("threshold", "" + DistanceCalculator.threshold(new LatLng(40.1119, 88.2282)));
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);



                });

    }


    public static int getProximityThreshold() {
        return proximityThreshold;
    }

}
