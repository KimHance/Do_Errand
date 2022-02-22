package com.example.toyproject_housework.Dialog

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.INPUT_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import com.example.toyproject_housework.Data.Notice
import com.example.toyproject_housework.R
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NoticeDialog(context : Context) {


    val context= context
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
        val btnFinish = dialog.findViewById<Button>(R.id.notice_finish) // 수정 완료
        val btnCancel = dialog.findViewById<Button>(R.id.notice_cancel) // 취소 버튼

        title.setText(notice.title)
        userName.text = notice.userName
        noticeContext.setText(notice.notice)

        //삭제하기 위해 미리 내용들 저장
        val bfTitle = title.text.toString()
        val bfContext = noticeContext.text.toString()
        var preName = ""
        // 현재 유저와 공지사항 등록자 비교
        db.collection("User")
            .document(id)
            .get()
            .addOnSuccessListener {
                preName = it["name"].toString()
                if(preName == notice.userName){
//                    mode = true // 같으면 수정버튼 활성화
                    btnModify.isVisible=true
                    btnModify.isClickable=true
                    btnDelete.isVisible=true
                    btnDelete.isClickable=true
//                    noticeContext.isEnabled=true
//                    title.isEnabled=true
                }
            }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            rdb.child(code).child("notice").child(notice.title).removeValue()
            dialog.dismiss()
        }
        btnModify.setOnClickListener{
            //수정하기, 삭제하기 비활성화
            btnDelete.isVisible=false
            btnDelete.isClickable=false
            btnModify.isVisible=false
            btnModify.isClickable=false

            //수정 취소버튼 활성화
            btnFinish.isVisible=true
            btnFinish.isClickable=true
            btnCancel.isVisible=true
            btnCancel.isClickable=true

            noticeContext.isEnabled=true
            title.isEnabled=true
            title.requestFocus()
            title.setSelection(title.length())


        }
        btnCancel.setOnClickListener {
            //수정하기, 삭제하기 활성화
            btnDelete.isVisible=true
            btnDelete.isClickable=true
            btnModify.isVisible=true
            btnModify.isClickable=true

            //수정 취소버튼 비활성화
            btnFinish.isVisible=false
            btnFinish.isClickable=false
            btnCancel.isVisible=false
            btnCancel.isClickable=false

            noticeContext.isEnabled=false
            title.isEnabled=false

            title.setText(notice.title)
            noticeContext.setText(notice.notice)
        }
        btnFinish.setOnClickListener {
            val map = mutableMapOf(
                "등록자" to preName ,
                "내용" to noticeContext.text.toString()
            )
            rdb.child(code).child("notice").child(bfTitle).removeValue()
            rdb.child(code).child("notice").child(title.text.toString()).setValue(map)
            dialog.dismiss()
        }





    }

    interface OnDialogClickListener{
        fun onClicked(editedContext : String,editedTitle : String)
    }
}