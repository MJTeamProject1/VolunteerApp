package com.team1.volunteerapp.Chat

import com.google.firebase.database.Exclude

data class ChatReadModel(
    val readuser : String = "",
    val read : Boolean = false
){
    @Exclude
    fun toMap() : Map<String, Any?> {
        return mapOf(
            "readuser" to readuser,
            "read" to read
        )
    }
}
