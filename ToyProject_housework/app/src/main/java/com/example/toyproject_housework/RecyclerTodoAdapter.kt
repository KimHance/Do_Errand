package com.example.toyproject_housework


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_todo.view.*


class RecyclerTodoAdapter(private val items : ArrayList<Todo>) :
RecyclerView.Adapter<RecyclerTodoAdapter.ViewHolder>(){
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerTodoAdapter.ViewHolder, position: Int) {
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
    ): RecyclerTodoAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_todo,parent,false)
        return RecyclerTodoAdapter.ViewHolder(inflatedView)
    }
    
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view : View = v
        fun bind(listener : View.OnClickListener, item: Todo){
            view.todoTitle.text = item.title
            view.setOnClickListener { listener }
        }
    }
}