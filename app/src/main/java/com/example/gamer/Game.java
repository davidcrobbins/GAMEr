package com.example.gamer;

import java.util.Map;

public class Game {
    public String key;

    public String owner;
    public String bio;
    public String name;
    public String url;

    public double userLatitude;
    public double userLongitude;

    public Map<String, Users> users;

    public Game() {

    }

    public Game(String bio, String game, String url, double userLatitude, double userLongitude, Map<String, Users> users, String key, String owner) {

        //this.owner = owner;
        this.bio = bio;
        this.name = game;
        this.url = url;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;

        this.owner = owner;
        this.users = users;
        this.key = key;
    }
}
