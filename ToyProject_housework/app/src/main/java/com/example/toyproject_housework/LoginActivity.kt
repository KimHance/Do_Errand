package com.example.toyproject_housework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_register.setOnClickListener {
            startActivity<RegisterActivity>()
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