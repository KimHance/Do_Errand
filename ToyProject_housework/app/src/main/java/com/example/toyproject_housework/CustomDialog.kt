package com.example.toyproject_housework

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.dialog_todo.*
import java.time.LocalDate

class CustomDialog(context: Context) {

    private var auth : FirebaseAuth? = null
    var db = FirebaseFirestore.getInstance()
    var rdb = Firebase.database.reference

    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.now()

    private val dialog = Dialog(context)
    private lateinit var onClickListener : OnDialogClickListener

//    val user = dialog.findViewById<TextView>(R.id.dialog_userName) // 등록자
//    val list = dialog.findViewById<TextView>(R.id.dialog_Todo) // 할일
//    val todoContext = dialog.findViewById<EditText>(R.id.dialog_context) // 내용
//    val btn_ok = dialog.findViewById<Button>(R.id.dialog_ok) // 완료 버튼
//    val img = dialog.findViewById<CircleImageView>(R.id.dialog_img) // 이미지

    fun setOnClickListener(listener : OnDialogClickListener){
        onClickListener = listener
    }

    fun showDialog(id : String ,code : String){
        dialog.setContentView(R.layout.dialog_todo)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val userID = id
        val roomCode = code

        val list = dialog.findViewById<TextView>(R.id.dialog_Todo) // 할일
        val user = dialog.findViewById<TextView>(R.id.dialog_userName) // 등록자
        val todoContext = dialog.findViewById<EditText>(R.id.dialog_context) // 내용
        val btn_cancle = dialog.findViewById<Button>(R.id.dialog_cancle) // 취소 버튼
        val btn_ok = dialog.findViewById<Button>(R.id.dialog_ok) // 완료 버튼
        val img = dialog.findViewById<CircleImageView>(R.id.dialog_img) // 이미지

        dialog.dialog_cancle.setOnClickListener {
            dialog.dismiss()
        }

        dialog.dialog_ok.setOnClickListener {
                db.collection("User")
                    .document(userID)
                    .get()
                    .addOnSuccessListener {
                        Log.d("방코드",roomCode)
                        Log.d("유저 아이디",userID)

                        rdb.child(roomCode).child(date.toString()).child(list.text.toString()).child("등록자").setValue(user.text.toString())
                        rdb.child(roomCode).child(date.toString()).child(list.text.toString()).child("내용").setValue(todoContext.text.toString())
                        onClickListener.onClicked(user.text.toString(),list.text.toString(),todoContext.text.toString())
                        dialog.dismiss()
                    }
//            onClickListener.onClicked(user.text.toString(),list.text.toString(),todoContext.text.toString())
//            dialog.dismiss()
        }

    }

    interface OnDialogClickListener{
        fun onClicked(user : String , todo : String, todoContext: String)
    }
}