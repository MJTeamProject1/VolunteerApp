package com.team1.volunteerapp.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.R
import com.team1.volunteerapp.SplashActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.emailEditArea_login)
        val password = findViewById<EditText>(R.id.passwordEditArea_login)
        val loginbtn = findViewById<Button>(R.id.loginBtn)

        loginbtn.setOnClickListener {
            val inputemail = email.text.toString()
            val inputpassword = password.text.toString()
            var isGoToJoin = true

            if(inputemail.isEmpty()){
                Toast.makeText(this,"이메일을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            else if(inputpassword.isEmpty()){
                Toast.makeText(this,"비밀번호를 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else {
                if (isGoToJoin) {
                    auth.signInWithEmailAndPassword(inputemail, inputpassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()

                                val intent = Intent(this, SplashActivity::class.java)
                                // 기존 엑티비티를 다 날림
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }

    }
}