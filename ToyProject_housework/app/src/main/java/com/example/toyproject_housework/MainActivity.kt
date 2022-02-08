package com.example.toyproject_housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    var db = FirebaseFirestore.getInstance()

    var code : String = ""
    var name : String = ""
    var role : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()


        val user = auth?.currentUser
        val userIDString = user?.uid.toString()

        var ex = db.collection("User").whereEqualTo("role","아들")
        Log.d("실험","$ex")
        db.collection("User")
            .document(userIDString)
            .get()
            .addOnSuccessListener {
                code = it["room"].toString()
                name = it["name"].toString()
                role = it["role"].toString()
                main_name.text = "${name}님"
            }
            .addOnFailureListener {
                Log.d("디비","실패")
            }

        val todoList = ArrayList<Todo>()
        val noticeList = ArrayList<Notice>()

        todoList.add(Todo(ContextCompat.getDrawable(this,R.drawable.ic_baseline_plus)!!,"추가하기","나","별거없음",true,"오늘"))
        todoList.add(Todo(ContextCompat.getDrawable(this,R.drawable.ic_baseline_plus)!!,"이건 일단 임시","나","별거없음",true,"오늘"))
        todoList.add(Todo(ContextCompat.getDrawable(this,R.drawable.ic_baseline_plus)!!,"임시 2","나","별거없음",true,"오늘"))
        todoList.add(Todo(ContextCompat.getDrawable(this,R.drawable.ic_baseline_plus)!!,"임시 3","나","별거없음",true,"오늘"))
        val todoAdapter = RecyclerTodoAdapter(todoList)
        Recycler_todoList.adapter = todoAdapter

        noticeList.add(Notice("나","임시 공지사항"))
        noticeList.add(Notice("현수","응 임시"))
        noticeList.add(Notice("접니다","킹현수;; 똑똑 지니어스"))
        noticeList.add(Notice("저에요","ㅎㅇㅎㅇ 임시"))

        val noticeAdapter = RecyclerNoticeAdapter(noticeList)
        Recycler_noticeList.adapter = noticeAdapter










//        out.setOnClickListener {
//            var data = mutableMapOf<String,Any>()
//            data["room"] = "not yet"
//            db.collection("User")
//                .document(userIDString)
//                .update(data)
//                .addOnSuccessListener {
//                    val updates = hashMapOf<String,Any>(
//                        name to FieldValue.delete()
//                    )
//                    db.collection("Room")
//                        .document(code)
//                        .update(updates)
//                    toast("방 나가기 성공")
//                    finish()
//                }
//                .addOnFailureListener {
//                    toast("방 나가기 실패")
//                }
//        }

    }
}