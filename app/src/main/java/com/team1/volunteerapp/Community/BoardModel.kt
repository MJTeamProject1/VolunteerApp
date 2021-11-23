package com.team1.volunteerapp.Community

import com.google.firebase.database.Exclude

data class BoardModel(
    val title: String = "",
    val content: String = "",
    val uid: String = "",
    val time: String = "",
    val nickname: String = "",
    val thumbint : Int = 0
){
    @Exclude
    fun toMap() : Map<String, Any> {
        return mapOf(
            "title" to title,
            "content" to content,
            "uid" to uid,
            "time" to time,
            "nickname" to nickname,
            "thumbint" to thumbint
        )
    }
}
