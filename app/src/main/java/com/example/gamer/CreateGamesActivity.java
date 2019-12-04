package com.example.gamer;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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


public class CreateGamesActivity extends AppCompatActivity {

    //Location of the user
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;

    private double userLatitude;
    private double userLongitude;

    private GoogleMap map;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategames);

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

        Button createGame = findViewById(R.id.createGame);

        createGame.setOnClickListener(unused -> {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
}
