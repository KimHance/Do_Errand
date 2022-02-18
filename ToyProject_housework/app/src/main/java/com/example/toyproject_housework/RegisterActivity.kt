package com.example.toyproject_housework

import android.os.Bundle
import android.system.StructMsghdr
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast


class RegisterActivity : AppCompatActivity(){

    private var auth : FirebaseAuth? = null
    var role = ""
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        auth = Firebase.auth

        // 스피너 구현
        val item : Array<String> = resources.getStringArray(R.array.family_array)
        val adapter = ArrayAdapter(this,R.layout.spinner_item,item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position !=0 ){
                    role = item[position]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        register_ok.setOnClickListener {
            val email = register_id.text.toString()
            val passwd = register_passwd.text.toString()
            val name = register_name.text.toString()

            if(email.isNotEmpty() && passwd.isNotEmpty() && name.isNotEmpty() && role.isNotEmpty()){
                if(passwd.length<6){
                    toast("비밀번호는 6자리 이상입니다.")
                }else{
                    createUser(email,passwd)
                }
            }else{
                toast("문항을 모두 확인해 주세요.")
            }
        }
        // 뒤로가기
        register_cancel.setOnClickListener {
            finish()
        }
    }
    private fun createUser(email: String, passwd: String){

            auth?.createUserWithEmailAndPassword(email,passwd)
                ?.addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        toast("계정 생성 완료")
                        val user = auth!!.currentUser
                        val userIDString = user?.uid.toString()
                        val data = hashMapOf(
                            "userID" to userIDString,
                            "name" to register_name.text.toString(),
                            "role" to role,
                            "room" to "not yet",
                            "add" to 0,
                            "do" to 0
                        )
                        db.collection("User")
                            .document(userIDString)
                            .set(data)
                            .addOnSuccessListener {
                                Log.d("db","파이어스토어 유저 저장 성공")
                            }
                            .addOnFailureListener {
                                Log.d("db","파이어스토어 유저 저장 실패")
                            }
                        finish()
                    }
                }
    }
}