package com.team1.volunteerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class WebMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_map)

        val webView : WebView = findViewById(R.id.webViewMap)
        val adrs = intent.getStringExtra("adr")
        webView.apply{
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }
        webView.loadUrl("https://m.map.naver.com/search2/search.naver?query=${adrs}&sm=hty&style=v5#/map")
    }
}