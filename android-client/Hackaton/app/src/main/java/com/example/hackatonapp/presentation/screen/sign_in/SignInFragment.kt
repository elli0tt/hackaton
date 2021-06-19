package com.example.hackatonapp.presentation.screen.sign_in

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

class SignInFragment : Fragment(R.layout.fragment_init) {

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
        binding.button.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            val user = User(login, password)
            if (password.isNotEmpty() && login.isNotEmpty()) {
                if (password.length > 6) {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.getUserToken(user)
                } else {
                    Toast.makeText(
                        context,
                        "Пароль должен содержать более 6 символов",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }else {
            Toast.makeText(
                context,
                "Все поля должны быть заполнены",
                Toast.LENGTH_SHORT
            )
                .show()
        }
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getUserToken(user)
        }
        binding.tvSendToRegistrationTitle.setOnClickListener {
            findNavController()
                .navigate(R.id.action_initFragment_to_registrationFragment)
        }
    }

    private fun subscribeToViewModel() {
        viewModel.userToken.observe(
            viewLifecycleOwner,
            Observer { response ->
                binding.progressBar.visibility = View.INVISIBLE
                findNavController()
                    .navigate(R.id.action_initFragment_to_patientDataListFragment)
            }
        )
    }
}
