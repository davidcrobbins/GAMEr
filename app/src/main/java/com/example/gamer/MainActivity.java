package com.example.gamer;

import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {


    protected void onCreate() {
        setContentView(R.layout.activity_main);
        //Initiallized buttons.
        Button findGames = findViewById(R.id.findGames);
        Button manageGames = findViewById(R.id.manageGames);
        Button yourGames = findViewById(R.id.yourGames);
        Button createGames = findViewById(R.id.createGames);

        //Launch Find GameActivity when button pressed
        findGames.setOnClickListener(unused -> {
            Intent intent = new Intent(this, FindGamesActivity.class);

            //Send User Email to find Games
            intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });

        //Launch the ManageGames Activity
        manageGames.setOnClickListener(unused -> {
            Intent intent = new Intent(this, ManageGamesActivity.class);

            //Send User Email to find Games
            intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });

        //Launch the Your Games Activity
        yourGames.setOnClickListener(unused -> {
            Intent intent = new Intent(this, YourGamesActivity.class);

            //Send User Email to find Games
            intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });

        //Launch the Create Game Activity
        createGames.setOnClickListener(unused -> {
            Intent intent = new Intent(this, CreateGamesActivity.class);

            //Send User Email to find Games
            intent.putExtra("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

            startActivity(intent);
        });
    }
}
