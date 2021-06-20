package com.example.hackatonapp.presentation.screen.sign_in

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
import com.example.hackatonapp.databinding.FragmentInitBinding
import com.example.hackatonapp.presentation.extensions.viewBinding
import com.example.hackatonapp.utils.Resource


class SignInFragment : Fragment(R.layout.fragment_init) {

    private val binding by viewBinding(FragmentInitBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        subscribeToViewModel()
    }

    private fun setListeners() {
        binding.button.setOnClickListener {
            val login = binding.loginSignInTextEdit.text.toString()
            val password = binding.passwordSignInTextEdit.text.toString()
            val user = User(login, password)
            if (password.isNotEmpty() && login.isNotEmpty()) {
                if (password.length > 6) {
                    changeLoadingVisibility(isShown = true)
                    viewModel.getUserToken(user)
                } else {
                    Toast.makeText(
                        context,
                        "Пароль должен содержать более 6 символов",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Все поля должны быть заполнены",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.tvSendToRegistrationTitle.setOnClickListener {
            findNavController()
                .navigate(R.id.action_initFragment_to_registrationFragment)
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
                        .navigate(R.id.action_initFragment_to_patientDataListFragment)
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