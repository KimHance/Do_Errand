package com.example.toyproject_housework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btn_start.setOnClickListener{
            startActivity<LoginActivity>()
            finish()
        }
    }
}