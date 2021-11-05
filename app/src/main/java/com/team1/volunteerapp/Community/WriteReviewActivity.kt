package com.team1.volunteerapp.Community

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.team1.volunteerapp.R
import com.team1.volunteerapp.databinding.ActivityWriteReviewBinding

class WriteReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteReviewBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_write_review)

        setContentView(binding.root)


        binding.btnRevOpinion.setOnClickListener {

            val r_title = binding.editTextRevTitle.text.toString()
            val r_contents = binding.editTextRevContents.text.toString()
            val r_nickname = binding.editTextRevNick.text.toString()


            val r_user = CUser(r_title, r_nickname, r_contents)
            if(r_title.isEmpty()){
                Toast.makeText(this,"제목을 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else if(r_nickname.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else if(r_contents.isEmpty()){
                Toast.makeText(this,"내용을 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else{
                database = FirebaseDatabase.getInstance().getReference("Review_list")
                database.push().setValue(r_user).addOnSuccessListener {

                    binding.editTextRevTitle.text.clear()
                    binding.editTextRevContents.text.clear()
                    binding.editTextRevNick.text.clear()

                    Toast.makeText(this,"Successfully Saved", Toast.LENGTH_SHORT).show()
                    finish()

                }.addOnFailureListener{
                    Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}