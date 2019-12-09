package com.example.gamer;

import android.content.Intent;
import android.graphics.Color;
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

        //Get user, set up most outer parent, and remove all views previously there.
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        LinearLayout parentForGames = findViewById(R.id.parentForManagingGames);
        parentForGames.removeAllViews();

        //Look through all of the games that have been retreived by the app
        for (Game game: games) {

            //make sure android doesn't have a heart attack
            if (game == null) {
                Log.d("LookingFortheGlitchNull", user);
            }

            //if the owner is the current user, show them things
            if (game.owner.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {


                //set up the chunk to be accepted
                View gameChunk = getLayoutInflater().inflate(R.layout.chunk_managegames, parentForGames, false);
                LinearLayout chunkContainer = gameChunk.findViewById(R.id.manageChunkContainer);

                TextView wantText = gameChunk.findViewById(R.id.wantText);
                TextView acceptText = gameChunk.findViewById(R.id.acceptText);
                //Setting up the sub parents in the chunkception chunck
                LinearLayout parentForAccepted = gameChunk.findViewById(R.id.parentForAccepted);
                LinearLayout parentForToBeAccepted = gameChunk.findViewById(R.id.parentForToBeAccepted);



                if (game.users == null) {
                    continue;
                } else {
                    Log.d("LookingFortheGlitch", user);
                    for (Map.Entry<String, Users> entry : game.users.entrySet()) {
                        Log.d("Confusing", user + entry.getValue().user);


                        if (entry.getValue().state == PlayerState.rightSwipe) {

                            //set up child chunk
                            View toBeAcceptedChunk = getLayoutInflater().inflate(R.layout.chunk_peopletoaccept, parentForToBeAccepted, false);

                            //set up Views in child chunk
                            TextView toAccept = toBeAcceptedChunk.findViewById(R.id.toAccept);
                            Button accept = toBeAcceptedChunk.findViewById(R.id.accept);
                            Button decline = toBeAcceptedChunk.findViewById(R.id.decline);

                            //change text from database
                            toAccept.setText(entry.getValue().user);

                            //If owner accepts this person, make the UI change
                            accept.setOnClickListener(unused -> {
                                changeDB(user, PlayerState.Accepted, entry.getKey(), game.key);
                            });

                            //if owner declines this person, make the UI change
                            decline.setOnClickListener(unused -> {
                                changeDB(user, PlayerState.notComing, entry.getKey(), game.key);
                            });

                            //add child chunk to the parent chunk
                            parentForToBeAccepted.addView(toBeAcceptedChunk);
                        } else if (entry.getValue().state == PlayerState.Accepted) {
                            //setting up the other child chunk
                            View acceptedChunk = getLayoutInflater().inflate(R.layout.chunk_peopleaccepted, parentForAccepted, false);

                            //set up the view in this child chunk
                            TextView acceptedEmail = acceptedChunk.findViewById(R.id.accepted);

                            //change views and add this child chunk to parent chunck
                            acceptedEmail.setText(entry.getValue().user);
                            parentForAccepted.addView(acceptedChunk);
                        }


                    }
                }
                if (parentForAccepted.getChildCount() < 1) {
                    acceptText.setVisibility(View.GONE);
                }

                if (parentForToBeAccepted.getChildCount() < 1) {
                    wantText.setVisibility(View.GONE);
                }
                TextView manageGame = gameChunk.findViewById(R.id.manageGameName);
                manageGame.setText(game.name);
                //add everything into the main view
                if (parentForGames.getChildCount() % 2 == 0) {
                    chunkContainer.setBackgroundColor(Color.rgb(148, 192, 219));
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
