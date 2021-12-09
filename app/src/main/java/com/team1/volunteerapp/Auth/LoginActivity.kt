package com.team1.volunteerapp.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.BtnLoadingProgressbar
import com.team1.volunteerapp.R
import com.team1.volunteerapp.Loading.SplashActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val handler = Handler()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.emailEditArea_login)
        val password = findViewById<EditText>(R.id.passwordEditArea_login)

        activity_login_btn.setOnClickListener {
            val progressbar = BtnLoadingProgressbar(it)
            val inputemail = email.text.toString()
            val inputpassword = password.text.toString()
            var isGoToJoin = true

            if (inputemail.isEmpty()) {
                Toast.makeText(this, "이메일을 입력하지 않았습니다", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else if (inputpassword.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력하지 않았습니다", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else {
                if (isGoToJoin) {
                    auth.signInWithEmailAndPassword(inputemail, inputpassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                progressbar.setLoading()
                                handler.postDelayed({
                                    progressbar.setState(true){
                                        handler.postDelayed({
                                            val intent = Intent(this, SplashActivity::class.java)
                                            finishAffinity()
                                            startActivity(intent)
                                            finish()

                                        }, 1500)
                                    }
                                }, 2000)

                            } else {
                                startError(progressbar)
                            }
                        }
                }

            }
        }
    }
    private fun startError(progressbar: BtnLoadingProgressbar) {
        progressbar.reset()
        handler.postDelayed({
            progressbar.setLoading()
            handler.postDelayed({
                progressbar.setState(false) {
                    handler.postDelayed({
                        progressbar.reset()
                    }, 1500)
                }
            }, 2000)
        }, 600)
    }
}