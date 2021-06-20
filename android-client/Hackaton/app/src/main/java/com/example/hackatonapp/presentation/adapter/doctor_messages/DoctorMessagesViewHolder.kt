package com.example.hackatonapp.presentation.adapter.doctor_messages

import androidx.recyclerview.widget.RecyclerView
import com.example.hackatonapp.data.database.entities.DoctorMessageEntity
import com.example.hackatonapp.databinding.ListItemDoctorMessageBinding

class DoctorMessagesViewHolder(private val binding: ListItemDoctorMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: DoctorMessageEntity) {
        binding.messageTextView.text = model.comment
    }
}