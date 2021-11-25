package com.team1.volunteerapp.Chat

data class ChatRoomListModel(
    val chatRoomNumber : Int = 0,
    val chatRoomMaxUnit : Int = 2,
    val chatRoomTitle : String = "",
    val chatRoomSubTitle : String = "",
    val chatRoomMakerUid : String = ""
)
