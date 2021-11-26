package com.team1.volunteerapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Paint
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Favorite.FavoriteModel
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.applycalendar.*
import kotlinx.android.synthetic.main.applycalendar.view.*
import kotlinx.android.synthetic.main.passwordcheck.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

class AboutViewActivity : AppCompatActivity() {

    private var num1 : String = ""
    private var tel1 : String = ""
    private var title1 : String = ""
    private var starttime1 : String = ""
    private var endtime1 : String = ""

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("ClickableViewAccessibility", "NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutview)
        setSupportActionBar(bottomAppBar)
        auth = Firebase.auth

        val num = intent.getStringExtra("num")
        val volunteerTitle: TextView = findViewById(R.id.volunteerTitle)
        val volunteerDetail: TextView = findViewById(R.id.voluteerDetail)
        val applyButton: AppCompatButton = findViewById(R.id.applyButton)
        val voluteerImage = findViewById<ImageView>(R.id.voluteerImage)

        // 기본 정보
        val textprogrmSttusSe = findViewById<TextView>(R.id.textprogrmSttusSe)
        val textmnnstNm = findViewById<TextView>(R.id.textmnnstNm)
        val textnoticeBgnde = findViewById<TextView>(R.id.textnoticeBgnde)
        val textnoticeEndde = findViewById<TextView>(R.id.textnoticeEndde)
        val textrcritNmpr = findViewById<TextView>(R.id.textrcritNmpr)
        val textprogrmBgnde = findViewById<TextView>(R.id.textprogrmBgnde)
        val textprogrmEndde = findViewById<TextView>(R.id.textprogrmEndde)
        val textactBeginTm = findViewById<TextView>(R.id.textactBeginTm)
        val textactEndTm = findViewById<TextView>(R.id.textactEndTm)
        val textactPlace = findViewById<TextView>(R.id.textactPlace)
        val textsrvcClCode = findViewById<TextView>(R.id.textsrvcClCode)
        val textnanmmbyNm = findViewById<TextView>(R.id.textnanmmbyNm)

        // 상세 정보
        val textnanmmbyNmAdmn = findViewById<TextView>(R.id.textnanmmbyNmAdmn)
        val texttelno = findViewById<TextView>(R.id.texttelno)
        val textpostAdres = findViewById<TextView>(R.id.textpostAdres)

        var vol_detail: String? = null
        var vol_starttime: String? = null
        var vol_endtime: String? = null
        var vol_title: String? = null

        // 기본 정보
        var vol_progrmSttusSe : String? = null
        var vol_mnnstNm : String? = null
        var vol_noticeBgnde  : String? = null
        var vol_noticeEndde : String? = null
        var vol_rcritNmpr : String? = null
        var vol_progrmBgnde  : String? = null
        var vol_progrmEndde : String? = null
        var vol_actBeginTm  : String? = null
        var vol_actEndTm : String? = null
        var vol_actPlace : String? = null
        var vol_srvcClCode : String? = null
        var vol_nanmmbyNm : String? = null

        // 연락처
        var vol_nanmmbyNmAdmn : String? = null
        var vol_telnum: String? = null
        var vol_adress : String? = null
        var vol_email: String? = null

        //봉사 신청시 날짜
        var apply_year : String? = null
        var apply_month : String? = null
        var apply_day : String? = null
        var apply_date : String? = null

        volunteerDetail.setMovementMethod(ScrollingMovementMethod())
        volunteerTitle.text = num

        // 하단 바
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

        // Coroutine을 이용한 API 불러오기
        val job = CoroutineScope(Dispatchers.IO).launch {
            val key: String =
                "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
            val url: String =
                //지역 정보는 현재 고정 추후 수정할 예정
                "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrPartcptnItem?progrmRegistNo=${num}&serviceKey=$key"
            //progrmRegistNo=2780545
            //2780545 프로그램 번호 수정

            val xml: Document =
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)

            xml.documentElement.normalize()
            println("Root element :" + xml.documentElement.nodeName)

            val list: NodeList = xml.getElementsByTagName("item")

            for (i in 0 until list.length) {
                val n: Node = list.item(i)
                if (n.nodeType == Node.ELEMENT_NODE) {
                    val elem = n as Element
                    val map = mutableMapOf<String, String>()

                    for (j in 0 until elem.attributes.length) {

                        map.putIfAbsent(
                            elem.attributes.item(j).nodeName,
                            elem.attributes.item(j).nodeValue
                        )
                    }

                    vol_detail = elem.getElementsByTagName("progrmCn").item(0).textContent
                    vol_starttime = elem.getElementsByTagName("actBeginTm").item(0).textContent
                    vol_endtime = elem.getElementsByTagName("actEndTm").item(0).textContent
                    vol_title = elem.getElementsByTagName("progrmSj").item(0).textContent
                    vol_email = elem.getElementsByTagName("email").item(0).textContent

                    vol_progrmSttusSe = elem.getElementsByTagName("progrmSttusSe").item(0).textContent
                    vol_mnnstNm = elem.getElementsByTagName("mnnstNm").item(0).textContent
                    vol_noticeBgnde = elem.getElementsByTagName("noticeBgnde").item(0).textContent
                    vol_noticeEndde = elem.getElementsByTagName("noticeEndde").item(0).textContent
                    vol_rcritNmpr = elem.getElementsByTagName("rcritNmpr").item(0).textContent
                    vol_progrmBgnde = elem.getElementsByTagName("progrmBgnde").item(0).textContent
                    vol_progrmEndde = elem.getElementsByTagName("progrmEndde").item(0).textContent
                    vol_actBeginTm = elem.getElementsByTagName("actBeginTm").item(0).textContent
                    vol_actEndTm = elem.getElementsByTagName("actEndTm").item(0).textContent
                    vol_actPlace = elem.getElementsByTagName("actPlace").item(0).textContent
                    vol_srvcClCode = elem.getElementsByTagName("srvcClCode").item(0).textContent
                    vol_nanmmbyNm = elem.getElementsByTagName("nanmmbyNm").item(0).textContent

                    vol_nanmmbyNmAdmn = elem.getElementsByTagName("nanmmbyNmAdmn").item(0).textContent
                    vol_telnum = elem.getElementsByTagName("telno").item(0).textContent
                    vol_adress = elem.getElementsByTagName("postAdres").item(0).textContent

                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }
        volunteerTitle.setText(vol_title)

        if(vol_progrmSttusSe == "2"){
            textprogrmSttusSe.text = "모집중"
        } else{
            textprogrmSttusSe.text = "모집 완료"
        }
        textmnnstNm.text = vol_mnnstNm
        textnoticeBgnde.text =
            vol_noticeBgnde.toString().substring(0,4) + "." + vol_noticeBgnde.toString().substring(4,6) + "." + vol_noticeBgnde.toString().substring(6,8)
        textnoticeEndde.text =
            vol_noticeEndde.toString().substring(0,4) + "." + vol_noticeEndde.toString().substring(4,6) + "." + vol_noticeEndde.toString().substring(6,8)
        textrcritNmpr.text = vol_rcritNmpr + "명"
        textprogrmBgnde.text =
            vol_progrmBgnde.toString().substring(0,4) + "." + vol_progrmBgnde.toString().substring(4,6) + "." + vol_progrmBgnde.toString().substring(6,8)
        textprogrmEndde.text =
            vol_progrmEndde.toString().substring(0,4) + "." + vol_progrmEndde.toString().substring(4,6) + "." + vol_progrmEndde.toString().substring(6,8)
        textactBeginTm.text = vol_actBeginTm + "시"
        textactEndTm.text = vol_actEndTm + "시"
        textactPlace.text = vol_actPlace
        textsrvcClCode.text = vol_srvcClCode
        textnanmmbyNm.text = vol_nanmmbyNm

        volunteerDetail.text = vol_detail

        textnanmmbyNmAdmn.text = vol_nanmmbyNmAdmn
        texttelno.text = vol_telnum
        textpostAdres.text = vol_adress

        // 이미지 설정
        if(vol_srvcClCode?.contains("문화") == true) {
            voluteerImage.setImageResource(R.drawable.world_book_day)
        }
        else if(vol_srvcClCode?.contains("주거") == true){
            voluteerImage.setImageResource(R.drawable.home)
        }
        else if(vol_srvcClCode?.contains("상담") == true){
            voluteerImage.setImageResource(R.drawable.consulting)
        }
        else if(vol_srvcClCode?.contains("교육") == true){
            voluteerImage.setImageResource(R.drawable.book)
        }
        else if(vol_srvcClCode?.contains("의료") == true){
            voluteerImage.setImageResource(R.drawable.first_aid_kit)
        }
        else if(vol_srvcClCode?.contains("농어촌") == true){
            voluteerImage.setImageResource(R.drawable.hill)
        }
        else if(vol_srvcClCode?.contains("환경") == true){
            voluteerImage.setImageResource(R.drawable.environmental_protection)
        }
        else if(vol_srvcClCode?.contains("행정") == true){
            voluteerImage.setImageResource(R.drawable.briefcase)
        }
        else if(vol_srvcClCode?.contains("안전") == true){
            voluteerImage.setImageResource(R.drawable.prevention)
        }
        else if(vol_srvcClCode?.contains("공익") == true){
            voluteerImage.setImageResource(R.drawable.healthcare)
        }
        else if(vol_srvcClCode?.contains("재난") == true){
            voluteerImage.setImageResource(R.drawable.disaster)
        }

        textpostAdres.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        textpostAdres.setOnClickListener {
            val intent = Intent(this, WebMapActivity::class.java)
            intent.putExtra("adr", textpostAdres.text.toString())
            startActivity(intent)
        }

        //홈으로 가기
        val homeButton = findViewById<FloatingActionButton>(R.id.fab)
        homeButton.setOnClickListener { // 홈으로 돌아가기
            fab.hide(AnimationB.addVisibilityChanged)
            Handler().postDelayed({
                finish()
            }, 300)
        }

        applyButton.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_DATE
            val formatted = current.format(formatter)
            var datearr = formatted.split("-")
            if((vol_progrmBgnde.toString().toInt() <= "${datearr[0]}${datearr[1]}${datearr[2]}".toInt()) &&
                "${datearr[0]}${datearr[1]}${datearr[2]}".toInt() <= vol_progrmEndde.toString().toInt()){
                apply_year = datearr[0]
                apply_month = datearr[1]
                apply_day = datearr[2]
            }
            else{
                apply_year = vol_progrmBgnde.toString().substring(0,4)
                apply_month = vol_progrmBgnde.toString().substring(4,6)
                apply_day = vol_progrmBgnde.toString().substring(6,8)
            }
            val mdialogview = LayoutInflater.from(this).inflate(R.layout.applycalendar, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mdialogview)
                .setTitle("신청 일자 선택")
            val  mAlertDialog = mBuilder.show()

            val mincalendar = Calendar.getInstance()
            val maxcalendar = Calendar.getInstance()

            mincalendar.set(
                vol_progrmBgnde.toString().substring(0,4).toInt(),
                vol_progrmBgnde.toString().substring(4,6).toInt() - 1,
                vol_progrmBgnde.toString().substring(6,8).toInt()
            )

            maxcalendar.set(
                vol_progrmEndde.toString().substring(0,4).toInt(),
                vol_progrmEndde.toString().substring(4,6).toInt() - 1,
                vol_progrmEndde.toString().substring(6,8).toInt()
            )

            mAlertDialog.calendarApply.minDate = mincalendar.timeInMillis
            mAlertDialog.calendarApply.maxDate = maxcalendar.timeInMillis

            mAlertDialog.calendarApply.setOnDateChangeListener { view, year, month, dayOfMonth ->
                apply_year = year.toString()
                if(month + 1 < 10){
                    apply_month = "0${month + 1}"
                }else{
                    apply_month = (month + 1).toString()
                }
                if(dayOfMonth < 10){
                    apply_day = "0${dayOfMonth}"
                }else{
                    apply_day = dayOfMonth.toString()
                }
            }

            mAlertDialog.dialogApplyBtn.setOnClickListener {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("신청 중...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                db.collection("UserData")
                .get()
                .addOnSuccessListener { result ->
                    for (doc in result) {
                        if (doc["uid"] == auth.uid.toString()) {
                            val num =
                                vol_endtime.toString().toInt() - vol_starttime.toString().toInt()
                            var time = "${vol_starttime}${vol_endtime}"
                            if(vol_starttime.toString().toInt() < 10 && vol_endtime.toString().toInt() < 0){
                                time = "0${vol_starttime}0${vol_endtime}"
                            }
                            else if(vol_starttime.toString().toInt() < 10){
                                time ="0${vol_starttime}${vol_endtime}"
                            }
                            else if(vol_endtime.toString().toInt() < 10){
                                time ="${vol_starttime}0${vol_endtime}"
                            }
                            else{
                                time ="${vol_starttime}${vol_endtime}"
                            }

                            apply_date = "${apply_year}${apply_month}${apply_day}"

                            val userauth: HashMap<String, Any> = hashMapOf(
                                "vol_time" to num,
                                "vol_title" to vol_title.toString(),
                                "vol_applyDate" to apply_date.toString(),
                                "whentime" to time
                            )

                            if (doc["vol_time"] != null) {
                                userauth["vol_time"] = doc["vol_time"].toString().toInt() + num
                            }
                            if (doc["vol_title"] != null) {
                                userauth["vol_title"] = vol_title.toString() + "@${doc["vol_title"].toString()}"
                            }
                            if (doc["vol_applyDate"] != null) {
                                userauth["vol_applyDate"] = apply_date.toString() + "@${doc["vol_applyDate"].toString()}"
                            }
                            if (doc["whentime"] != null) {
                                userauth["whentime"] = time + "@${doc["whentime"].toString()}"
                            }
                            db.collection("UserData").document(doc.id.toString())
                                .update(userauth)
                            Toast.makeText(this, "신청완료!", Toast.LENGTH_SHORT).show()
                            if(progressDialog.isShowing){
                                progressDialog.dismiss()
                            }
                            //TODO 신청할 시 같은 날짜에 봉사가 있는지 판별
                        }
                    }
                }
            }

            mdialogview.dialogCancleBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }

        }

        tel1 = vol_telnum.toString()
        if (num != null) {
            num1 = num
        }
        title1 = vol_title.toString()
        starttime1 = vol_starttime.toString()
        endtime1 = vol_endtime.toString()
    }


    // 화면 아래 메뉴 바
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navi_menu_aboutview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.app_bar_apply -> {
                db.collection("UserData")
                    .get()
                    .addOnSuccessListener { result ->
                        for (doc in result) {
                            if (doc["uid"] == auth.uid.toString()) {
                                val num =
                                    endtime1.toString().toInt() - starttime1.toString().toInt()
                                val userauth: HashMap<String, Any> = hashMapOf(
                                    "vol_time" to num,
                                    "vol_title" to title1.toString()
                                )

                                if (doc["vol_time"] != null) {
                                    userauth["vol_time"] = doc["vol_time"].toString().toInt() + num
                                }
                                if (doc["vol_title"] != null) {
                                    userauth["vol_title"] = title1.toString() + "@${doc["vol_title"].toString()}"
                                }
                                db.collection("UserData").document(doc.id.toString())
                                    .update(userauth)
                                Toast.makeText(this, "신청완료!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


            }
            R.id.app_bar_favadd -> {
                println("-===================" + num1)
                if (num1 != null) {
                    FBRef.favoriteRef
                        .child(FBAuth.getUid())
                        .child(num1)
                        .setValue(FavoriteModel(true))
                    Toast.makeText(this, "즐겨찾기 추가완료!", Toast.LENGTH_SHORT).show()
                }


            }
            R.id.app_bar_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${tel1}")
                startActivity(intent)
            }
        }
        return true
    }
}