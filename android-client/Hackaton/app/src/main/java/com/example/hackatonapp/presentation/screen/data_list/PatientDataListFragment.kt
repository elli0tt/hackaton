package com.example.hackatonapp.presentation.screen.data_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentPatientDataListBinding
import com.example.hackatonapp.presentation.adapter.patient_data_list.PatientDataListAdapter
import com.example.hackatonapp.presentation.extensions.viewBinding

class PatientDataListFragment : Fragment(R.layout.fragment_patient_data_list) {

    private val binding by viewBinding(FragmentPatientDataListBinding::bind)

    private val viewModel: PatientDataListViewModel by viewModels()

    private val recyclerAdapter = PatientDataListAdapter().apply {
        onItemClickListener = PatientDataListAdapter.OnItemClickListener { position ->
            viewModel.onListItemClick(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        subscribeToViewModel()
    }

    private fun initViews() {
        binding.recyclerView.apply {
            adapter = recyclerAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL))
        }
    }

    private fun setListeners() {

    }

    private fun subscribeToViewModel() {
        viewModel.apply {
            list.observe(viewLifecycleOwner) {
                recyclerAdapter.submitList(it)
            }
        }
    }
}