package com.example.toyproject_housework

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import android.widget.TextView




class RegisterActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {

    private lateinit var family : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ArrayAdapter.createFromResource(
            this,
            R.array.family_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_family.adapter=adapter
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        (p0 as TextView).setTextColor(Color.WHITE)
        if(spinner_family.getItemAtPosition(p2).equals("본인 관계")){
            family = "${spinner_family.getItemAtPosition(p2)}"
            Log.d("TAG",family)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}