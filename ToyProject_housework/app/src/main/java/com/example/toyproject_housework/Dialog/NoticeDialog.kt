package com.example.toyproject_housework.Dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.*
import androidx.core.view.isVisible
import com.example.toyproject_housework.Data.Notice
import com.example.toyproject_housework.R
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NoticeDialog(context : Context) {

    var db = FirebaseFirestore.getInstance()
    var rdb = Firebase.database.reference

    private val dialog = Dialog(context)
    private lateinit var onClickListener : OnDialogClickListener


    fun setOnClickListener(listener : OnDialogClickListener){
        onClickListener = listener
    }

    fun showDialog(id: String, code: String, notice: Notice){
        dialog.setContentView(R.layout.dialog_notice)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 삐죽 티어나온거 수정
        dialog.show()

        val title = dialog.findViewById<EditText>(R.id.notice_title) // 제목
        val userName = dialog.findViewById<TextView>(R.id.notice_name) // 작성자
        val btnClose = dialog.findViewById<ImageButton>(R.id.notice_close) // 닫기버튼
        val noticeContext = dialog.findViewById<EditText>(R.id.notice_context) // 내용
        val btnModify = dialog.findViewById<Button>(R.id.notice_modify) // 수정 버튼
        val btnDelete = dialog.findViewById<Button>(R.id.notice_delete) // 삭제 버튼

//        var mode = false

        title.setText(notice.title)
        userName.text = notice.userName
        noticeContext.setText(notice.notice)

        // 현재 유저와 공지사항 등록자 비교
        db.collection("User")
            .document(id)
            .get()
            .addOnSuccessListener {
                if(it["name"] == notice.userName){
//                    mode = true // 같으면 수정버튼 활성화
                    btnModify.isVisible=true
                    btnModify.isClickable=true
                    btnDelete.isVisible=true
                    btnDelete.isClickable=true
                    noticeContext.isEnabled=true
                    title.isEnabled=true
                }
            }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            //데이터 삭제 구현
        }
        btnModify.setOnClickListener{

            //수정한 데이터 넘겨줌
            onClickListener.onClicked(noticeContext.text.toString())
        }





    }

    interface OnDialogClickListener{
        fun onClicked(editedContext : String)
    }
}