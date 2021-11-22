package com.team1.volunteerapp.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.R
import com.team1.volunteerapp.Loading.SplashActivity

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        // 회원가입 입력
        val email = findViewById<EditText>(R.id.emailEditArea)
        val password = findViewById<EditText>(R.id.passwordEditArea)
        val joinbtn = findViewById<Button>(R.id.joinBtn)
        val password_check = findViewById<EditText>(R.id.password2EditArea)
        val username= findViewById<EditText>(R.id.nameEditArea)
        val phonenumber = findViewById<EditText>(R.id.phonenumberEditArea)
        val nickname = findViewById<EditText>(R.id.nicknameEditArea)
        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val settime = findViewById<EditText>(R.id.volsetTimeArea)

        var sidodata : String = ""
        var gugundata : String = ""

        // spinner 관련
        val sido = resources.getStringArray(R.array.spinner_region)
        val gugun1 = resources.getStringArray(R.array.spinner_region_seoul)
        val gugun2 = resources.getStringArray(R.array.spinner_region_busan)
        val gugun3 = resources.getStringArray(R.array.spinner_region_incheon)
        val gugun4 = resources.getStringArray(R.array.spinner_region_daegu)
        val gugun5 = resources.getStringArray(R.array.spinner_region_gwangju)
        val gugun6 = resources.getStringArray(R.array.spinner_region_daejeon)
        val gugun7 = resources.getStringArray(R.array.spinner_region_ulsan)
        val gugun8 = resources.getStringArray(R.array.spinner_region_sejong)
        val gugun9 = resources.getStringArray(R.array.spinner_region_gyeonggi)
        val gugun10 = resources.getStringArray(R.array.spinner_region_gangwon)
        val gugun11 = resources.getStringArray(R.array.spinner_region_chung_buk)
        val gugun12 = resources.getStringArray(R.array.spinner_region_chung_nam)
        val gugun13 = resources.getStringArray(R.array.spinner_region_gyeong_buk)
        val gugun14 = resources.getStringArray(R.array.spinner_region_gyeong_nam)
        val gugun15 = resources.getStringArray(R.array.spinner_region_jeon_buk)
        val gugun16 = resources.getStringArray(R.array.spinner_region_jeon_nam)
        val gugun17 = resources.getStringArray(R.array.spinner_region_jeju)

        val sidoadapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, sido)
        val gugunadapter1 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun1)
        val gugunadapter2 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun2)
        val gugunadapter3 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun3)
        val gugunadapter4 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun4)
        val gugunadapter5 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun5)
        val gugunadapter6 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun6)
        val gugunadapter7 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun7)
        val gugunadapter8 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun8)
        val gugunadapter9 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun9)
        val gugunadapter10 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun10)
        val gugunadapter11 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun11)
        val gugunadapter12 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun12)
        val gugunadapter13 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun13)
        val gugunadapter14 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun14)
        val gugunadapter15 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun15)
        val gugunadapter16 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun16)
        val gugunadapter17 = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, gugun17)

        spinner1.adapter = sidoadapter
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                        spinner2.adapter = gugunadapter1
                        println("==========="+sido[position])
                        sidodata = "서울특별시"
                    }
                    1 -> { spinner2.adapter = gugunadapter2
                        sidodata = "부산광역시"}
                    2 -> {spinner2.adapter = gugunadapter3
                        sidodata = "대구광역시"}
                    3 -> {spinner2.adapter = gugunadapter4
                        sidodata = "인천광역시"}
                    4 -> {spinner2.adapter = gugunadapter5
                        sidodata = "광주광역시"}
                    5 -> {spinner2.adapter = gugunadapter6
                        sidodata = "대전광역시"}
                    6 -> {spinner2.adapter = gugunadapter7
                        sidodata = "울산광역시"}
                    7 -> {spinner2.adapter = gugunadapter8
                        sidodata = "세종특별자치시"}
                    8 -> {spinner2.adapter = gugunadapter9
                        sidodata = "경기도"}
                    9 ->{spinner2.adapter = gugunadapter10
                        sidodata = "강원도"}
                    10 -> {spinner2.adapter = gugunadapter11
                        sidodata = "충청북도"}
                    11 -> {spinner2.adapter = gugunadapter12
                        sidodata = "충청남도"}
                    12 -> {spinner2.adapter = gugunadapter13
                        sidodata = "전라북도"}
                    13 -> {spinner2.adapter = gugunadapter14
                        sidodata = "전라남도"}
                    14 -> {spinner2.adapter = gugunadapter15
                        sidodata = "경상북도"}
                    15 -> {spinner2.adapter = gugunadapter16
                        sidodata = "경상남도"}
                    16 -> {spinner2.adapter = gugunadapter17
                        sidodata = "제주특별자치도"}

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }

        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                if(sidodata == "서울특별시"){
                    gugundata = gugun1[position]
                }
                else if(sidodata == "부산광역시"){
                    gugundata = gugun2[position]
                }
                else if(sidodata == "대구광역시"){
                    gugundata = gugun3[position]
                }
                else if(sidodata == "인천광역시"){
                    gugundata = gugun4[position]
                }
                else if(sidodata == "광주광역시"){
                    gugundata = gugun5[position]
                }
                else if(sidodata == "대전광역시"){
                    gugundata = gugun6[position]
                }
                else if(sidodata == "울산광역시"){
                    gugundata = gugun7[position]
                }
                else if(sidodata == "세종특별자치시"){
                    gugundata = gugun8[position]
                }
                else if(sidodata == "경기도"){
                    gugundata = gugun9[position]
                }
                else if(sidodata == "강원도"){
                    gugundata = gugun10[position]
                }
                else if(sidodata == "충청북도"){
                    gugundata = gugun11[position]
                }
                else if(sidodata == "충청남도"){
                    gugundata = gugun12[position]
                }
                else if(sidodata == "전라북도"){
                    gugundata = gugun13[position]
                }
                else if(sidodata == "전라남도"){
                    gugundata = gugun14[position]
                }
                else if(sidodata == "경상북도"){
                    gugundata = gugun15[position]
                }
                else if(sidodata == "경상남도"){
                    gugundata = gugun16[position]
                }
                else if(sidodata == "제주특별자치도"){
                    gugundata = gugun17[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        joinbtn.setOnClickListener {
            var isGoToJoin = true

            val inputemail = email.text.toString()
            val inputpassword = password.text.toString()
            val password_check_db = password_check.text.toString()
            val username_db= username.text.toString()
            val phonenumber_db = phonenumber.text.toString()
            val nickname_db = nickname.text.toString()
            val sidodatadb = sidodata
            val gugundatadb = gugundata
            val settimedb = settime.text.toString()

            val userauth : HashMap<String, String> = hashMapOf(
                "email" to inputemail,
                "passworld" to inputpassword,
                "username" to username_db,
                "phonenumber" to phonenumber_db,
                "nickname" to nickname_db,
                "sidodata" to sidodatadb,
                "gugundata" to gugundatadb,
                "timedata" to settimedb,
                "uid" to auth.uid.toString()
            )

            if(inputemail.isEmpty()){
                Toast.makeText(this,"이메일을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(inputpassword.isEmpty()){
                Toast.makeText(this,"비밀번호를 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(password_check_db.isEmpty()){
                Toast.makeText(this,"비밀번호 확인을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(username_db.isEmpty()){
                Toast.makeText(this,"이름을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(phonenumber_db.isEmpty()){
                Toast.makeText(this,"번호를 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(!phonenumber_db.isDigitsOnly()){
                Toast.makeText(this,"번호에 숫자만 입력해주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(nickname_db.isEmpty()){
                Toast.makeText(this,"닉네임을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(inputpassword.length < 6){
                Toast.makeText(this,"비밀번호를 6자리 이상 눌러주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(settimedb.isEmpty()){
                Toast.makeText(this,"목표 봉사 시간을 입력하지 않았습니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else if(!settimedb.isDigitsOnly()){
                Toast.makeText(this,"목표 봉사 시간에 숫자만 입력해주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            else {
                if (isGoToJoin) {
                    auth.createUserWithEmailAndPassword(inputemail, inputpassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                userauth["uid"] = auth.uid.toString()
                                // Firebase Firestore db에 넣기
                                db.collection("UserData")
                                    .add(userauth)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("TAGMESSAGE", "DocumentSnapshot added with ID: ${documentReference.id}")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("TAGMESSAGE", "Error adding document", e)
                                    }
                                val Intent = Intent(this, SplashActivity::class.java)
                                // 기존 엑티비티를 다 날림
                                finishAffinity()
                                startActivity(Intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                            }
                        }
                }
            }
        }
    }
}
