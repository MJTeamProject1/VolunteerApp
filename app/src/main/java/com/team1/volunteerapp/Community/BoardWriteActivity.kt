package com.team1.volunteerapp.Community

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team1.volunteerapp.Profile.ProfileActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.databinding.ActivityBoardWriteBinding
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_home.*

class BoardWriteActivity : AppCompatActivity() {
    private val addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener =
        object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(fab: FloatingActionButton?) {
                super.onShown(fab)
            }

            @SuppressLint("NewApi")
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
//                fab?.show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)
        setSupportActionBar(bottomAppBar)

        val checkbox = findViewById<CheckBox>(R.id.checkreview)
        val writeBtn = findViewById<FloatingActionButton>(R.id.fab)

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->   }
        writeBtn.setOnClickListener {
            val title = findViewById<EditText>(R.id.boardEditTitle)
            val contents = findViewById<EditText>(R.id.boardEditContents)

            val inputTitle = title.text.toString()
            val inputContents = contents.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            var isGoToWrite = true

            if(inputTitle.isEmpty()){
                Toast.makeText(this,"제목을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToWrite = false
            }

            else if(inputContents.isEmpty()){
                Toast.makeText(this,"내용을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToWrite = false
            }
            else{
                if(isGoToWrite){
                    if(checkbox.isChecked){
                        FBRef.reviewRef
                            .push()
                            .setValue(BoardModel(inputTitle,inputContents,uid,time))

                        Toast.makeText(this, "입력 완료", Toast.LENGTH_SHORT).show()

                        fab.hide(addVisibilityChanged)
                        Handler().postDelayed({
                            finish()
                        }, 300)
                    }
                    else{
                        FBRef.communityRef
                            .push()
                            .setValue(BoardModel(inputTitle,inputContents,uid,time))

                        Toast.makeText(this, "입력 완료", Toast.LENGTH_SHORT).show()

                        fab.hide(addVisibilityChanged)
                        Handler().postDelayed({
                            finish()
                        }, 300)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> { // 홈으로 돌아가기
                fab.hide(addVisibilityChanged)
                Handler().postDelayed({
                    finish()
                }, 300)
            }
            R.id.app_bar_community_list ->{

            }
        }
        return true
    }

    override fun onStart() {
        // 애니메이션 작동
        super.onStart()
        Handler().postDelayed({
            fab.show()
        }, 450)
    }
}