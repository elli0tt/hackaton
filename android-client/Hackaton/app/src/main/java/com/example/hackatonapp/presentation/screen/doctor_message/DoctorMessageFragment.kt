package com.example.hackatonapp.presentation.screen.doctor_message

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentDoctorMessageBinding
import com.example.hackatonapp.presentation.adapter.doctor_messages.DoctorMessagesAdapter
import com.example.hackatonapp.presentation.extensions.viewBinding
import com.example.hackatonapp.utils.Resource

class DoctorMessageFragment : Fragment(R.layout.fragment_doctor_message) {

    private val binding by viewBinding(FragmentDoctorMessageBinding::bind)

    private val viewModel: DoctorMessageViewModel by viewModels()

    private val recyclerAdapter = DoctorMessagesAdapter()

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
        }
    }

    private fun setListeners() {

    }

    private fun subscribeToViewModel() {
        viewModel.messages.observe(viewLifecycleOwner) {
            when (it) {
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
                    recyclerAdapter.submitList(it.data)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        viewModel.update(sharedPref.getString("token", "")!!)
    }
}
