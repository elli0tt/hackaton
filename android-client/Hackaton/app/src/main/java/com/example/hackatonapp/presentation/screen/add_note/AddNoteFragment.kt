package com.example.hackatonapp.presentation.screen.add_note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentAddNoteBinding
import com.example.hackatonapp.presentation.extensions.viewBinding

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val binding by viewBinding(FragmentAddNoteBinding::bind)

    private val viewModel: AddNoteViewModel by viewModels()

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

    }
}