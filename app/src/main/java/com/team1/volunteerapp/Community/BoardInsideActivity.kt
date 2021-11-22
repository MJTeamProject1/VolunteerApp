package com.team1.volunteerapp.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.Comment.CommentLVAdapter
import com.team1.volunteerapp.Comment.CommentModel
import com.team1.volunteerapp.R
import com.team1.volunteerapp.databinding.ActivityBoardInsideBinding
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var key : String
    private lateinit var commentAdapter: CommentLVAdapter
    private val commentDataList = mutableListOf<CommentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isreview = false
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        key = intent.getStringExtra("key").toString()
        isreview = intent.getBooleanExtra("review",false)

        getBoardData(key,isreview)

        binding.boardDelBtn.setOnClickListener {
            if(isreview){
                FBRef.reviewRef.child(key).removeValue()
                Toast.makeText(this,"삭제 완료", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                FBRef.communityRef.child(key).removeValue()
                Toast.makeText(this,"삭제 완료", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commnetLV.adapter = commentAdapter

        getCommentData(key)
    }

    fun getCommentData(key : String){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                commentDataList.clear()
                
                for(dataModel in snapshot.children){
                    Log.d("asvv",dataModel.toString())

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)

                }

                commentAdapter.notifyDataSetChanged()


            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }

    fun insertComment(key : String){
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getTime()
                )
            )
        binding.commentArea.setText("")
    }

    private fun getBoardData(key: String, isreview: Boolean){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val dataModel = snapshot.getValue(BoardModel::class.java)

                    binding.titleArea.text = dataModel!!.title
                    binding.uidArea.text = dataModel!!.nickname
                    binding.contentArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    if(myUid.equals(writerUid)){
                        //Toast.makeText(baseContext,"작성자 본인", Toast.LENGTH_SHORT).show()
                        binding.boardDelBtn.isVisible = true
                    }else{
                        //Toast.makeText(baseContext,"작성자가 아님", Toast.LENGTH_SHORT).show()
                    }

                }catch (e:Exception){
                    Log.d("asdf","삭제완료")
                }


            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        if(isreview){
            FBRef.reviewRef.child(key).addValueEventListener(postListener)
        }
        else {
            FBRef.communityRef.child(key).addValueEventListener(postListener)
        }
    }
}