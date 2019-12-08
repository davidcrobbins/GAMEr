package com.example.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.w("fuck12", "onCreate: for MainActivity ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initiallized buttons.
        Button findGames = findViewById(R.id.findGames);
        Button manageGames = findViewById(R.id.manageGames);
        Button yourGames = findViewById(R.id.yourGames);
        Button createGames = findViewById(R.id.createGames);
        Button settings = findViewById(R.id.settings);

        //Launch Find GameActivity when button pressed
        findGames.setOnClickListener(unused -> {
            Intent intent = new Intent(MainActivity.this, FindGamesActivity.class);

            //Send User Email to find Games
            //intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });

        //Launch the ManageGames Activity
        manageGames.setOnClickListener(unused -> {
            Intent intent = new Intent(this, FindGamesActivity.class);

            //Send User Email to find Games
            //intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });

        //Launch the Your Games Activity
        yourGames.setOnClickListener(unused -> {
            Intent intent = new Intent(this, FindGamesActivity.class);

            //Send User Email to find Games
            //intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });

        //Launch the Create Game Activity
        createGames.setOnClickListener(unused -> {
            Intent intent = new Intent(this, CreateGamesActivity.class);

            //Send User Email to find Games
            //intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });

        //Launch ProximityThreshold Activity
        settings.setOnClickListener(unused -> {
            Intent intent = new Intent(this, ProximityThresholdActivity.class);
            startActivity(intent);
        });
    }
}
