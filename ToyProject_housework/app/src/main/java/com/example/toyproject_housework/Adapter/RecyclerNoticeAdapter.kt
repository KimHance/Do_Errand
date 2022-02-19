package com.example.toyproject_housework.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.toyproject_housework.Data.Notice
import com.example.toyproject_housework.R
import kotlinx.android.synthetic.main.item_list_notice.view.*

class RecyclerNoticeAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerNoticeAdapter.ViewHolder>(){

    var items = mutableListOf<Notice>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data : Notice, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_notice,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(v:View): RecyclerView.ViewHolder(v){
        private var view : View = v

        fun bind(item: Notice){
            view.userName.text = item.title

            val pos =adapterPosition
            if(pos!=RecyclerView.NO_POSITION){
                view.findViewById<CardView>(R.id.notice_card).setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
        }
    }
}