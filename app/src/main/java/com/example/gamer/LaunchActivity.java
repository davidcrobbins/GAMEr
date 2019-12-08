package com.example.gamer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

/**
 * hahahahahaha.
 */
public class LaunchActivity extends AppCompatActivity {
    /**
     * I do not enjoy this.
     * @param savedInstanceState gg.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Intent intent = new Intent(this, MainActivity.class);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) { // see below discussion
            startActivity(intent);
            finish();

        } else {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

// Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    1);
        }

        Button createGame = findViewById(R.id.goLogin);
        createGame.setOnClickListener(unused -> goLoginClicked());

        LatLng firstPoint = new LatLng(40.1119, 88.2282);
        LatLng secondPoint = new LatLng(40.1092, 88.2272);
        //Log.d("distance", "" + DistanceCalculator.distance(firstPoint, secondPoint));
        //System.out.println(DistanceCalculator.threshold(firstPoint));
        Log.d("threshold", "" + DistanceCalculator.threshold(firstPoint));
    }

    /**
     * Invoked by the Android system when a request launched by startActivityForResult completes.
     * @param requestCode the request code passed by to startActivityForResult
     * @param resultCode a value indicating how the request finished (e.g. completed or canceled)
     * @param data an Intent containing results (e.g. as a URI or in extras)
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(this, MainActivity.class);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) { // see below discussion
            startActivity(intent);
            finish();

        }
    }

    /**
     * hahahahahaha.
     */
    private void goLoginClicked() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                1);
    }
}
