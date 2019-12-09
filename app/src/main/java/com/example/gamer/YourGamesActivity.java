package com.example.gamer;

;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class YourGamesActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(final Bundle SavedInstance) {
        //Set everything up
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_yourgames);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getGames(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    private void getGames(String user) {
        List<Game> games = new ArrayList<>();

        Query myTopPostsQuery = mDatabase.child("games").child("games");
        // ...
        // My top posts by number of stars
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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
        LinearLayout parentForGames = findViewById(R.id.yourGamesList);
        parentForGames.removeAllViews();

        for (Game game: games) {
            if (game == null) {
                Log.d("LookingFortheGlitchNull", user);
            }

            Log.d("LookingFortheGlitch", user);
            if (game.users == null) {
                continue;
            }
            for (Map.Entry<String, Users> entry: game.users.entrySet()) {
                Log.d("Confusing", user + entry.getValue().user);
                View gameChunk = getLayoutInflater().inflate(R.layout.chunk_yourgames, parentForGames, false);
                if (entry.getValue().user.equals(user)) {
                    if (entry.getValue().state == PlayerState.Accepted) {
                        Log.d("WTH", "WTH");

                        LinearLayout chunkContainer = gameChunk.findViewById(R.id.chunkForGames);

                        TextView owner = gameChunk.findViewById(R.id.owner);
                        TextView gameName = gameChunk.findViewById(R.id.gameName);
                        TextView coords = gameChunk.findViewById(R.id.coords);


                        owner.setText(String.format("Owner : %s", game.owner));
                        gameName.setText(game.name);
                        coords.setText(String.format("Locations: %s, %s", game.userLatitude, game.userLongitude));
                        if (parentForGames.getChildCount() % 2 == 0) {
                            chunkContainer.setBackgroundColor(Color.rgb(148, 192, 219));
                        }
                        //parentForGames.removeAllViews();
                        parentForGames.addView(gameChunk);
                        break;
                    }


                }

            }

        }
        Log.d("FFFFFF", "" + parentForGames.getChildCount());
    }
}
