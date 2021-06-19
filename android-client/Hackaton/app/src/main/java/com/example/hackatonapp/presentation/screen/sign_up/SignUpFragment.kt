package com.example.hackatonapp.presentation.screen.sign_up

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.hackatonapp.R
import com.example.hackatonapp.data.entities.User
import com.example.hackatonapp.databinding.FragmentRegistrationBinding
import com.example.hackatonapp.presentation.extensions.viewBinding

class SignUpFragment : Fragment(R.layout.fragment_registration) {

    private val binding by viewBinding(FragmentRegistrationBinding::bind)

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        subscribeToViewModel()
    }

    private fun initViews() {}

    private fun setListeners() {
        binding.buttonStartRegistration.setOnClickListener {
            val login = binding.etLoginRegistration.text.toString()
            val password = binding.etPasswordRegistration.text.toString()
            val snils = binding.etSnilsRegistration.text.toString()
            val user = User(login, password, "pat", snils)
            if (password.isNotEmpty() && login.isNotEmpty() && snils.isNotEmpty()) {
                if (password.length > 6) {
                    binding.progressBarRegistration.visibility = View.VISIBLE
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
        }
    }

    private fun subscribeToViewModel() {
        viewModel.userToken.observe(
            viewLifecycleOwner,
            Observer { response ->
                binding.etSnilsRegistration.visibility = View.INVISIBLE
                findNavController()
                    .navigate(R.id.action_registrationFragment_to_patientDataListFragment)
            }
        )
    }
}
