package com.team1.volunteerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Auth.JoinActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth
        // println("================================="+auth.currentUser?.uid)
        if (auth.currentUser?.uid == null) {
            // 회원가입이 안되있으므로 JoinActivity
            Handler().postDelayed({
                startActivity(Intent(this, JoinActivity::class.java))
                finish()
            }, 3000)
        } else {
            // 회원가입이 되어있으므로 HomeActivity

            Handler().postDelayed({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, 3000)
        }
    }
}