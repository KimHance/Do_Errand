package com.example.toyproject_housework

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()



        login_register.setOnClickListener {
            startActivity<RegisterActivity>()
            overridePendingTransition(R.anim.slide_up,R.anim.slide_up_exit)
        }
        login_login.setOnClickListener {
            val email = login_id.text.toString()
            val password = login_passwd.text.toString()
            login(email, password)
        }

    }

    // 로그인
    private fun login(email : String, password : String){
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

    // 방 배정x -> 방 생성&참여 화면
    // 방 배정o -> 메인 화면
    private fun moveMainActivity(user: FirebaseUser?){
        val userIDString = user?.uid.toString()
        var room = ""
        db.collection("User")
            .document(userIDString)
            .get()
            .addOnSuccessListener { result ->
                room = result["room"].toString()
                if(user != null){
                    if(room == "not yet"){
                        Log.d("move","방 아직 없음")
                        startActivity<RoomActivity>()
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_up_exit)
                        login_passwd.setText("")
                    }else{
                        Log.d("move","메인으로")
                        startActivity<MainActivity>()
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_up_exit)
                        login_passwd.setText("")
                    }
                }
            }
            .addOnFailureListener {
                Log.d("db","방 정보 가져오기 실패")
            }
    }
}