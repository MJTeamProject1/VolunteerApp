package com.team1.volunteerapp.Community

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.team1.volunteerapp.R
import com.team1.volunteerapp.databinding.ActivityWriteCommBinding

class WriteCommActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteCommBinding
    private lateinit var database : DatabaseReference
//    var usernickname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteCommBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_write_comm)
        setContentView(binding.root)

//        usernickname = intent.getStringExtra("nickname")



        binding.btnCommOpinion.setOnClickListener {

            val c_title = binding.editTextCommTitle.text.toString()
            val c_contents = binding.editTextCommContents.text.toString()
            val c_nickname = binding.editTextCommNick.text.toString()


            val c_user = CUser(c_title, c_nickname, c_contents)
            if(c_title.isEmpty()){
                Toast.makeText(this,"제목을 입력하지 않았습니다",Toast.LENGTH_SHORT).show()
            }
            else if(c_nickname.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else if(c_contents.isEmpty()){
                Toast.makeText(this,"내용을 입력하지 않았습니다",Toast.LENGTH_SHORT).show()
            }
            else{
                database = FirebaseDatabase.getInstance().getReference("Community_list")
                database.push().setValue(c_user).addOnSuccessListener {

                    binding.editTextCommTitle.text.clear()
                    binding.editTextCommContents.text.clear()
                    binding.editTextCommNick.text.clear()

                    Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
                    finish()

                }.addOnFailureListener{
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}