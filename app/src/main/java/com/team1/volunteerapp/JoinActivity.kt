package com.team1.volunteerapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.emailEditArea)
        val password = findViewById<EditText>(R.id.passwordEditArea)
        val joinbtn = findViewById<Button>(R.id.joinBtn)

        joinbtn.setOnClickListener {
            //Toast.makeText(this,email.text.toString(),Toast.LENGTH_SHORT).show()
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val Intent = Intent(this, HomeActivity::class.java)
                        startActivity(Intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }
}
