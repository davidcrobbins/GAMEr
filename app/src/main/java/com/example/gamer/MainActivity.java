package com.example.gamer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    protected void onCreate() {
        setContentView(R.layout.activity_main);

        Button findGames = findViewById(R.id.findGames);

        findGames.setOnClickListener(unused -> {
            Intent intent = new Intent()

            startActivity(intent);
        });
    }
}
