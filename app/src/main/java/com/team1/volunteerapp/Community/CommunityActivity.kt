package com.team1.volunteerapp.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team1.volunteerapp.R
import com.team1.volunteerapp.Fragment.PostActivityStateAdapter
import com.team1.volunteerapp.utils.AnimationB
import kotlinx.android.synthetic.main.activity_community2.*
import kotlinx.android.synthetic.main.activity_community2.fab
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.math.abs

class CommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community2)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        setSupportActionBar(bottomAppBar)

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val appbar = findViewById<AppBarLayout>(R.id.appbar)

        // 하단 바

        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

        // 뷰 페이지
        viewPager2.adapter = PostActivityStateAdapter(this)

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            if(position == 1){
                tab.text = "후기"
            }else{
                tab.text = "자유 게시판"
            }
        }.attach()

        var initTransitionY = tabLayout.translationY
        tabLayout.post {
            initTransitionY = tabLayout.translationY
        }

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            //Check if the view is collapsed
            if (abs(verticalOffset) >= appbar.totalScrollRange) {
                collapsingToolbar.title = ""
            } else {
                collapsingToolbar.title = ""
            }

            tabLayout.translationY =
                initTransitionY + initTransitionY * (verticalOffset / appBarLayout.totalScrollRange.toFloat())

        })

        // 글 쓰기 버튼
        val btnWriteComm = findViewById<FloatingActionButton>(R.id.fab)
        btnWriteComm.setOnClickListener {
            fab.hide(AnimationB.addVisibilityChanged)
            Handler().postDelayed({
                val intent = Intent(this, BoardWriteActivity::class.java)
                startActivity(intent)
            }, 300)

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