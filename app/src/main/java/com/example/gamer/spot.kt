package com.example.gamer

data class Spot(
        val key: String,
        val name: String,
        val city: String,
        val url: String,
        val latitude: Double,
        val longitude: Double
) {
    companion object {
        private var counter = 0L
    }
}