package com.example.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ManageGamesActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(final Bundle SavedInstance) {
        //Set everything up
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_managegames);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email == null) {
            Intent intent = new Intent(this, LaunchActivity.class);
            startActivity(intent);
        } else {
            getGames(email);
        }


    }

    private void getGames(String user) {
        List<Game> games = new ArrayList<>();

        Query getGamesList = mDatabase.child("games").child("games");

        // Retreiving the games that are currently in the database
        getGamesList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Game game = postSnapshot.getValue(Game.class);
                    games.add(game);
                }
                Log.d("GGGGGG", "" + games.size());
                writeGames(games);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("F", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    private void writeGames(List<Game> games) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        LinearLayout parentForGames = findViewById(R.id.parentForManagingGames);
        parentForGames.removeAllViews();

        for (Game game: games) {
            if (game == null) {
                Log.d("LookingFortheGlitchNull", user);
            }

            if (game.owner.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

                //set up the chunk
                View gameChunk = getLayoutInflater().inflate(R.layout.chunk_managegames, parentForGames, false);
                //Setting up the sub parents
                LinearLayout parentForAccepted = gameChunk.findViewById(R.id.parentForAccepted);
                LinearLayout parentForToBeAccepted = gameChunk.findViewById(R.id.parentForToBeAccepted);




                Log.d("LookingFortheGlitch", user);
                for (Map.Entry<String, Users> entry: game.users.entrySet()) {
                    Log.d("Confusing", user + entry.getValue().user);


                    if (entry.getValue().state == PlayerState.rightSwipe) {
                        View toBeAcceptedChunk = getLayoutInflater().inflate(R.layout.chunk_peopletoaccept, parentForToBeAccepted, false);

                        TextView toAccept = toBeAcceptedChunk.findViewById(R.id.toAccept);
                        Button accept = toBeAcceptedChunk.findViewById(R.id.accept);
                        Button decline = toBeAcceptedChunk.findViewById(R.id.decline);

                        toAccept.setText(entry.getValue().user);

                        accept.setOnClickListener(unused -> {
                            changeDB(user, PlayerState.Accepted, entry.getKey(), game.key);
                        });

                        decline.setOnClickListener(unused -> {
                            changeDB(user, PlayerState.notComing, entry.getKey(), game.key);
                        });

                        parentForToBeAccepted.addView(toBeAcceptedChunk);
                    } else if (entry.getValue().state == PlayerState.Accepted) {
                        //setting up the subviews
                        View acceptedChunk = getLayoutInflater().inflate(R.layout.chunk_peopleaccepted, parentForAccepted, false);

                        TextView acceptedEmail = acceptedChunk.findViewById(R.id.accepted);

                        acceptedEmail.setText(entry.getValue().user);
                        parentForAccepted.addView(acceptedChunk);
                    }
                }
                parentForGames.addView(gameChunk);
            }
        }
    }

    private void changeDB(String user, int newPlayerState, String key, String gameKey) {
        mDatabase.child("games").child("games").child(gameKey).child("users").child(key).child("state").setValue(newPlayerState);

        getGames(user);
    }
}
