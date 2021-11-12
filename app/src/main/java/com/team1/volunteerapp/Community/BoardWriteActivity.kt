package com.team1.volunteerapp.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team1.volunteerapp.R
import com.team1.volunteerapp.databinding.ActivityBoardWriteBinding
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_write)
        binding.writeBtn.setOnClickListener {

            val title = binding.boardEditTitle.text.toString()
            val contents = binding.boardEditContents.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d("asdfasdf", title)
            Log.d("asdfasdf", contents)

            FBRef.communityRef
                .push()
                .setValue(BoardModel(title,contents,uid,time))

            Toast.makeText(this, "입력 완료", Toast.LENGTH_SHORT).show()

            finish()

        }
    }
}