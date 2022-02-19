package com.example.studentrollcall.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentrollcall.databinding.ItemClassBinding
import com.example.studentrollcall.fragment.HomeFragmentDirections
import com.example.studentrollcall.model.Class

class ClassAdapter(private val listener: OnItemClickListener) : ListAdapter<Class, ClassAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassAdapter.ViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemClassBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position))
                    }
                }
            }
        }

        fun bind(_class: Class) {
            binding.apply {
                tvTitle.text = _class.title
                tvDescription.text = _class.description
                buttonMore.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemOptionClick(getItem(position))
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Class>() {
        override fun areItemsTheSame(oldItem: Class, newItem: Class): Boolean {
            return oldItem.shortId == newItem.shortId
        }

        override fun areContentsTheSame(oldItem: Class, newItem: Class): Boolean {
            return oldItem == newItem
        }

    }

    interface OnItemClickListener {
        fun onItemClick(_class: Class)
        fun onItemOptionClick(_class: Class)
    }

}