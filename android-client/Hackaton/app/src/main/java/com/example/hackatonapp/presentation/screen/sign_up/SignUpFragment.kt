package com.example.hackatonapp.presentation.screen.sign_up

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackatonapp.R
import com.example.hackatonapp.data.entities.User
import com.example.hackatonapp.databinding.FragmentRegistrationBinding
import com.example.hackatonapp.presentation.extensions.viewBinding
import com.example.hackatonapp.utils.Resource

class SignUpFragment : Fragment(R.layout.fragment_registration) {

    private val binding by viewBinding(FragmentRegistrationBinding::bind)

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        subscribeToViewModel()
    }

    private fun setListeners() {
        binding.buttonStartRegistration.setOnClickListener {
            val login = binding.loginSignUpTextEdit.text.toString()
            val password = binding.passwordSignUpTextEdit.text.toString()
            val snils = binding.snilsSignUpTextEdit.text.toString()
            val user = User(login, password, "pat", snils)
            if (password.isNotEmpty() && login.isNotEmpty() && snils.isNotEmpty()) {
                if (password.length >= 6) {
                    changeLoadingVisibility(isShown = true)
                    viewModel.getUserToken(user)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.password_length_error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.all_fields_should_be_filled_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.userToken.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    changeLoadingVisibility(isShown = false)
                    response.data?.let { token ->
                        saveToSharedPreferences(token)
                    }
                    findNavController()
                        .navigate(R.id.action_registrationFragment_to_patientDataListFragment)
                }
                is Resource.Error -> {
                    changeLoadingVisibility(isShown = false)
                    Toast.makeText(
                        context,
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    changeLoadingVisibility(isShown = true)
                }
            }
        }
    }

    private fun changeLoadingVisibility(isShown: Boolean) {
        binding.progressBar.isVisible = isShown
        binding.loadingView.isVisible = isShown
    }

    private fun saveToSharedPreferences(token: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("token", token)
            apply()
        }
    }
}
