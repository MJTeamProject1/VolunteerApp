package com.team1.volunteerapp.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object{
        private val database = Firebase.database

        val favoriteRef = database.getReference("favorite_list")
    }
}