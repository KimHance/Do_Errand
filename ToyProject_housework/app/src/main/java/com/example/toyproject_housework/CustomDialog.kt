package com.example.toyproject_housework

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_todo.*
import java.time.LocalDate

class CustomDialog(context: Context) : View.OnClickListener{

    private var auth : FirebaseAuth? = null
    var db = FirebaseFirestore.getInstance()
    var rdb = Firebase.database.reference

    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.now()

    var isOpen = false
    private val dialog = Dialog(context)
    private lateinit var onClickListener : OnDialogClickListener

    fun setOnClickListener(listener : OnDialogClickListener){
        onClickListener = listener
    }

    fun showDialog(id : String ,code : String){ //어댑터에서 추가,완료 모드인지 param 으로 받아와야함
        dialog.setContentView(R.layout.dialog_todo)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 삐죽 티어나온거 수정
        dialog.show()

        val userID = id
        val roomCode = code


        val list = dialog.findViewById<TextView>(R.id.dialog_Todo) // 할일
        val user = dialog.findViewById<TextView>(R.id.dialog_userName) // 등록자
        val todoContext = dialog.findViewById<EditText>(R.id.dialog_context) // 내용

        db.collection("User")
            .document(userID)
            .get()
            .addOnSuccessListener {
                user.text = "${it["name"]}"
            }.addOnFailureListener {

            }

        dialog.dialog_cancle.setOnClickListener { // 취소버튼
            dialog.dismiss()
        }

        dialog.dialog_ok.setOnClickListener {  // 완료/추가mode별로 구분 해야함
            //추가모드
                db.collection("User")
                    .document(userID)
                    .get()
                    .addOnSuccessListener {
                        // rdb에 날짜별로 넣어줌
                        Log.d("방코드",roomCode)
                        Log.d("유저 아이디",userID)
                        rdb.child(roomCode).child(date.toString()).child(list.text.toString()).child("등록자").setValue(user.text.toString())
                        rdb.child(roomCode).child(date.toString()).child(list.text.toString()).child("내용").setValue(todoContext.text.toString())

                        onClickListener.onClicked(user.text.toString(),list.text.toString(),todoContext.text.toString())
                        dialog.dismiss()
                    }
        }

        dialog.dialog_img.setOnClickListener {
            // 안눌려 있으면
            if(!isOpen){
                dialog.floating_cleaning.setOnClickListener(this)
                dialog.floating_trash.setOnClickListener(this)
                dialog.floating_shopping.setOnClickListener(this)
                dialog.floating_pickup.setOnClickListener(this)
                dialog.floating_laudnry.setOnClickListener(this)
                dialog.floating_etc.setOnClickListener(this)
                dialog.floating_cooking.setOnClickListener(this)
                dialog.floating_clothes.setOnClickListener(this)
                dialog.floating_dishes.setOnClickListener(this)
                floatingOpen() // 플로팅 메뉴 열어줌
                isOpen = true
                //각 플로팅 버튼 클릭시 이벤트 구현해야함 ;;

            }else{ // 눌려져 있으면
                dialog.floating_cleaning.setOnClickListener(this)
                dialog.floating_trash.setOnClickListener(this)
                dialog.floating_shopping.setOnClickListener(this)
                dialog.floating_pickup.setOnClickListener(this)
                dialog.floating_laudnry.setOnClickListener(this)
                dialog.floating_etc.setOnClickListener(this)
                dialog.floating_cooking.setOnClickListener(this)
                dialog.floating_clothes.setOnClickListener(this)
                dialog.floating_dishes.setOnClickListener(this)

                floatingClose() // 플로팅 메뉴 닫아줌
                isOpen=false
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            dialog.floating_cleaning.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.cleaning)
                dialog.dialog_Todo.text = "청소"
                isOpen=false
            }
            dialog.floating_clothes.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.clothespin)
                dialog.dialog_Todo.text = "빨래널기&개기"
                isOpen=false
            }
            dialog.floating_cooking.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.cooking)
                dialog.dialog_Todo.text = "요리"
                isOpen=false
            }
            dialog.floating_dishes.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.apron)
                dialog.dialog_Todo.text = "설거지"
                isOpen=false
            }
            dialog.floating_etc.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.etc)
                dialog.dialog_Todo.text = "기타"
                isOpen=false
            }
            dialog.floating_laudnry.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.laundry)
                dialog.dialog_Todo.text = "빨래"
                isOpen=false
            }
            dialog.floating_pickup.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.pickup)
                dialog.dialog_Todo.text = "픽업"
                isOpen=false
            }
            dialog.floating_shopping.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.shopping)
                dialog.dialog_Todo.text = "장보기"
                isOpen=false
            }
            dialog.floating_trash.id ->{
                floatingClose()
                dialog.dialog_img.setImageResource(R.drawable.recycle)
                dialog.dialog_Todo.text = "쓰레기"
                isOpen=false
            }
        }
    }

    // 플로팅 메뉴 열기
    private fun floatingOpen(){ //눌려져 있으면
        val fabOpen = AnimationUtils.loadAnimation(dialog.context,R.anim.fab_open)
        val fabRAntiClockwise = AnimationUtils.loadAnimation(dialog.context,R.anim.rotate_anticlockwise)

        dialog.dialog_img.startAnimation(fabRAntiClockwise)
        dialog.floating_cleaning.isVisible=true
        dialog.floating_clothes.isVisible=true
        dialog.floating_cooking.isVisible=true
        dialog.floating_dishes.isVisible=true
        dialog.floating_etc.isVisible=true
        dialog.floating_laudnry.isVisible=true
        dialog.floating_pickup.isVisible=true
        dialog.floating_shopping.isVisible=true
        dialog.floating_trash.isVisible=true

        //애니메이션 실행(나타남)
        dialog.floating_cleaning.startAnimation(fabOpen)
        dialog.floating_clothes.startAnimation(fabOpen)
        dialog.floating_cooking.startAnimation(fabOpen)
        dialog.floating_dishes.startAnimation(fabOpen)
        dialog.floating_etc.startAnimation(fabOpen)
        dialog.floating_laudnry.startAnimation(fabOpen)
        dialog.floating_pickup.startAnimation(fabOpen)
        dialog.floating_shopping.startAnimation(fabOpen)
        dialog.floating_trash.startAnimation(fabOpen)

        //클릭 가능
        dialog.floating_cleaning.isClickable=true
        dialog.floating_clothes.isClickable=true
        dialog.floating_cooking.isClickable=true
        dialog.floating_dishes.isClickable=true
        dialog.floating_etc.isClickable=true
        dialog.floating_laudnry.isClickable=true
        dialog.floating_pickup.isClickable=true
        dialog.floating_shopping.isClickable=true
        dialog.floating_trash.isClickable=true

    }

    // 플로팅 메뉴 닫기
    private fun floatingClose(){
        val fabClose = AnimationUtils.loadAnimation(dialog.context,R.anim.fab_close)
        val fabRClockwise = AnimationUtils.loadAnimation(dialog.context,R.anim.rotate_clockwise)

        dialog.dialog_img.startAnimation(fabRClockwise)

        //애니메이션 실행(사라짐)
        dialog.floating_cleaning.startAnimation(fabClose)
        dialog.floating_clothes.startAnimation(fabClose)
        dialog.floating_cooking.startAnimation(fabClose)
        dialog.floating_dishes.startAnimation(fabClose)
        dialog.floating_etc.startAnimation(fabClose)
        dialog.floating_laudnry.startAnimation(fabClose)
        dialog.floating_pickup.startAnimation(fabClose)
        dialog.floating_shopping.startAnimation(fabClose)
        dialog.floating_trash.startAnimation(fabClose)

        //클릭 불가능
        dialog.floating_cleaning.isClickable=false
        dialog.floating_clothes.isClickable=false
        dialog.floating_cooking.isClickable=false
        dialog.floating_dishes.isClickable=false
        dialog.floating_etc.isClickable=false
        dialog.floating_laudnry.isClickable=false
        dialog.floating_pickup.isClickable=false
        dialog.floating_shopping.isClickable=false
        dialog.floating_trash.isClickable=false

    }

    interface OnDialogClickListener{
        fun onClicked(user : String , todo : String, todoContext: String)
    }

}