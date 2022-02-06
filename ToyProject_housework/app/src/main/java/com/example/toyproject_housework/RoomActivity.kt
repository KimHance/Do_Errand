package com.example.toyproject_housework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_room.*
import org.jetbrains.anko.startActivity


class RoomActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    lateinit var role : String
    lateinit var name : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)


        auth = FirebaseAuth.getInstance()
        val userIDString = auth?.currentUser?.uid.toString()

        db.collection("User")
            .document(userIDString)
            .get()
            .addOnSuccessListener {
                role = it["role"].toString()
                name = it["name"].toString()
                Log.d("역할과이름","역할 = $role 이름 = $name")
            }.addOnFailureListener {
                Log.d("db","db 받아오기 실패")
            }

        val range = (0..9)
        var code = ""
        for(i in 1..8){
            code += range.random().toString()
        }
        Log.d("방 코드",code)

        room_enter.setOnClickListener {
            startActivity<EnterRoomActivity>()
        }

        room_make.setOnClickListener {
            makeRoom(auth.currentUser, code, role, name)
        }

    }

    private fun makeRoom(user: FirebaseUser? , code: String, role : String, name : String){
        val userIDString = user?.uid.toString()
        val data = hashMapOf(
            name to role
        )
        val roomData = mutableMapOf<String,Any>()
        roomData["room"] = code

        db.collection("Room")
            .document(code)
            .set(data)
            .addOnSuccessListener {
                Log.d("db","고유방 만들기 성공")
                db.collection("User")
                    .document(userIDString)
                    .update(roomData)
                    .addOnSuccessListener {
                        // 메인으로 데이터 잘 넘어가는지 임시 확인
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("code",code)
                        intent.putExtra("name",name)
                        intent.putExtra("role",role)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Log.d("db","고유방 만들기 실패")
                    }
            }
            .addOnFailureListener {
                Log.d("db","고유방 만들기 실패")
            }

    }
}