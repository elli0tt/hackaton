package com.example.hackatonapp.presentation.adapter.doctor_messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.hackatonapp.data.database.entities.DoctorMessageEntity
import com.example.hackatonapp.databinding.ListItemDoctorMessageBinding

class DoctorMessagesAdapter :
    ListAdapter<DoctorMessageEntity, DoctorMessagesViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DoctorMessageEntity>() {
            override fun areItemsTheSame(
                oldItem: DoctorMessageEntity,
                newItem: DoctorMessageEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DoctorMessageEntity,
                newItem: DoctorMessageEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorMessagesViewHolder {
        val binding = ListItemDoctorMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DoctorMessagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorMessagesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}