package com.example.gamer;

import com.android.volley.RequestQueue;

//Helper Class to allow commmunication with the JSON
public class WebApi {
    private static String webAPIBase = "https://www.boardgameatlas.com/api/search?name=";
    private static RequestQueue requestQueue;
    public static String gameList;




    public static void reset() {
        gameList = null;
    }
}