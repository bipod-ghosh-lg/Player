package com.example.player.ui.components


data class Comments(
    val videoId: String,
    val username: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()

)