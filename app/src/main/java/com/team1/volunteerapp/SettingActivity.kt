package com.team1.volunteerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Auth.IntroActivity

class SettingActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    var count : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.emailRectifyArea)
        val rectifybtn = findViewById<Button>(R.id.rectifyBtn)
        val phonenumber = findViewById<EditText>(R.id.phonenumberRectifyArea)
        val nickname = findViewById<EditText>(R.id.nicknameRectifyArea)
        val spinner1 = findViewById<Spinner>(R.id.spinner1_rectify)
        val spinner2 = findViewById<Spinner>(R.id.spinner2_rectify)
        val goaltime = findViewById<EditText>(R.id.volRectifyTimeArea)

        email.setText(intent.getStringExtra("userEmail"))
        phonenumber.setText(intent.getStringExtra("userPhone"))
        nickname.setText(intent.getStringExtra("userNickname"))
        goaltime.setText(intent.getStringExtra("userGoal"))


        var sidodata: String? = intent.getStringExtra("userSido")
        var gugundata: String? = intent.getStringExtra("userGungu")
        var gugunintent : String? = gugundata

        // spinner 관련
        var sido = resources.getStringArray(R.array.spinner_region)
        var gugun1 = resources.getStringArray(R.array.spinner_region_seoul)
        var gugun2 = resources.getStringArray(R.array.spinner_region_busan)
        var gugun3 = resources.getStringArray(R.array.spinner_region_incheon)
        var gugun4 = resources.getStringArray(R.array.spinner_region_daegu)
        var gugun5 = resources.getStringArray(R.array.spinner_region_gwangju)
        var gugun6 = resources.getStringArray(R.array.spinner_region_daejeon)
        var gugun7 = resources.getStringArray(R.array.spinner_region_ulsan)
        var gugun8 = resources.getStringArray(R.array.spinner_region_sejong)
        var gugun9 = resources.getStringArray(R.array.spinner_region_gyeonggi)
        var gugun10 = resources.getStringArray(R.array.spinner_region_gangwon)
        var gugun11 = resources.getStringArray(R.array.spinner_region_chung_buk)
        var gugun12 = resources.getStringArray(R.array.spinner_region_chung_nam)
        var gugun13 = resources.getStringArray(R.array.spinner_region_gyeong_buk)
        var gugun14 = resources.getStringArray(R.array.spinner_region_gyeong_nam)
        var gugun15 = resources.getStringArray(R.array.spinner_region_jeon_buk)
        var gugun16 = resources.getStringArray(R.array.spinner_region_jeon_nam)
        var gugun17 = resources.getStringArray(R.array.spinner_region_jeju)


        val sidoadapter =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, sido)
        val gugunadapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun1)
        val gugunadapter2 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun2)
        val gugunadapter3 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun3)
        val gugunadapter4 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun4)
        val gugunadapter5 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun5)
        val gugunadapter6 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun6)
        val gugunadapter7 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun7)
        val gugunadapter8 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun8)
        val gugunadapter9 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun9)
        val gugunadapter10 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun10)
        val gugunadapter11 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun11)
        val gugunadapter12 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun12)
        val gugunadapter13 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun13)
        val gugunadapter14 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun14)
        val gugunadapter15 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun15)
        val gugunadapter16 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun16)
        val gugunadapter17 =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, gugun17)

        spinner1.adapter = sidoadapter
        when (sidodata) {
            "서울특별시" -> {
                spinner1.setSelection(0)
                spinner2.adapter = gugunadapter1
            }
            "부산광역시" -> {
                spinner1.setSelection(1)
                spinner2.adapter = gugunadapter2
            }
            "대구광역시" -> {
                spinner1.setSelection(2)
                spinner2.adapter = gugunadapter3
            }
            "인천광역시" -> {
                spinner1.setSelection(3)
                spinner2.adapter = gugunadapter4
            }
            "광주광역시" -> {
                spinner1.setSelection(4)
                spinner2.adapter = gugunadapter5
            }
            "대전광역시" -> {
                spinner1.setSelection(5)
                spinner2.adapter = gugunadapter6
            }
            "울산광역시" -> {
                spinner1.setSelection(6)
                spinner2.adapter = gugunadapter7
            }
            "세종특별자치시" -> {
                spinner1.setSelection(7)
                spinner2.adapter = gugunadapter8
            }
            "경기도" -> {
                spinner1.setSelection(8)
                spinner2.adapter = gugunadapter9
            }
            "강원도" -> {
                spinner1.setSelection(9)
                spinner2.adapter = gugunadapter10
            }
            "충청북도" -> {
                spinner1.setSelection(10)
                spinner2.adapter = gugunadapter11
            }
            "충청남도" -> {
                spinner1.setSelection(11)
                spinner2.adapter = gugunadapter12
            }
            "전라북도" -> {
                spinner1.setSelection(12)
                spinner2.adapter = gugunadapter13
            }
            "전라남도" -> {
                spinner1.setSelection(13)
                spinner2.adapter = gugunadapter14
            }
            "경상북도" -> {
                spinner1.setSelection(14)
                spinner2.adapter = gugunadapter15
            }
            "경상남도" -> {
                spinner1.setSelection(15)
                spinner2.adapter = gugunadapter16
            }
            "제주특별자치도" -> {
                spinner1.setSelection(16)
                spinner2.adapter = gugunadapter17
            }
        }
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                        spinner2.adapter = gugunadapter1
                        println("===========" + sido[position])
                        sidodata = "서울특별시"
                    }
                    1 -> {
                        spinner2.adapter = gugunadapter2
                        sidodata = "부산광역시"
                    }
                    2 -> {
                        spinner2.adapter = gugunadapter3
                        sidodata = "대구광역시"
                    }
                    3 -> {
                        spinner2.adapter = gugunadapter4
                        sidodata = "인천광역시"
                    }
                    4 -> {
                        spinner2.adapter = gugunadapter5
                        sidodata = "광주광역시"
                    }
                    5 -> {
                        spinner2.adapter = gugunadapter6
                        sidodata = "대전광역시"
                    }
                    6 -> {
                        spinner2.adapter = gugunadapter7
                        sidodata = "울산광역시"
                    }
                    7 -> {
                        spinner2.adapter = gugunadapter8
                        sidodata = "세종특별자치시"
                    }
                    8 -> {
                        spinner2.adapter = gugunadapter9
                        sidodata = "경기도"
                    }
                    9 -> {
                        spinner2.adapter = gugunadapter10
                        sidodata = "강원도"
                    }
                    10 -> {
                        spinner2.adapter = gugunadapter11
                        sidodata = "충청북도"
                    }
                    11 -> {
                        spinner2.adapter = gugunadapter12
                        sidodata = "충청남도"
                    }
                    12 -> {
                        spinner2.adapter = gugunadapter13
                        sidodata = "전라북도"
                    }
                    13 -> {
                        spinner2.adapter = gugunadapter14
                        sidodata = "전라남도"
                    }
                    14 -> {
                        spinner2.adapter = gugunadapter15
                        sidodata = "경상북도"
                    }
                    15 -> {
                        spinner2.adapter = gugunadapter16
                        sidodata = "경상남도"
                    }
                    16 -> {
                        spinner2.adapter = gugunadapter17
                        sidodata = "제주특별자치도"
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }

        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, p2: Int, id: Long) {
                if(count > 1) {
                    if (sidodata == "서울특별시") {
                        gugundata = gugun1[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "부산광역시") {
                        gugundata = gugun2[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "대구광역시") {
                        gugundata = gugun3[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "인천광역시") {
                        gugundata = gugun4[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "광주광역시") {
                        gugundata = gugun5[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "대전광역시") {
                        gugundata = gugun6[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "울산광역시") {
                        gugundata = gugun7[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "세종특별자치시") {
                        gugundata = gugun8[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "경기도") {
                        gugundata = gugun9[p2]
                        println("============" + gugundata)

                    } else if (sidodata == "강원도") {
                        gugundata = gugun10[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "충청북도") {
                        gugundata = gugun11[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "충청남도") {
                        gugundata = gugun12[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "전라북도") {
                        gugundata = gugun13[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "전라남도") {
                        gugundata = gugun14[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "경상북도") {
                        gugundata = gugun15[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "경상남도") {
                        gugundata = gugun16[p2]
                        println("============" + gugundata)
                    } else if (sidodata == "제주특별자치도") {
                        gugundata = gugun17[p2]
                        println("============" + gugundata)
                    }
                }
                else{
                    for(i in 0..spinner2.count){
                        if(gugunintent == spinner2.getItemAtPosition(i)){
                            spinner2.setSelection(i)
                            break
                        }
                    }
                }

                count += 1
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")

            }
        }


        rectifybtn.setOnClickListener {
            var isGoToJoin = true
            val phonenumber_db = phonenumber.text.toString()
            val nickname_db = nickname.text.toString()
            val sidodatadb = sidodata
            val gugundatadb = gugundata
            val settimedb = goaltime.text.toString()

            val userauth: HashMap<String, String?> = hashMapOf(
                "phonenumber" to phonenumber_db,
                "nickname" to nickname_db,
                "sidodata" to sidodatadb,
                "gugundata" to gugundatadb,
                "timedata" to settimedb
            )


            if (phonenumber_db.isEmpty()) {
                Toast.makeText(this, "번호를 입력하지 않았습니다", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else if (nickname_db.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력하지 않았습니다", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else {
                if (isGoToJoin) {
                    //Toast.makeText(this,email.text.toString(),Toast.LENGTH_SHORT).show()
                    db.collection("UserData")
                        .get()
                        .addOnSuccessListener { result ->
                            for (doc in result) {
                                if (doc["uid"] == auth.uid.toString()) {
                                    db.collection("UserData").document(doc.id.toString())
                                        .update(userauth as Map<String, Any>)
                                    Firebase.auth.signOut()
                                    val intent = Intent(this, IntroActivity::class.java)
                                    finishAffinity()
                                    startActivity(intent)
                                    finish()
                                    Toast.makeText(this, "변경완료!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
            }
        }
    }
}

