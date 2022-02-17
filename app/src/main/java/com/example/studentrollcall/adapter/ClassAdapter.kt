package com.example.studentrollcall.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentrollcall.databinding.ItemClassBinding
import com.example.studentrollcall.model.Class

class ClassAdapter: RecyclerView.Adapter<ClassAdapter.ViewHolder>() {

    var classes = ArrayList<Class>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassAdapter.ViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassAdapter.ViewHolder, position: Int) {
        holder.bind(classes[position])
    }

    override fun getItemCount(): Int {
        return classes.size
    }

    inner class ViewHolder(private val binding: ItemClassBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(_class: Class) {
            binding.apply {
                tvTitle.text = _class.title
            }
        }
    }

}