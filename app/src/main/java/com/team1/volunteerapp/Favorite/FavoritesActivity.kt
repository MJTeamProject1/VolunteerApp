package com.team1.volunteerapp.Favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.AboutViewActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef

class FavoritesActivity : AppCompatActivity() {
    val favorite_items = mutableListOf<String>()

    lateinit var favorite_rvAdapter : FavoriteRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        //RecyclerView Adapter 연결
        val favorite_rv = findViewById<RecyclerView>(R.id.mRecyclerViewFavorite)

        favorite_rvAdapter = FavoriteRVAdapter(favorite_items)

        favorite_rv.adapter = favorite_rvAdapter
        favorite_rv.layoutManager = LinearLayoutManager(this)

        //realtimeDB에서 즐겨찾기된 부분 가져오기
        getfavoriteData()

        favorite_rvAdapter.itemClick = object : FavoriteRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                //눌렀을때 어떻게 할지
                Toast.makeText(baseContext,"testmessage",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getfavoriteData(){

        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataModel in snapshot.children){
                    favorite_items.add(dataModel.key.toString())
                }
                println("---------------" + favorite_items.toString())
                favorite_rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.favoriteRef.child(FBAuth.getUid()).addValueEventListener(postListener)

    }
}