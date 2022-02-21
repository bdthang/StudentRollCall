package com.example.studentrollcall.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.ItemClassBinding
import com.example.studentrollcall.model.Class
import java.util.*

class ClassAdapter(private val listener: OnItemClickListener, private val isTeacher: Boolean) : ListAdapter<Class, ClassAdapter.ViewHolder>(DiffCallback()) {

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

                val time = Calendar.getInstance().timeInMillis
                val endTime = _class.timeStart.time + _class.timeLimit * 60 * 1000
                if (time < endTime) {
                    containerItem.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.warning))
                }

                if (isTeacher) {
                    buttonEditClass.setOnClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemEditClick(getItem(position))
                        }
                    }
                } else {
                    buttonEditClass.visibility = View.GONE
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
        fun onItemEditClick(_class: Class)
    }

}