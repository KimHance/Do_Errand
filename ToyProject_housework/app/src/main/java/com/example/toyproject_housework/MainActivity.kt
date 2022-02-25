package com.example.toyproject_housework

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.toyproject_housework.Adapter.RecyclerNoticeAdapter
import com.example.toyproject_housework.Adapter.RecyclerTodoAdapter
import com.example.toyproject_housework.Data.Notice
import com.example.toyproject_housework.Data.Todo
import com.example.toyproject_housework.Dialog.NoticeDialog
import com.example.toyproject_housework.Dialog.TodoDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var auth : FirebaseAuth? = null
    private var db = FirebaseFirestore.getInstance()
    private var rdb = Firebase.database
    private var TAG = "MainActivity"

    var code : String = ""
    var name : String = ""
    var role : String = ""

    var menuOpen = false

    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.now()

    lateinit var todoAdapter: RecyclerTodoAdapter
    lateinit var noticeAdapter : RecyclerNoticeAdapter
    private val addItem = mutableListOf<Todo>()
    private val noticeItem = mutableListOf<Notice>()
    lateinit var id : String



    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addItem.add(Todo("추가하기","처음꺼","처음꺼",true))
        initRecyclerTodo(this)


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

                main_userAdd.text = "등록수 : ${it["add"].toString()}"
                main_userDo.text = "완료수 : ${it["do"].toString()}"
                main_name.text = "${name}님"

                when(role){
                    "아빠" -> { main_userImg.setImageResource(R.drawable.green_dad) }
                    "엄마" -> { main_userImg.setImageResource(R.drawable.green_mom) }
                    "아들" -> { main_userImg.setImageResource(R.drawable.green_son) }
                    "딸" -> { main_userImg.setImageResource(R.drawable.green_daughter) }
                }

                // 할일목록
                val roomDB = rdb.getReference(code).child((date.toString()))
                roomDB.addChildEventListener(object : ChildEventListener{
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.d("알디비", "데이터 추가됨 $snapshot")

                        val todo = snapshot.key
                        val map = snapshot.value as Map<*,*>
                        val context = map["내용"].toString()
                        val name = map["등록자"].toString()
                        addItem.add(Todo(todo!!,name,context,false))
                        Log.d("알디비",addItem.toString())
                        initRecyclerTodo(this@MainActivity)
                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        Log.d("알디비","데이터 제거됨 $snapshot")
                        val key = snapshot.key
                        val map = snapshot.value as Map<*,*>
                        val context = map["내용"].toString()
                        val name = map["등록자"].toString()

                        val todo = Todo(key!!,name,context,false)
                        addItem.remove(todo)

                        initRecyclerTodo(this@MainActivity)
                    }
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { Log.d("알디비","데이터 수정됨") }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { Log.d("알디비","데이터 순서 바뀜") }
                    override fun onCancelled(error: DatabaseError) {}
                })

                // 공지사항
                val noticeDB = rdb.getReference(code).child("notice")
                noticeDB.addChildEventListener(object : ChildEventListener{
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val noticeTitle = snapshot.key
                        val map = snapshot.value as Map<*,*>
                        val context = map["내용"].toString()
                        val name = map["등록자"].toString()
                        noticeItem.add(Notice(noticeTitle.toString(),name,context))
                        initRecyclerNotice(this@MainActivity)
                    }
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        val noticeTitle = snapshot.key
                        val map = snapshot.value as Map<*,*>
                        val context = map["내용"].toString()
                        val name = map["등록자"].toString()

                        val notice = Notice(noticeTitle.toString(),name,context)
                        noticeItem.remove(notice)
                        initRecyclerNotice(this@MainActivity)
                    }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {}
                })

            }
            .addOnFailureListener {
                Log.d("디비","실패")
            }

        main_family.setOnClickListener(this)
        main_invite.setOnClickListener(this)
        main_notice.setOnClickListener(this)

        main_menu.setOnClickListener{
            if(menuOpen){
                closeMenu()
                menuOpen = false
            }else{
                openMenu()
                menuOpen = true
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            db.collection("User")
                .document(userIDString)
                .get()
                .addOnSuccessListener {
                    val room = it["room"].toString()
                    val name = it["name"].toString()
                    rdb.getReference(room).child("userToken").child(name).setValue(token)
                }

            // Log and toast
            val msg = token
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

    }

    private fun initRecyclerNotice(context:Context){
        Log.d("리사이클러","Notice 호출")
        noticeAdapter = RecyclerNoticeAdapter(this)
        Recycler_noticeList.adapter = noticeAdapter
        noticeAdapter.items = noticeItem
        Log.d("Notice아이템", "현재 아이템 $noticeItem")

        noticeAdapter.setOnItemClickListener(object : RecyclerNoticeAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Notice, pos: Int) {
                val dialog = NoticeDialog(context)
                val userID = auth?.currentUser?.uid.toString()

                db.collection("User")
                    .document(userID)
                    .get()
                    .addOnSuccessListener {
                        dialog.showDialog(userID,it["room"].toString(),data)
                    }

                dialog.setOnClickListener(object : NoticeDialog.OnDialogClickListener{
                    override fun onClicked(editedContext: String, editedTitle : String) {
                        initRecyclerNotice(this@MainActivity)
                    }
                })
            }

        })
    }

    private fun initRecyclerTodo(context : Context) {
        Log.d("리사이클러","Todo 호출")
        todoAdapter = RecyclerTodoAdapter(this)
        Recycler_todoList.adapter = todoAdapter
        todoAdapter.items = addItem
        Log.d("Todo아이템", "현재 아이템 $addItem")

        todoAdapter.setOnItemClickListener(object : RecyclerTodoAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Todo, pos: Int) {
                Log.d("아이템","클릭됨")
                val dialog = TodoDialog(context)
                val userID = auth?.currentUser?.uid.toString()

                db.collection("User")
                    .document(userID)
                    .get()
                    .addOnSuccessListener {
                        dialog.showDialog(userID,it["room"].toString(),data)
                    }

                dialog.setOnClickListener(object :  TodoDialog.OnDialogClickListener {
                    override fun onClicked(userDo: String, userAdd: String) {
                        main_userDo.text = "완료수 : $userDo"
                        main_userAdd.text = "등록수 : $userAdd"
                    }

                })
            }
        })
    }

    private fun openMenu(){ //메뉴 열기
        val fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open)
        main_family.isVisible = true
        main_invite.isVisible = true
        main_notice.isVisible = true
        text_family.isVisible = true
        text_invite.isVisible = true
        text_notice.isVisible = true

        main_family.startAnimation(fabOpen)
        main_invite.startAnimation(fabOpen)
        main_notice.startAnimation(fabOpen)
        text_family.startAnimation(fabOpen)
        text_invite.startAnimation(fabOpen)
        text_notice.startAnimation(fabOpen)

        main_family.isClickable = true
        main_invite.isClickable = true
        main_notice.isClickable = true

    }

    private fun closeMenu(){ //메뉴 닫기
        val fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close)

        main_family.startAnimation(fabClose)
        main_invite.startAnimation(fabClose)
        main_notice.startAnimation(fabClose)
        text_family.startAnimation(fabClose)
        text_invite.startAnimation(fabClose)
        text_notice.startAnimation(fabClose)

        main_family.isClickable = false
        main_invite.isClickable = false
        main_notice.isClickable = false

    }

    override fun onClick(v: View) {
        when(v.id){
            this.main_invite.id -> {
                var intent = Intent(this,InviteActivity::class.java)
                intent.putExtra("roomCode",code)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_up,R.anim.slide_up_exit)
                closeMenu()
                menuOpen = false
            }
            this.main_family.id -> {
                var intent = Intent(this,FamilyActivity::class.java)
                intent.putExtra("roomCode",code)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_up,R.anim.slide_up_exit)
                closeMenu()
                menuOpen = false
            }
            this.main_notice.id -> {
                var intent = Intent(this,AddNoticeActivity::class.java)
                intent.putExtra("name",name)
                intent.putExtra("roomCode",code)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_up,R.anim.slide_up_exit)
                closeMenu()
                menuOpen = false
            }
        }
    }
}