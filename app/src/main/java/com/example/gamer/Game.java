package com.example.gamer;

public class Game {
    public int idNumber;

    //public String owner;
    public String bio;
    public String name;
    public String url;

    //public double userLatitude;
    //public double userLongitude;

    public Game() {

    }

    public Game(String bio, String game, String url, int idNumber) {

        //this.owner = owner;
        this.bio = bio;
        this.name = game;
        this.url = url;
        //this.userLatitude = userLatitude;
        //this.userLongitude = userLongitude;

        this.idNumber = idNumber;
    }
}
