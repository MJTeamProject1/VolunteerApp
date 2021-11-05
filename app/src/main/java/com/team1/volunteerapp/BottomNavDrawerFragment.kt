package com.team1.volunteerapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team1.volunteerapp.Auth.IntroActivity
import kotlinx.android.synthetic.main.fragment_bottom_nav_drawer.*

class BottomNavDrawerFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_nav_drawer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigationView.setNavigationItemSelectedListener {
            dismiss()
            when (it.itemId) {
                R.id.nav1 -> {
                    //세팅 엑티비티 연결시켜야 됨
                }
                R.id.nav2 -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1365.go.kr/vols/main.do"))
                    startActivity(intent)
                }
                R.id.nav3 -> {
                    var emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rudtjr1206@naver.com", null))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(Intent.createChooser(emailIntent, ""))
                }
            }
            true
        }
    }
}