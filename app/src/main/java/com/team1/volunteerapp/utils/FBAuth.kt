package com.team1.volunteerapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {
    companion object{
        private val db = FirebaseFirestore.getInstance()
        private var vol_time: String? = null
        private var vol_title: String? = null
        private var vol_goaltime: String? = null
        private var vol_user: String? = null
        private var user_email: String? = null
        private var vol_name: String? = null
        private var user_sido: String? = null
        private var user_gugun: String? = null
        private var user_phone: String? = null


        private lateinit var auth: FirebaseAuth

        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

        fun getTime() : String{
            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentTime)

            return dateFormat
        }

        fun getUserData(num : Int) : String? {
            auth = Firebase.auth
            var result = ""
            db.collection("UserData")
                .get()
                .addOnSuccessListener { result ->
                    for (doc in result) {
                        if (doc["uid"] == auth.uid.toString()) {

                            if (doc["vol_time"] != null) {
                                vol_time = doc["vol_time"].toString()
                            }
                            if (doc["vol_title"] != null) {
                                vol_title = doc["vol_title"].toString()
                            }
                            if (doc["timedata"] != null) {
                                vol_goaltime = doc["timedata"].toString()
                            }
                            if (doc["nickname"] != null) {
                                vol_user = doc["nickname"].toString()
                            }
                            if (doc["email"] != null) {
                                user_email = doc["email"].toString()
                            }
                            if (doc["username"] != null) {
                                vol_name = doc["username"].toString()
                            }
                            if (doc["sidodata"] != null) {
                                user_sido = doc["sidodata"].toString()
                            }
                            if (doc["gugundata"] != null) {
                                user_gugun = doc["gugundata"].toString()
                            }
                            if (doc["phonenumber"] != null) {
                                user_phone = doc["phonenumber"].toString()
                            }
                        }
                    }
                }
            when(num){
                1-> result = vol_time.toString()
                2-> result = vol_title.toString()
                3-> result = vol_goaltime.toString()
                4-> result = vol_user.toString()
                5-> result = user_email.toString()
                6-> result = vol_name.toString()
                7-> result = user_sido.toString()
                8-> result = user_gugun.toString()
                9-> result = user_phone.toString()
            }
            return result
        }
    }
}
