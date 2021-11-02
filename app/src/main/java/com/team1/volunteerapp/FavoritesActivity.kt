package com.team1.volunteerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val items = mutableListOf<String>()

        items.add("a")
        items.add("b")
        items.add("c")
        items.add("a")
        items.add("b")
        items.add("c")
        items.add("a")
        items.add("b")
        items.add("c")
        items.add("a")
        items.add("b")
        items.add("c")

        //RecyclerView Adapter 연결
        val favorite_rv = findViewById<RecyclerView>(R.id.mRecyclerViewFavorite)
        val favorite_rvAdapter = FavoriteRVAdapter(items)

        favorite_rv.adapter = favorite_rvAdapter
        favorite_rv.layoutManager = LinearLayoutManager(this)
    }
}