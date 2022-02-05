package com.example.toyproject_housework

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        login_login.setOnClickListener {
            val email = login_id.text.toString()
            val password = login_passwd.text.toString()
            signIn(email, password)
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun signIn(email : String, password : String){
        if(email.isNotEmpty() && password.isNotEmpty()){
            if(password.length<6){
                toast("비밀번호는 6자리 이상입니다.")
            }else{
                auth?.signInWithEmailAndPassword(email,password)
                    ?.addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                            toast("로그인 성공")
                            moveMainActivity(auth?.currentUser)
                        }else{
                            toast("로그인 실패")
                        }
                    }
            }
        }else{
            toast("빈칸이 존재합니다.")
        }
    }

    fun moveMainActivity(user: FirebaseUser?){
        if(user != null){
            startActivity<MainActivity>()
            login_passwd.setText("")
        }
    }
}