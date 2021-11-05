package com.team1.volunteerapp.Community

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.team1.volunteerapp.R

class InfoActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comm_info)

        val makeName : TextView = findViewById(R.id.tvInfoMakeTitle)
        val makeNick : TextView = findViewById(R.id.tvInfoMakeNick)
        val makeCont : TextView = findViewById(R.id.tvInfoMakeCont)

        val bundle : Bundle?=intent.extras
        val makename = bundle!!.getString("maketitle")
        val makenick = bundle!!.getString("makenick")
        val makecont = bundle!!.getString("makecont")

        makeName.text = makename
        makeNick.text = makenick
        makeCont.text = makecont

    }
}