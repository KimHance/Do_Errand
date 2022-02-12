package com.example.toyproject_housework

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    private var db = FirebaseFirestore.getInstance()
    private var rdb = Firebase.database

    var code : String = ""
    var name : String = ""
    var role : String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.now()

    lateinit var todoAdapter: RecyclerTodoAdapter

    private val addItem = mutableListOf<Todo>()

    lateinit var id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addItem.add(Todo("추가하기","처음꺼","처음꺼",true))

        auth = FirebaseAuth.getInstance()

        val user = auth?.currentUser
        val userIDString = user?.uid.toString()
        id = userIDString

        db.collection("User")
            .document(userIDString)
            .get()
            .addOnSuccessListener {
                code = it["room"].toString()
                name = it["name"].toString()
                role = it["role"].toString()

                Log.d("룸","순서좀 보자")

                main_name.text = "${name}님"
                when(role){
                    "아빠" -> { main_userImg.setImageResource(R.drawable.green_dad) }
                    "엄마" -> { main_userImg.setImageResource(R.drawable.green_mom) }
                    "아들" -> { main_userImg.setImageResource(R.drawable.green_son) }
                    "딸" -> { main_userImg.setImageResource(R.drawable.green_daughter) }
                }
                rdb.getReference(code).child((date.toString())).addChildEventListener(object : ChildEventListener{

                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.d("알디비", "데이터 추가됨 $snapshot")
                        val todo = snapshot.key
                        val map = snapshot.value as Map<*,*>
                        val context = map["내용"].toString()
                        val name = map["등록자"].toString()
                        addItem.add(Todo(todo!!,name,context,false))
                        initRecyclerTodo(this@MainActivity)
                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        Log.d("알디비","데이터 제거됨 $snapshot")
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.d("알디비","데이터 수정됨")
                    }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.d("알디비","데이터 순서 바뀜")
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

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

//       Handler().postDelayed({
//           // db에서 코드번호 받아오는데 걸리는 지연시간 생각
//           if(code!=""){ //코드 받아오면
//               rdb.getReference(code).child((date.toString())).addChildEventListener(object : ChildEventListener{
//
//                   override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                       Log.d("알디비", "데이터 추가됨 $snapshot")
//                       val todo = snapshot.key
//                       val map = snapshot.value as Map<*,*>
//                       val context = map["내용"].toString()
//                       val name = map["등록자"].toString()
//                       addItem.add(Todo(todo!!,name,context,false))
//                       initRecyclerTodo(this@MainActivity)
//                   }
//                   override fun onChildRemoved(snapshot: DataSnapshot) {
//                       Log.d("알디비","데이터 제거됨 $snapshot")
//                   }
//
//                   override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                       Log.d("알디비","데이터 수정됨")
//                   }
//                   override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                       Log.d("알디비","데이터 순서 바뀜")
//                   }
//                   override fun onCancelled(error: DatabaseError) {
//                       TODO("Not yet implemented")
//                   }
//               })
//           }
//       },500L)


/*        out.setOnClickListener {
            var data = mutableMapOf<String,Any>()
            data["room"] = "not yet"
            db.collection("User")
                .document(userIDString)
                .update(data)
                .addOnSuccessListener {
                    val updates = hashMapOf<String,Any>(
                        name to FieldValue.delete()
                    )
                    db.collection("Room")
                        .document(code)
                        .update(updates)
                    toast("방 나가기 성공")
                    finish()
                }
                .addOnFailureListener {
                    toast("방 나가기 실패")
                }
        }*/



    }

    private fun getCode(code : String): String {
        val returnCode = code
        return returnCode
    }

    private fun initRecyclerTodo(context : Context) {
        Log.d("리사이클러","이닛 호출")
        todoAdapter = RecyclerTodoAdapter(this)
        Recycler_todoList.adapter = todoAdapter
        todoAdapter.items = addItem
        Log.d("아이템", "현재 아이템 $addItem")

        todoAdapter.setOnItemClickListener(object : RecyclerTodoAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Todo, pos: Int) {
                Log.d("아이템","클릭됨")
                val dialog = CustomDialog(context)
                if(!data.add){
                    // 완료모드

                }else{
                    // 추가모드
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
                            addItem.apply{
                                add(Todo(todo,user,todoContext,false))
                            }
                        }
                    })
                }
            }
        })
    }
}