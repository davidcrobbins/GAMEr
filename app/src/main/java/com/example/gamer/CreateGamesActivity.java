package com.example.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class CreateGamesActivity extends AppCompatActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategames);

        Button createGame = findViewById(R.id.createGame);

        createGame.setOnClickListener(unused -> {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
