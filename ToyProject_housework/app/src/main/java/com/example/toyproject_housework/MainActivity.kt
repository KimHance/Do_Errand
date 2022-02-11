package com.example.toyproject_housework

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    var db = FirebaseFirestore.getInstance()

    var code : String = ""
    var name : String = ""
    var role : String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.now()

    lateinit var todoAdapter: RecyclerTodoAdapter

    val items = mutableListOf<Todo>()

    lateinit var id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()


        val user = auth?.currentUser
        val userIDString = user?.uid.toString()
        id = userIDString

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
                when(role){
                    "아빠" -> { main_userImg.setImageResource(R.drawable.green_dad) }
                    "엄마" -> { main_userImg.setImageResource(R.drawable.green_mom) }
                    "아들" -> { main_userImg.setImageResource(R.drawable.green_son) }
                    "딸" -> { main_userImg.setImageResource(R.drawable.green_daughter) }
                }
            }
            .addOnFailureListener {
                Log.d("디비","실패")
            }

        val noticeList = ArrayList<Notice>()

        noticeList.add(Notice("나","임시 공지사항"))
        noticeList.add(Notice("현수","응 임시"))
        noticeList.add(Notice("접니다","킹현수;; 똑똑 지니어스"))
        noticeList.add(Notice("저에요","ㅎㅇㅎㅇ 임시"))

        val noticeAdapter = RecyclerNoticeAdapter(noticeList)
        Recycler_noticeList.adapter = noticeAdapter

        initRecyclerTodo(this)

//        main_userImg.setOnClickListener {
//            val dialog = CustomDialog(this)
//            dialog.showDialog()
//        }


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


    private fun initRecyclerTodo(context : Context) {
        todoAdapter = RecyclerTodoAdapter(this)
        Recycler_todoList.adapter = todoAdapter

        items.apply{
            add(Todo("추가하기","처음꺼","처음꺼",true,"현재"))
        }

        todoAdapter.items = items
        todoAdapter.notifyDataSetChanged()
        todoAdapter.setOnItemClickListener(object : RecyclerTodoAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Todo, pos: Int) {
                Log.d("아이템","클릭됨")
                val dialog = CustomDialog(context)
                if(!data.add){
                }else{
                    // item의 데이터가 비어있음
                    // 다이어로그에서 넘어온 데이터로 rdb 추가
                        val userID = auth?.currentUser?.uid.toString()
                    db.collection("User")
                        .document(userID)
                        .get()
                        .addOnSuccessListener {
                            dialog.showDialog(userID,it["room"].toString())
                        }
                    dialog.setOnClickListener(object :  CustomDialog.OnDialogClickListener {
                        override fun onClicked(user: String, todo: String, todoContext: String) {
                            //다이얼로그에서 데이터 넘겨줌
                            items.apply{
                                add(Todo(todo,user,todoContext,false,date.toString()))
                            }
                        }
                    })
                }
            }
        })
    }
}