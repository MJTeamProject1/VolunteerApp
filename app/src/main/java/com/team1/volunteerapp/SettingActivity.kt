package com.team1.volunteerapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.team1.volunteerapp.Auth.IntroActivity
import com.team1.volunteerapp.utils.FBAuth
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.content_profile.*
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SettingActivity : AppCompatActivity() {
    val OPEN_GALLERY = 1
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var count: Int = 0
    lateinit var uri: Uri
    var useemail : String? = ""

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
        var editprofileimage = findViewById<ImageView>(R.id.editprofileimage)
        useemail = intent.getStringExtra("userEmail")
        email.setText(useemail)
        phonenumber.setText(intent.getStringExtra("userPhone"))
        nickname.setText(intent.getStringExtra("userNickname"))
        goaltime.setText(intent.getStringExtra("userGoal"))

        editprofileimage.setImageBitmap(FBAuth.getUserImage())


        var sidodata: String? = intent.getStringExtra("userSido")
        var gugundata: String? = intent.getStringExtra("userGungu")
        val gugunintent: String? = gugundata

        // spinner ??????
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
            "???????????????" -> {
                spinner1.setSelection(0)
                spinner2.adapter = gugunadapter1
            }
            "???????????????" -> {
                spinner1.setSelection(1)
                spinner2.adapter = gugunadapter2
            }
            "???????????????" -> {
                spinner1.setSelection(2)
                spinner2.adapter = gugunadapter3
            }
            "???????????????" -> {
                spinner1.setSelection(3)
                spinner2.adapter = gugunadapter4
            }
            "???????????????" -> {
                spinner1.setSelection(4)
                spinner2.adapter = gugunadapter5
            }
            "???????????????" -> {
                spinner1.setSelection(5)
                spinner2.adapter = gugunadapter6
            }
            "???????????????" -> {
                spinner1.setSelection(6)
                spinner2.adapter = gugunadapter7
            }
            "?????????????????????" -> {
                spinner1.setSelection(7)
                spinner2.adapter = gugunadapter8
            }
            "?????????" -> {
                spinner1.setSelection(8)
                spinner2.adapter = gugunadapter9
            }
            "?????????" -> {
                spinner1.setSelection(9)
                spinner2.adapter = gugunadapter10
            }
            "????????????" -> {
                spinner1.setSelection(10)
                spinner2.adapter = gugunadapter11
            }
            "????????????" -> {
                spinner1.setSelection(11)
                spinner2.adapter = gugunadapter12
            }
            "????????????" -> {
                spinner1.setSelection(12)
                spinner2.adapter = gugunadapter13
            }
            "????????????" -> {
                spinner1.setSelection(13)
                spinner2.adapter = gugunadapter14
            }
            "????????????" -> {
                spinner1.setSelection(14)
                spinner2.adapter = gugunadapter15
            }
            "????????????" -> {
                spinner1.setSelection(15)
                spinner2.adapter = gugunadapter16
            }
            "?????????????????????" -> {
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
                        sidodata = "???????????????"
                    }
                    1 -> {
                        spinner2.adapter = gugunadapter2
                        sidodata = "???????????????"
                    }
                    2 -> {
                        spinner2.adapter = gugunadapter3
                        sidodata = "???????????????"
                    }
                    3 -> {
                        spinner2.adapter = gugunadapter4
                        sidodata = "???????????????"
                    }
                    4 -> {
                        spinner2.adapter = gugunadapter5
                        sidodata = "???????????????"
                    }
                    5 -> {
                        spinner2.adapter = gugunadapter6
                        sidodata = "???????????????"
                    }
                    6 -> {
                        spinner2.adapter = gugunadapter7
                        sidodata = "???????????????"
                    }
                    7 -> {
                        spinner2.adapter = gugunadapter8
                        sidodata = "?????????????????????"
                    }
                    8 -> {
                        spinner2.adapter = gugunadapter9
                        sidodata = "?????????"
                    }
                    9 -> {
                        spinner2.adapter = gugunadapter10
                        sidodata = "?????????"
                    }
                    10 -> {
                        spinner2.adapter = gugunadapter11
                        sidodata = "????????????"
                    }
                    11 -> {
                        spinner2.adapter = gugunadapter12
                        sidodata = "????????????"
                    }
                    12 -> {
                        spinner2.adapter = gugunadapter13
                        sidodata = "????????????"
                    }
                    13 -> {
                        spinner2.adapter = gugunadapter14
                        sidodata = "????????????"
                    }
                    14 -> {
                        spinner2.adapter = gugunadapter15
                        sidodata = "????????????"
                    }
                    15 -> {
                        spinner2.adapter = gugunadapter16
                        sidodata = "????????????"
                    }
                    16 -> {
                        spinner2.adapter = gugunadapter17
                        sidodata = "?????????????????????"
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }

        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, p2: Int, id: Long) {
                if (count > 1) {
                    if (sidodata == "???????????????") {
                        gugundata = gugun1[p2]
                    } else if (sidodata == "???????????????") {
                        gugundata = gugun2[p2]
                    } else if (sidodata == "???????????????") {
                        gugundata = gugun3[p2]
                    } else if (sidodata == "???????????????") {
                        gugundata = gugun4[p2]
                    } else if (sidodata == "???????????????") {
                        gugundata = gugun5[p2]
                    } else if (sidodata == "???????????????") {
                        gugundata = gugun6[p2]
                    } else if (sidodata == "???????????????") {
                        gugundata = gugun7[p2]
                    } else if (sidodata == "?????????????????????") {
                        gugundata = gugun8[p2]
                    } else if (sidodata == "?????????") {
                        gugundata = gugun9[p2]
                    } else if (sidodata == "?????????") {
                        gugundata = gugun10[p2]
                    } else if (sidodata == "????????????") {
                        gugundata = gugun11[p2]
                    } else if (sidodata == "????????????") {
                        gugundata = gugun12[p2]
                    } else if (sidodata == "????????????") {
                        gugundata = gugun13[p2]
                    } else if (sidodata == "????????????") {
                        gugundata = gugun14[p2]
                    } else if (sidodata == "????????????") {
                        gugundata = gugun15[p2]
                    } else if (sidodata == "????????????") {
                        gugundata = gugun16[p2]
                    } else if (sidodata == "?????????????????????") {
                        gugundata = gugun17[p2]
                    }
                } else {
                    for (i in 0..spinner2.count) {
                        if (gugunintent == spinner2.getItemAtPosition(i)) {
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

        editprofileimage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_GALLERY)
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
                "timedata" to settimedb,
            )


            if (phonenumber_db.isEmpty()) {
                Toast.makeText(this, "????????? ???????????? ???????????????", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else if (nickname_db.isEmpty()) {
                Toast.makeText(this, "???????????? ???????????? ???????????????", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else {
                uploadImage()
                if (isGoToJoin) {
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setMessage("????????? ????????? ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
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
                                }
                            }
                            if (progressDialog.isShowing) {
                                progressDialog.dismiss()
                                Toast.makeText(this, "????????????!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun uploadImage() {

        val storage = FirebaseStorage.getInstance().getReference("images/${useemail}")
        storage.putFile(uri).addOnSuccessListener {
            Toast.makeText(this, "?????????????????????!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {

        }

    }

    //????????? ?????? ????????????
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_GALLERY -> {
                    try {
                        uri = data?.data!!
                        Log.d("Profile", uri.toString())
                        editprofileimage.setImageURI(uri)
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}

