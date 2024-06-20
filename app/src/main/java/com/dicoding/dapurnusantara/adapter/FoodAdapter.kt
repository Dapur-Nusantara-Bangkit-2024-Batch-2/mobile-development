package com.dicoding.dapurnusantara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.dataclass.FoodItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FoodAdapter(private val foodList: List<FoodItem>, private val deleteAction: (Int) -> Unit) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_img)
        val nameView: TextView = itemView.findViewById(R.id.item_name)
        val caloriesView: TextView = itemView.findViewById(R.id.item_desc)
        val deleteButton: FloatingActionButton = itemView.findViewById(R.id.button_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.imageView.setImageURI(foodItem.imageUri)
        holder.nameView.text = foodItem.name
        holder.caloriesView.text = foodItem.calories
        holder.deleteButton.setOnClickListener {
            deleteAction(position)
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}
