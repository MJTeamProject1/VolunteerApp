package com.team1.volunteerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Auth.IntroActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    var sido : String? = null
    var gugun : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = Firebase.auth
         println("================================="+auth.currentUser?.uid)

        db.collection("UserData")
            .get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    if (doc["uid"] == auth.uid.toString()) {
                        sido = doc["sidodata"].toString()
                        gugun = doc["gugundata"].toString()
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("++++++++++++++++++실패+++++++++")
            }

        if (auth.currentUser?.uid == null) {
            // 회원가입이 안되있으므로 IntroActivity
            Handler().postDelayed({
                val Intent = Intent(this, IntroActivity::class.java)
                startActivity(Intent)
                finish()
            }, 3000)
        } else {
            // 회원가입이 되어있으므로 HomeActivity
            Handler().postDelayed({
//                val Intent = Intent(this, HomeActivity::class.java)
                val dataintent = Intent(this, LoadingActivity::class.java)
                dataintent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                dataintent.putExtra("sido",sido)
                dataintent.putExtra("gugun",gugun)

                startActivity(dataintent)
                finish()
            }, 3000)
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

}