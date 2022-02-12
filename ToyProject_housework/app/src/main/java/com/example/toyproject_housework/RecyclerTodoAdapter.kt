package com.example.toyproject_housework


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_todo.view.*
import org.jetbrains.anko.find


class RecyclerTodoAdapter( private val context: Context) : RecyclerView.Adapter<RecyclerTodoAdapter.ViewHolder>(){

    var items = mutableListOf<Todo>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data : Todo, pos : Int)
    }
    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_todo,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view : View = v

        fun bind(item: Todo){
            view.todoTitle.text = item.title

            when(view.todoTitle.text){ //타이틀 별로 이미지 바꿔줘야함
                "청소" -> {view.todoImg.setImageResource(R.drawable.cleaning)}
                "빨래널기&개기" -> {view.todoImg.setImageResource(R.drawable.clothespin)}
                "요리" -> {view.todoImg.setImageResource(R.drawable.cooking)}
                "설거지" -> {view.todoImg.setImageResource(R.drawable.apron)}
                "기타" -> {view.todoImg.setImageResource(R.drawable.etc)}
                "빨래" -> {view.todoImg.setImageResource(R.drawable.laundry)}
                "픽업" -> {view.todoImg.setImageResource(R.drawable.pickup)}
                "장보기" -> {view.todoImg.setImageResource(R.drawable.shopping)}
                "쓰레기" -> {view.todoImg.setImageResource(R.drawable.recycle)}
            }

            val pos = adapterPosition
            if(pos!=RecyclerView.NO_POSITION){
                view.findViewById<CardView>(R.id.item_card).setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
        }
    }


}