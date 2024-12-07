package com.babumusai.sparkleai.model

data class Message(
    var content: String,
//    val imageUrl: String?,
    val timestamp: String,
    val isUser: Boolean
)