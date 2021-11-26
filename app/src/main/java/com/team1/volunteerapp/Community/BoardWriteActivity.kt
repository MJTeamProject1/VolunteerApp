package com.team1.volunteerapp.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_home.*

class BoardWriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)
        setSupportActionBar(bottomAppBar)

        // 하단 바
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

        // 후기 선택 시
        val checkbox = findViewById<CheckBox>(R.id.checkreview)
        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->   }

        val writeBtn = findViewById<FloatingActionButton>(R.id.fab)
        writeBtn.setOnClickListener {
            val title = findViewById<EditText>(R.id.boardEditTitle)
            val contents = findViewById<EditText>(R.id.boardEditContents)

            val inputTitle = title.text.toString()
            val inputContents = contents.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            val inputnickname = FBAuth.getUserData(4) // Firebase가 데이터를 불러오는데 시간이 걸림 그래서 2번 실행
            val inputnickname2 = FBAuth.getUserData(4)
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
                            .setValue(BoardModel(inputTitle,inputContents,uid,time,inputnickname2))

                        Toast.makeText(this, "입력 완료", Toast.LENGTH_SHORT).show()

                        fab.hide(AnimationB.addVisibilityChanged)
                        Handler().postDelayed({
                            finish()
                        }, 300)
                    }
                    else{
                        FBRef.communityRef
                            .push()
                            .setValue(BoardModel(inputTitle,inputContents,uid,time,inputnickname2))

                        Toast.makeText(this, "입력 완료", Toast.LENGTH_SHORT).show()

                        fab.hide(AnimationB.addVisibilityChanged)
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
                fab.hide(AnimationB.addVisibilityChanged)
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