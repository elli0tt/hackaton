package com.example.hackatonapp.presentation.screen.add_note

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.set
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentAddNoteBinding
import com.example.hackatonapp.presentation.extensions.viewBinding
import com.example.hackatonapp.utils.Resource
import java.util.ArrayList

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val binding by viewBinding(FragmentAddNoteBinding::bind)

    private val viewModel: AddNoteViewModel by viewModels()

    private val args: AddNoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        subscribeToViewModel()

    }

    private fun getFromSharedPreferences() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val sys = sharedPref.getString("SYS", "")
        if (sys != "" && sys != "-1") {
            binding.topPressureEditText.setText(sys)
        }
        val dia = sharedPref.getString("DIA", "")
        if (dia != "" && dia != "-1") {
            binding.bottomPressureEditText.setText(dia)
        }
        val pulse = sharedPref.getString("Pulse", "")
        if (pulse != "" && pulse != "-1") {
            binding.pulseEditText.setText(pulse)
        }
    }

    private fun setListeners() {
        binding.openCameraButton.setOnClickListener {
            findNavController().navigate(R.id.navigateToCameraFragment)
        }
        binding.saveButton.setOnClickListener {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

            viewModel.onSaveClick(
                sys = binding.topPressureEditText.text.toString(),
                dia = binding.bottomPressureEditText.text.toString(),
                pulse = binding.pulseEditText.text.toString(),
                activity = binding.activitySpinner.selectedItem as String,
                comment = binding.commentEditText.text.toString(),
                sharedPref!!.getString("token", "")!!
            )
        }
    }

    private fun subscribeToViewModel() {
        viewModel.patientNoteEntity.observe(viewLifecycleOwner) { patientNoteEntity ->
            binding.apply {
                topPressureEditText.setText(patientNoteEntity.sys.toString())
                bottomPressureEditText.setText(patientNoteEntity.dia.toString())
                patientNoteEntity.pulse?.let { pulse ->
                    pulseEditText.setText(pulse.toString())
                }
                activitySpinner.setSelection(
                    viewModel.findActivityPosition(
                        patientNoteEntity.activity,
                        resources.getStringArray(R.array.patient_activity_entries).toList()
                    )
                )
                commentEditText.setText(patientNoteEntity.comment)
            }
        }

        viewModel.saveResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.loadingView.isVisible = true
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    binding.loadingView.isVisible = false
                    Toast.makeText(
                        context,
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    binding.loadingView.isVisible = false
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.start(args.id)
    }
}