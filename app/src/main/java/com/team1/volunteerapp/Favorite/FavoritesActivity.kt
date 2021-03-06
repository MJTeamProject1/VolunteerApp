package com.team1.volunteerapp.Favorite

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.AboutViewActivity
import com.team1.volunteerapp.utils.BottomNavDrawerFragment
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_home.*

class FavoritesActivity : AppCompatActivity() {

    val favorite_items = mutableListOf<String>()

    lateinit var favorite_rvAdapter : FavoriteRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(bottomAppBar)

        // 하단 바
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

        //RecyclerView Adapter 연결
        val favorite_rv = findViewById<RecyclerView>(R.id.mRecyclerViewFavorite)

        favorite_rvAdapter = FavoriteRVAdapter(favorite_items)

        favorite_rv.adapter = favorite_rvAdapter
        favorite_rv.layoutManager = LinearLayoutManager(this)

        //구분선 넣기
        val dividerItemDecoration =
            DividerItemDecoration(favorite_rv.context, LinearLayoutManager(this).orientation)

        favorite_rv.addItemDecoration(dividerItemDecoration)

        //홈으로 가기
        val homeButton = findViewById<FloatingActionButton>(R.id.fab)
        homeButton.setOnClickListener { // 홈으로 돌아가기
            fab.hide(AnimationB.addVisibilityChanged)
            Handler().postDelayed({
                finish()
            }, 300)
        }


        //realtimeDB에서 즐겨찾기된 부분 가져오기
        favorite_items.clear()
        getfavoriteData()

        favorite_rvAdapter.itemClick = object : FavoriteRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                //눌렀을때 어떻게 할지
                val intent = Intent(favorite_rv.context, AboutViewActivity::class.java)
                intent.putExtra("num", favorite_items[position].toString())
                ContextCompat.startActivity(favorite_rv.context, intent, null)
            }
        }

    }


    private fun getfavoriteData(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(dataModel in snapshot.children){
                    favorite_items.add(dataModel.key.toString())
                }
                favorite_rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.favoriteRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu_favorite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> BottomNavDrawerFragment().show(supportFragmentManager,
                BottomNavDrawerFragment().tag)
            R.id.app_bar_all_delete -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("모두 삭제")
                    builder.setMessage("정말로 모두 삭제 하시겠습니까?")
                    builder.setNegativeButton("취소"
                    ) { dialogInterface: DialogInterface?, i: Int ->
                        //아무런 동작도 하지 않음
                    }
                builder.setPositiveButton("확인"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    FBRef.favoriteRef.child(FBAuth.getUid()).removeValue()
                    favorite_rvAdapter.notifyDataSetChanged()
                    finish()
                    Toast.makeText(this, "삭제를 완료하였습니다.", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
        }
        favorite_rvAdapter.notifyDataSetChanged()
        return true
    }
}