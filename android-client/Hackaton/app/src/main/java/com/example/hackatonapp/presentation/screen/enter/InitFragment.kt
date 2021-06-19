package com.example.hackatonapp.presentation.screen.enter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentInitBinding
import com.example.hackatonapp.presentation.extensions.viewBinding
import com.example.hackatonapp.presentation.screen.add_note.AddNoteViewModel

class InitFragment : Fragment(R.layout.fragment_init) {

    private val binding by viewBinding(FragmentInitBinding::bind)

    private val viewModel: AddNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        subscribeToViewModel()
    }

    private fun initViews() {}

    private fun setListeners() {
        binding.tvSendToRegistrationTitle.setOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_registrationFragment)
        }
    }

    private fun subscribeToViewModel() {}
}
