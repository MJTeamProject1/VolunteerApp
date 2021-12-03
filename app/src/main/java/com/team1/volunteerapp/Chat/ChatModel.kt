package com.team1.volunteerapp.Chat

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatModel (
    val user : String = "",
    val uid : String = "",
    val text : String = "",
    val time: String = "",
    val readuser : ChatReadModel? = null,
    val readusercount : Int = 0,
){
    @Exclude
    fun toMap() : Map<String, Any?> {
        return mapOf(
            "user" to user,
            "uid" to uid,
            "text" to text,
            "time" to time,
            "readuser" to readuser,
        )
    }
}
