package com.team1.volunteerapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
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
        private var user_password: String? = null
        private var user_image: Bitmap? = null


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

        private val await = run{
                GlobalScope.launch {
                    launch {
                        auth = Firebase.auth
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
                                        if (doc["passworld"] != null) {
                                            user_password = doc["passworld"].toString()
                                        }
                                    }
                                }
                            }
                    }
                }

            }

        fun runImage(email : String?){
            val storageref = FirebaseStorage.getInstance().reference.child("images/${email}")
            val localfile = File.createTempFile("tempImage", "jpg")
            storageref.getFile(localfile).addOnSuccessListener {
                user_image = BitmapFactory.decodeFile(localfile.absolutePath)
            }
        }

        fun getUserImage() : Bitmap?{
            return  user_image
        }


        fun getUserData(num : Int) : String {
            print(await)

            var resultData = ""
            when(num){
                1-> resultData = vol_time.toString()
                2-> resultData = vol_title.toString()
                3-> resultData = vol_goaltime.toString()
                4-> resultData = vol_user.toString()
                5-> resultData = user_email.toString()
                6-> resultData = vol_name.toString()
                7-> resultData = user_sido.toString()
                8-> resultData = user_gugun.toString()
                9-> resultData = user_phone.toString()
                10-> resultData = user_password.toString()
            }

            Log.d("1231231231 ",resultData)

            return resultData
        }
    }
}
