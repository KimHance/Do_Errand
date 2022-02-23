package com.example.toyproject_housework.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toyproject_housework.Data.Family
import com.example.toyproject_housework.R
import kotlinx.android.synthetic.main.item_grid_family.view.*

class RecyclerFamilyAdapter( private val context: Context) : RecyclerView.Adapter<RecyclerFamilyAdapter.ViewHolder>() {

    var items = mutableListOf<Family>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerFamilyAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_grid_family,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerFamilyAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        fun bind(item: Family){
            view.family_name.text = item.name

            when(item.role){
                "아들" ->{view.family_img.setImageResource(R.drawable.green_son)}
                "아빠" ->{view.family_img.setImageResource(R.drawable.green_dad)}
                "엄마" ->{view.family_img.setImageResource(R.drawable.green_mom)}
                "딸" ->{view.family_img.setImageResource(R.drawable.green_daughter)}
            }
        }
    }
}