package com.example.hackatonapp.presentation.adapter.patient_data_list

import androidx.recyclerview.widget.RecyclerView
import com.example.hackatonapp.R
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import com.example.hackatonapp.databinding.ListItemPatientDataBinding
import com.example.hackatonapp.domain.model.PatientNote
import java.text.SimpleDateFormat
import java.util.*

class PatientDataListViewHolder(
    private val binding: ListItemPatientDataBinding,
    private val onItemClickListener: PatientDataListAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: PatientNote) {
        binding.apply {
            pressureTextView.text = model.pressure
            pulseTextView.text = model.pulse
            dateTextView.text = model.dateCreated
            timeTextView.text = model.timeCreated
        }

        itemView.setOnClickListener {
            onItemClickListener.onItemClick(bindingAdapterPosition)
        }
    }
}