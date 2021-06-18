package com.example.hackatonapp.presentation.adapter.patient_data_list

import androidx.recyclerview.widget.RecyclerView
import com.example.hackatonapp.data.entities.PatientNoteEntity
import com.example.hackatonapp.databinding.ListItemPatientDataBinding
import java.text.SimpleDateFormat
import java.util.*

class PatientDataListViewHolder(
    private val binding: ListItemPatientDataBinding,
    private val onItemClickListener: PatientDataListAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun bind(model: PatientNoteEntity) {
        binding.apply {
            pressureTextView.text = model.pressure
            pulseTextView.text = model.pulse

            val date = Date(model.dateTimeCreated)
            dateTextView.text = dateFormat.format(date)
            timeTextView.text = timeFormat.format(date)
        }

        itemView.setOnClickListener {
            onItemClickListener.onItemClick(bindingAdapterPosition)
        }
    }
}