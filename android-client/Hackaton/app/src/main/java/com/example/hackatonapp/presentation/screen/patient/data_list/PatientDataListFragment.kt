package com.example.hackatonapp.presentation.screen.patient.data_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentPatientDataListBinding
import com.example.hackatonapp.presentation.extensions.viewBinding

class PatientDataListFragment : Fragment(R.layout.fragment_patient_data_list) {

    private val viewBinding by viewBinding(FragmentPatientDataListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

    }
}