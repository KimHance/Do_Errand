package com.example.toyproject_housework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_notice.view.*

class RecyclerNoticeAdapter(private val items : ArrayList<Notice>) :
    RecyclerView.Adapter<RecyclerNoticeAdapter.ViewHolder>(){
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerNoticeAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "목록 잘 눌린다", Toast.LENGTH_SHORT).show()
        }
        holder.apply{
            bind(listener,item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerNoticeAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_notice,parent,false)
        return RecyclerNoticeAdapter.ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view : View = v
        fun bind(listener : View.OnClickListener, item: Notice){
            view.userName.text = item.userName
            view.notice.text = item.notice
            view.setOnClickListener { listener }
        }
    }
}