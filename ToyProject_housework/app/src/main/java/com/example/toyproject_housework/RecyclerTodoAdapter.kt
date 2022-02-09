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
            val pos = adapterPosition
            if(pos!=RecyclerView.NO_POSITION){
                view.findViewById<CardView>(R.id.item_card).setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
        }
    }


}