package com.example.gamer;

import com.google.android.gms.maps.model.LatLng;

public class DistanceCalculator {
    private static double proximityThreshold = 500;

    private static LatLng userLocation = new LatLng(40.1092, 88.2272);

    public static double distance(final LatLng one, final LatLng another) {
        final double latDistanceScale = 110574;
        final double lngDistanceScale = 111320;
        final double degToRad = Math.PI / 180;
        double latRadians = degToRad * one.latitude;
        double latDistance = latDistanceScale * (one.latitude - another.latitude);
        double lngDistance = lngDistanceScale * (one.longitude - another.longitude) * Math.cos(latRadians);
        return Math.sqrt(latDistance * latDistance + lngDistance * lngDistance);
    }

    public static boolean threshold(LatLng coordinate) {
        if (distance(userLocation, coordinate) < proximityThreshold) {
            return true;
        } else {
            return false;
        }
    }





}
