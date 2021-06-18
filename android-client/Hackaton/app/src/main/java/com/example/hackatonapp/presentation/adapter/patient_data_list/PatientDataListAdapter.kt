package com.example.hackatonapp.presentation.adapter.patient_data_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.hackatonapp.data.entities.PatientNoteEntity
import com.example.hackatonapp.databinding.ListItemPatientDataBinding

class PatientDataListAdapter :
    ListAdapter<PatientNoteEntity, PatientDataListViewHolder>(DIFF_CALLBACK) {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PatientNoteEntity>() {
            override fun areItemsTheSame(
                oldItem: PatientNoteEntity,
                newItem: PatientNoteEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PatientNoteEntity,
                newItem: PatientNoteEntity
            ): Boolean {
                return oldItem.pressure == newItem.pressure &&
                        oldItem.pulse == newItem.pulse &&
                        oldItem.dateTimeCreated == newItem.dateTimeCreated
            }

        }
    }

    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientDataListViewHolder {
        val binding = ListItemPatientDataBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PatientDataListViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: PatientDataListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}