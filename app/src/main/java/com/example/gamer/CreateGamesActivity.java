package com.example.gamer;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class CreateGamesActivity extends AppCompatActivity {

    //Utilities used for the finding of the User's location.
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;

    //selected/retrieved coordinates for the creation of the game.
    private double userLatitude;
    private double userLongitude;

    //The creation map
    private GoogleMap map;

    //Base URLS for the different WEB functions
    //private String webAPIBaseURL = "http://api.myjson.com/bins/q1ey4";

    //Hopoefully you can use Firebase
    private DatabaseReference mDatabase;

    //Volley do things
    private static String webAPIBase = "https://www.boardgameatlas.com/api/search?name=";
    private RequestQueue requestQueue;
    public static String gameList;

    private LocationCallback locationCallback;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategames);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //mLocationSettingsRequest = builder.build();
        Log.d("weAreClose", "wereWe?");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            setUserLocation(location);
                            centerMap(getMap());
                            Log.d("weIt", "onSuccess: " + location.getLatitude() + " " + location.getLongitude());
                        } else {
                            setFakeLocation();
                            centerOnFakeLocation(getMap());

                            AlertDialog alertDialog = new AlertDialog.Builder(CreateGamesActivity.this).create();
                            alertDialog.setTitle("Current Location could not be found.");
                            alertDialog.setMessage("Your current location could not be found, please hold down on the location of the game on the map.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            Log.d("FGFGFG", "FGFGFG");
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("weFakedIt", "gg sir");
                        setFakeLocation();
                        centerOnFakeLocation(getMap());

                        AlertDialog alertDialog = new AlertDialog.Builder(CreateGamesActivity.this).create();
                        alertDialog.setTitle("Current Location could not be found.");
                        alertDialog.setMessage("Your current location could not be found, please hold down on the location of the game on the map.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        e.printStackTrace();
            }
        });

        //Setup Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SupportMapFragment createGameMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        createGameMapFragment.getMapAsync(newMap -> {
            map = newMap;

            if (map != null) {

                //this should remove the current marker and place the new marker on there.
                map.setOnMapLongClickListener(location -> {

                    map.clear();

                    //update position of user
                    userLatitude = location.latitude;
                    userLongitude = location.longitude;

                    //add marker
                    MarkerOptions options = new MarkerOptions().position(location);
                    Marker marker = map.addMarker(options);

                    BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                    marker.setIcon(icon);
                    //Do not center since the user is the one that added this and that may look a little weird.

                });
            }
        });


        //Creating buttons/ text view for this activity
        Button createGame = findViewById(R.id.createGame);
        Button search = findViewById(R.id.search);
        TextView gameName = findViewById(R.id.boardGameName);
        TextView bio = findViewById(R.id.bio);

        search.setOnClickListener(unused -> {
            String input = gameName.getText().toString();

            getNames(input);
        });
        createGame.setOnClickListener(unused -> {
            if (gameName.getText().toString().length() > 0 && !(gameName.getText().toString().equals("Game Name"))) {
                makeGames(gameName.getText().toString());
                Log.d("GameToBeWritten", "One of these days");
            }

        });
    }

    //sets the location of the user
    public void setUserLocation(Location currentLocation) {
        userLocation = currentLocation;
    }

    public void setFakeLocation() {
        userLatitude = 37.7601;
        userLongitude = -89.0773;
    }

    //sets a default location for map, in situations where the user's GPS isn't receiving location data.
    public void centerOnFakeLocation(final GoogleMap map) {
        LatLng userCoordinates = new LatLng(userLatitude, userLongitude);

        //Create the marker
        MarkerOptions options = new MarkerOptions().position(userCoordinates);
        Marker marker = map.addMarker(options);

        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
        marker.setIcon(icon);

        //move the camera to the marker position.
        map.moveCamera(CameraUpdateFactory.newLatLng(userCoordinates));
    }

    //center the Creattion map on the User's last known location.
    private void centerMap(final GoogleMap map) {
        //bounds of users last known location.

        //lat and lon of the user set for posting the JSON later.
        userLatitude = userLocation.getLatitude();
        userLongitude = userLocation.getLongitude();


        LatLng userCoordinates = new LatLng(userLatitude, userLongitude);

        //Create the Marker
        MarkerOptions options = new MarkerOptions().position(userCoordinates);
        Marker marker = map.addMarker(options);

        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
        marker.setIcon(icon);

        //Move Cmap to the correct position.
        map.moveCamera(CameraUpdateFactory.newLatLng(userCoordinates));
    }

    private GoogleMap getMap() {
        return map;
    }

    private void writeGames(String url) {
        //Setting up the text views to take the game names from
        TextView bio = findViewById(R.id.bio);
        TextView name = findViewById(R.id.boardGameName);

        //getting the game information
        String gameBio = bio.getText().toString();
        String gameName = name.getText().toString();

        //get the email of the person making the game
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //finding the URL of the game
        String myKey = mDatabase.child("games").child("games").push().getKey();
        Game game = new Game(gameBio, gameName, url, userLatitude, userLongitude, new HashMap<String, Users>(), myKey, email);
        mDatabase.child("games").child("games").child(myKey).setValue(game);

        //The Game has been posted, so go ahead and make it okay to go back to the main menu.
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("NothingHappeningHere", "User input did not meet the criteria");
        AlertDialog alertDialog = new AlertDialog.Builder(CreateGamesActivity.this).create();
        alertDialog.setTitle("YAY!");
        alertDialog.setMessage("Your game has been created! Please go back to the main menu.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Main Menu",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }
    private void readGames() {
        // My top posts by number of stars
        Query gamesQuery = mDatabase.child("games").child("games");

        gamesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Game game = postSnapshot.getValue(Game.class);
                    Log.d("bioIsHere", game.bio);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("F", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public void getNames(String userInput) {
        if (userInput == null || userInput.equals("Game Name") || userInput.length() < 1) {
            Log.d("Properly stopped the wrog thing", "Looks good");
            return;
        } else {

            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
                requestQueue.start();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, webAPIBase + userInput + "&limit=1&fuzzy_match=true&client_id=SB1VGnDv7M", null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("NoErrorsYet", "Somehow I manage");
                            try {
                                Log.d("we got 'em boys ", response.getJSONArray("games").getJSONObject(0).getString("name"));
                                //Get the JSONArray of the games from the search
                                String gameNames = response.getJSONArray("games").getJSONObject(0).getString("name");
                                setGameList(gameNames);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                //setGameList("error");

                                AlertDialog alertDialog = new AlertDialog.Builder(CreateGamesActivity.this).create();
                                alertDialog.setTitle("Whoopsies :(");
                                alertDialog.setMessage("We weren't able to find you game online");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "okay",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //setGameList("error");
                            AlertDialog alertDialog = new AlertDialog.Builder(CreateGamesActivity.this).create();
                            alertDialog.setTitle("Whoopsies :(");
                            alertDialog.setMessage("We weren't able to find you game online");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "okay",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void setGameList(String inputGame) {
        TextView gameName = findViewById(R.id.boardGameName);
        gameName.setText(inputGame);
    }

    private void makeGames(String userInput) {

        if (userInput == null || userInput.equals("Game Name") || userInput.length() < 1) {
            Log.d("Properly stopped the wrog thing", "Looks good");
            return;
        } else {

            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
                requestQueue.start();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, webAPIBase + userInput + "&limit=1&fuzzy_match=true&client_id=SB1VGnDv7M", null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("NoErrorsYet", "Somehow I manage");
                            try {
                                Log.d("we got 'em boys ", response.getJSONArray("games").getJSONObject(0).getString("name"));
                                //Get the JSONArray of the games from the search
                                String url = response.getJSONArray("games").getJSONObject(0).getJSONObject("images").getString("large");
                                writeGames(url);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                //setGameList("error");


                                AlertDialog alertDialog = new AlertDialog.Builder(CreateGamesActivity.this).create();
                                alertDialog.setTitle("Whoopsies :(");
                                alertDialog.setMessage("We weren't able to find you game online, do you want to proceed and use a basic image or change your game?");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "I have the right game",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                writeGames("https://media.wired.com/photos/5a0201b14834c514857a7ed7/master/w_2560%2Cc_limit/1217-WI-APHIST-01.jpg");
                                            }
                                        });
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel post",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            setGameList("error");
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        }
    }

}
