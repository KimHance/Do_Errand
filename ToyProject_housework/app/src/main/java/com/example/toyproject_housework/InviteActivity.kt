package com.example.toyproject_housework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_invite_code.*

class InviteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_code)

        var code = intent.getStringExtra("roomCode")
        invite_code.text = code

        invite_back.setOnClickListener {
            finish()
        }

    }
}