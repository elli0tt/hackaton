package com.example.hackatonapp.presentation.adapter.doctor_messages

import androidx.recyclerview.widget.RecyclerView
import com.example.hackatonapp.data.database.entities.DoctorMessageEntity
import com.example.hackatonapp.databinding.ListItemDoctorMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class DoctorMessagesViewHolder(private val binding: ListItemDoctorMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun bind(model: DoctorMessageEntity) {
        binding.messageTextView.text = model.comment

        val date = Date(model.dateTime)
        binding.dateTextView.text = dateFormat.format(date)
        binding.timeTextView.text = timeFormat.format(date)
    }
}