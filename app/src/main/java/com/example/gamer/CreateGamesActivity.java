package com.example.gamer;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
    private String webAPIBaseURL = "http://api.myjson.com/bins/q1ey4";

    //Hopoefully you can use Firebase
    private DatabaseReference mDatabase;

    //Volley do things
    private static String webAPIBase = "https://www.boardgameatlas.com/api/search?name=";
    private RequestQueue requestQueue;
    public static String gameList;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategames);

        //Create what needs to be made to get the user's current/Last known location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Log.d("weAreClose", "wereWe?");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            setUserLocation(location);
                            centerMap(getMap());
                            Log.d("weFuckedIt", "onSuccess: " + location.getLatitude() + " " + location.getLongitude());
                        } else {
                            setFakeLocation();
                            centerOnFakeLocation(getMap());
                            Log.d("weFakedIt", "onSuccess: ");
                        }
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
            writeGames();
            Log.d("GameToBeWritten", "One of these days");
        });
    }

    //sets the location of the user
    public void setUserLocation(Location currentLocation) {
        userLocation = currentLocation;
    }

    public void setFakeLocation() {
        userLatitude = 37.7601;
        userLongitude = 89.0773;
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
        //boundns of users last known location.

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

    private void writeGames() {
        String myKey = mDatabase.child("games").child("games").push().getKey();
        Game game = new Game("Looking for Gold", "John Shuster", "https://image.businessinsider.com/5a917d2eaae60527008b46d7?width=1100&format=jpeg&auto=webp", 40.1, 40.1, new ArrayList<Users>(), myKey);
        mDatabase.child("games").child("games").child(myKey).setValue(game);
    }
    private void readGames() {
        // My top posts by number of stars
        Query myTopPostsQuery = mDatabase.child("games").child("games");
            // TODO: implement the ChildEventListener methods as documented above
            // ...
            // My top posts by number of stars
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Game game = postSnapshot.getValue(Game.class);
                    Log.d("bioIsHere", game.bio);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                                setGameList("error");
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

    private void setGameList(String inputGame) {
        TextView gameName = findViewById(R.id.boardGameName);
        gameName.setText(inputGame);
    }
}
