package com.example.hackatonapp.presentation.screen.add_note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentAddNoteBinding
import com.example.hackatonapp.presentation.extensions.viewBinding

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val binding by viewBinding(FragmentAddNoteBinding::bind)

    private val viewModel: AddNoteViewModel by viewModels()

    private val args: AddNoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        subscribeToViewModel()
    }

    private fun initViews() {

    }

    private fun setListeners() {
        binding.openCameraButton.setOnClickListener {
            findNavController().navigate(R.id.navigateToCameraFragment)
        }
        binding.saveButton.setOnClickListener {
            viewModel.onSaveClick(
                sys = binding.topPressureEditText.text.toString(),
                dia = binding.bottomPressureEditText.text.toString(),
                pulse = binding.pulseEditText.text.toString(),
                activity = binding.activitySpinner.selectedItem as String,
                comment = binding.commentEditText.text.toString()
            )
            findNavController().popBackStack()
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

        viewModel.start(args.id)
    }
}