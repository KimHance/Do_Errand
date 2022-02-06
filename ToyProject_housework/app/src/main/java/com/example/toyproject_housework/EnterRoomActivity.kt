package com.example.toyproject_housework

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_enterroom.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class EnterRoomActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    var db = FirebaseFirestore.getInstance()

    lateinit var name : String
    lateinit var role : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterroom)

        auth = FirebaseAuth.getInstance()
        val userIDString = auth?.currentUser?.uid.toString()

        roomEnter_back.setOnClickListener {
            finish()
        }

        roomEnter_check.setOnClickListener {
            var code = enterRoom_code.text.toString()
            if(code.isNullOrEmpty()){
                toast("코드가 비었습니다")
            }else{
                val room = db.collection("Room").document(code)
                room.get()
                    .addOnSuccessListener { dc ->
                        if (!dc.exists()){
                            toast("잘못된 코드입니다")
                        }else{
                            db.collection("User")
                                .document(userIDString)
                                .get()
                                .addOnSuccessListener {
                                    role = it["role"].toString()
                                    name = it["name"].toString()
                                    var data = hashMapOf(
                                        name to role
                                    )
                                    room.set(data, SetOptions.merge())
                                    db.collection("User")
                                        .document(userIDString)
                                        .update("room",code)
                                    startActivity<MainActivity>()
                                    finish()
                                }.addOnFailureListener {
                                    Log.d("db","db 받아오기 실패")
                                }

                        }
                    }
                    .addOnFailureListener {
                        toast("실패")
                    }
            }
        }
    }
}