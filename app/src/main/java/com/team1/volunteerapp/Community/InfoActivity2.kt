package com.team1.volunteerapp.Community

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.team1.volunteerapp.R

class InfoActivity2 : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rev_info)

        val makeName : TextView = findViewById(R.id.tvInfoMakeTitleRev)
        val makeNick : TextView = findViewById(R.id.tvInfoMakeNickRev)
        val makeCont : TextView = findViewById(R.id.tvInfoMakeContRev)

        val bundle : Bundle?=intent.extras
        val makename = bundle!!.getString("maketitler")
        val makenick = bundle!!.getString("makenickr")
        val makecont = bundle!!.getString("makecontr")

        makeName.text = makename
        makeNick.text = makenick
        makeCont.text = makecont

    }
}