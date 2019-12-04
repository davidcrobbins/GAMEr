package com.example.gamer

data class Spot(
        val id: Long = counter++,
        val name: String,
        val city: String,
        val url: String,
        val idNumber: Long
) {
    companion object {
        private var counter = 0L
    }
}