package com.example.hackatonapp.presentation.screen.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.hackatonapp.R
import com.example.hackatonapp.data.entities.User
import com.example.hackatonapp.databinding.FragmentInitBinding
import com.example.hackatonapp.presentation.extensions.viewBinding
import com.example.hackatonapp.presentation.screen.sign_in.SignInViewModel
import com.example.hackatonapp.utils.Resource


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentInitBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        subscribeToViewModel()
    }

    private fun initViews() {}

    private fun setListeners() {

    }

    private fun subscribeToViewModel() {

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


    private fun saveToSharedPreferences(token: String){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("token", token)
            apply()
        }
    }
}
