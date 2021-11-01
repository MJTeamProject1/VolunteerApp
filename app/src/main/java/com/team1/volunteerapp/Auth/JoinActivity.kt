package com.team1.volunteerapp.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.HomeActivity
import com.team1.volunteerapp.R

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth


        val email = findViewById<EditText>(R.id.emailEditArea)
        val password = findViewById<EditText>(R.id.passwordEditArea)
        val joinbtn = findViewById<Button>(R.id.joinBtn)
        val password_check = findViewById<EditText>(R.id.password2EditArea)
        val username= findViewById<EditText>(R.id.nameEditArea)
        val phonenumber = findViewById<EditText>(R.id.phonenumberEditArea)
        val nickname = findViewById<EditText>(R.id.nicknameEditArea)



        joinbtn.setOnClickListener {
            var isGoToJoin = true

            val inputemail = email.text.toString()
            val inputpassword = password.text.toString()
            val password_check_db = password_check.text.toString()
            val username_db= username.text.toString()
            val phonenumber_db = phonenumber.text.toString()
            val nickname_db = nickname.text.toString()

            val userauth : HashMap<String, String> = hashMapOf(
                "email" to inputemail,
                "passworld" to inputpassword,
                "username" to username_db,
                "phonenumber" to phonenumber_db,
                "nickname" to nickname_db
            )

            if(inputemail.isEmpty()){
                Toast.makeText(this,"이메일을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(inputpassword.isEmpty()){
                Toast.makeText(this,"비밀번호를 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(password_check_db.isEmpty()){
                Toast.makeText(this,"비밀번호 확인을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(username_db.isEmpty()){
                Toast.makeText(this,"이름을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(phonenumber_db.isEmpty()){
                Toast.makeText(this,"번호를 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(nickname_db.isEmpty()){
                Toast.makeText(this,"닉네임을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(inputpassword.length < 6){
                Toast.makeText(this,"비밀번호를 6자리 이상 눌러주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else {
                if (isGoToJoin) {
                    //Toast.makeText(this,email.text.toString(),Toast.LENGTH_SHORT).show()
                    auth.createUserWithEmailAndPassword(inputemail, inputpassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                                // Firebase Firestore db에 넣기
                                db.collection("UserData")
                                    .add(userauth)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("TAGMESSAGE", "DocumentSnapshot added with ID: ${documentReference.id}")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("TAGMESSAGE", "Error adding document", e)
                                    }




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
    }
}
