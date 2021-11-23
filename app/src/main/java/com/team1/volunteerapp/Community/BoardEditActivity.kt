package com.team1.volunteerapp.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.R
import com.team1.volunteerapp.databinding.ActivityBoardEditBinding
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef


class BoardEditActivity : AppCompatActivity() {
    private lateinit var key : String
    private lateinit var binding : ActivityBoardEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        var isreview = false

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        isreview = intent.getBooleanExtra("review",false)
        getBoardData(key,isreview)

        binding.boardEditBtn123.setOnClickListener {
            editBoardData(key,isreview)
        }
    }

    private fun editBoardData(key:String,isreview:Boolean){
        if(isreview){
            FBRef.reviewRef
                .child(key)
                .setValue(
                    BoardModel(
                        binding.boardEditTitle.text.toString(),
                        binding.boardEditContents.text.toString(),
                        FBAuth.getUid(),
                        FBAuth.getTime(),
                        FBAuth.getUserData(4)
                    )
                )
        }
        else{
            FBRef.communityRef
                .child(key)
                .setValue(
                    BoardModel(
                        binding.boardEditTitle.text.toString(),
                        binding.boardEditContents.text.toString(),
                        FBAuth.getUid(),
                        FBAuth.getTime(),
                        FBAuth.getUserData(4)
                    )
                )
        }

        Toast.makeText(this,"수정 완료", Toast.LENGTH_SHORT).show()

        finish()
    }

    private fun getBoardData(key:String, isreview: Boolean){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(BoardModel::class.java)
                Log.d("asdf", dataModel.toString())
                binding.boardEditTitle.setText(dataModel?.title)
                binding.boardEditContents.setText(dataModel?.content)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        if(isreview){
            FBRef.reviewRef.child(key).addValueEventListener(postListener)
        }
        else {
            FBRef.communityRef.child(key).addValueEventListener(postListener)
        }
    }
}