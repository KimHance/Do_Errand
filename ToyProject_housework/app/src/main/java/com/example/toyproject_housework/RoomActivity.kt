package com.example.toyproject_housework

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)


        room_enter.setOnClickListener {
            startActivity<EnterRoomActivity>()
        }

        room_make.setOnClickListener {
        }

    }

    private fun makeRoom(user: FirebaseUser?){
        val userIDString = user?.uid.toString()
        db.collection("Room")

    }
}