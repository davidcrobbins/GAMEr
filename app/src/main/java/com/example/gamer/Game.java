package com.example.gamer;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public String key;

    //public String owner;
    public String bio;
    public String name;
    public String url;

    public double userLatitude;
    public double userLongitude;

    public List<Users> users;

    public Game() {

    }

    public Game(String bio, String game, String url, double userLatitude, double userLongitude, ArrayList<Users> users, String key) {

        //this.owner = owner;
        this.bio = bio;
        this.name = game;
        this.url = url;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;

        this.users = users;
        this.key = key;
    }
}
