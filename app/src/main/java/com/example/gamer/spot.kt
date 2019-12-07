package com.example.gamer

data class Spot(
        val id: Int = counter++.toInt(),
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