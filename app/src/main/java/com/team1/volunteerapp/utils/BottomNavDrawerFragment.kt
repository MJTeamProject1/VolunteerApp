package com.team1.volunteerapp.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team1.volunteerapp.R
import com.team1.volunteerapp.SettingActivity
import kotlinx.android.synthetic.main.fragment_bottom_nav_drawer.*

class BottomNavDrawerFragment : BottomSheetDialogFragment() {
    private var emai : String? = null
    private var phonen : String? = null
    private var nickn : String? = null
    private var goalt : String? = null
    private var sidod: String? = null
    private var gugund: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_nav_drawer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigationView.setNavigationItemSelectedListener {
            dismiss()
            when (it.itemId) {
                R.id.nav2 -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1365.go.kr/vols/main.do"))
                    startActivity(intent)
                }
                R.id.nav3 -> {
                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rudtjr1206@naver.com", null))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(Intent.createChooser(emailIntent, ""))
                }
            }
            true
        }
    }

    fun setIntent(email : String?, phonenumber : String?, nickname : String?, goaltime : String?, sidodata : String?, gugundata : String?){
        emai = email
        phonen = phonenumber
        nickn = nickname
        goalt = goaltime
        sidod = sidodata
        gugund = gugundata
    }
}