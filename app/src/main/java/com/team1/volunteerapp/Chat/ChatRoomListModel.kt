package com.team1.volunteerapp.Chat

import com.google.firebase.database.Exclude

data class ChatRoomListModel(
    val chatRoomNumber : Int = 0,
    val chatRoomMaxUnit : Int = 2,
    val chatRoomTitle : String = "",
    val chatRoomSubTitle : String = "",
    val chatRoomMakerUid : String = ""
){
    @Exclude
    fun toMap() : Map<String, Any> {
        return mapOf(
            "chatRoomNumber" to chatRoomNumber,
            "chatRoomMaxUnit" to chatRoomMaxUnit,
            "chatRoomTitle" to chatRoomTitle,
            "chatRoomSubTitle" to chatRoomSubTitle,
            "chatRoomMakerUid" to chatRoomMakerUid
        )
    }
}
