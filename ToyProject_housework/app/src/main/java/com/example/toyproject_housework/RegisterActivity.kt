package com.example.toyproject_housework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast


class RegisterActivity : AppCompatActivity(){

    private lateinit var auth : FirebaseAuth
    lateinit var family : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val item : Array<String> = resources.getStringArray(R.array.family_array)
        val adapter = ArrayAdapter(this,R.layout.spinner_item,item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position !=0 ){
                    family = item[position]
                    Log.d("TAG",family)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }




        // 뒤로가기
        register_cancel.setOnClickListener {
            finish()
        }
    }

    private fun createUser(email: String, passwd: String){
        auth.createUserWithEmailAndPassword(email,passwd)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    toast("회원가입 성공")
                    val user = auth.currentUser
                }else{
                    toast("회원가입 실패")
                }
            }
            .addOnFailureListener{
                toast("회원가입 실패")
            }
    }
}