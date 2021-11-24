package com.team1.volunteerapp.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Comment.CommentLVAdapter
import com.team1.volunteerapp.Comment.CommentModel
import com.team1.volunteerapp.R
import com.team1.volunteerapp.databinding.ActivityBoardInsideBinding
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import java.lang.Exception
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.comment_list_item.view.*


class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var key : String
    private lateinit var commentAdapter: CommentLVAdapter
    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var auth: FirebaseAuth
    private var thumbicon = false
    private var count = 0
    private var uidlist =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        var isreview = false
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        key = intent.getStringExtra("key").toString()
        isreview = intent.getBooleanExtra("review",false)

        // 글 가져오기
        getBoardData(key,isreview)

        // 수정 버튼
        binding.boardSettingBtn.setOnClickListener {
            showDialog(isreview)
        }

        // 댓글 버튼
        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }


        // 추천 버튼

        binding.boardThumbBtn.setOnClickListener {
            if(!thumbicon){
                writeNewPost(key,count+1, FBAuth.getUid())
            }else{
                writeNewPost(key,count-1, FBAuth.getUid())

            }

        }

        // listview 연결
        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commnetLV.adapter = commentAdapter

        // 댓글 가져오기
        getCommentData(key)

//        binding.commnetLV.setOnClickListener {
//            val item = parent.getItemAtPosition(position) as commentDataList
//        }

        binding.commnetLV.onItemClickListener =
            OnItemClickListener { a_parent, a_view, a_position, a_id ->
                val item = commentAdapter.getItem(a_position)
                Log.d("asvv",item.toString())
            }
//        commentAdapter.itemClick = object : CommentLVAdapter.ItemClick{
//            override fun onClick(view: View, position: Int) {
//                Log.d("asvv",commentDataList[position].toString())
//                Log.d("asvvv", position.toString())
//            }
//
//        }
    }

    // 추천 기능
    private fun writeNewPost(key:String, thumbint: Int, uid : String) {
        val postValues = thumbint
        val checkUid = uidlist.contains(uid)
        if(!checkUid){
            uidlist = "$uidlist@$uid"
            val childUpdates = hashMapOf<String, Any>(
                "$key/thumbint" to postValues,
                "$key/thumblist" to uidlist
            )

            binding.boardThumbBtn.setImageResource(R.drawable.baseline_thumb_up_black_24)
            thumbicon = true

            FBRef.communityRef.updateChildren(childUpdates)
        }
        else{
            var removeUidList = uidlist.removeSuffix("@$uid")
            val childUpdates = hashMapOf<String, Any>(
                "$key/thumbint" to postValues,
                "$key/thumblist" to removeUidList
            )

            binding.boardThumbBtn.setImageResource(R.drawable.baseline_thumb_up_off_alt_black_24)
            thumbicon = false
            FBRef.communityRef.updateChildren(childUpdates)
        }
    }


    // 글 수정 삭제 다이얼로그
    private fun showDialog(isreview: Boolean){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.board_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.BoardEditBtn)?.setOnClickListener {
            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            intent.putExtra("review",isreview)
            startActivity(intent)
        }

        alertDialog.findViewById<Button>(R.id.BoardDelBtn)?.setOnClickListener {
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

    }

    // 댓글 가져오기
    private fun getCommentData(key : String){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                commentDataList.clear()
                
                for(dataModel in snapshot.children){
                    Log.d("asvvv",dataModel.toString())

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)


                    if(FBAuth.getUid() == item.commentUid) {
                        Log.d("asvvv", item.commentNickname)
                    }
                }

                commentDataList.reverse()
                commentAdapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }


    // 댓글 DB에 넣기
    private fun insertComment(key : String){
        if(binding.commentArea.text.toString() !=""){
            FBRef.commentRef
                .child(key)
                .push()
                .setValue(
                    CommentModel(
                        binding.commentArea.text.toString(),
                        FBAuth.getTime(),
                        FBAuth.getUserData(4), // 닉네임 불러오기
                        FBAuth.getUid()
                    )
                )
            binding.commentArea.setText("")
        } else{
            Toast.makeText(this,"내용을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
        }

    }

    // 작성 글 가져오기
    private fun getBoardData(key: String, isreview: Boolean){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val dataModel = snapshot.getValue(BoardModel::class.java)

                    binding.titleArea.text = dataModel!!.title
                    binding.uidArea.text = dataModel!!.nickname
                    binding.contentArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time
                    binding.boardThumbInt.text = dataModel!!.thumbint.toString()
                    count = dataModel!!.thumbint
                    uidlist = dataModel.thumblist
                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    if(myUid.equals(writerUid)){
                        //Toast.makeText(baseContext,"작성자 본인", Toast.LENGTH_SHORT).show()
                        binding.boardSettingBtn.isVisible = true
                    }else{
                        //Toast.makeText(baseContext,"작성자가 아님", Toast.LENGTH_SHORT).show()
                    }

                    // 아이콘 불러오기
                    thumbicon = uidlist.contains(FBAuth.getUid())
                    if(thumbicon){
                        binding.boardThumbBtn.setImageResource(R.drawable.baseline_thumb_up_black_24)}
                    else{
                        binding.boardThumbBtn.setImageResource(R.drawable.baseline_thumb_up_off_alt_black_24)
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