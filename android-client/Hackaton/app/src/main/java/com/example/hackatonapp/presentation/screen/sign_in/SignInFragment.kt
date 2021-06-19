package com.example.hackatonapp.presentation.screen.sign_in

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
import com.example.hackatonapp.utils.Resource


class SignInFragment : Fragment(R.layout.fragment_init) {

    private val binding by viewBinding(FragmentInitBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val oldToken = sharedPref.getString("token", "none")
        if (oldToken != "none") {
            findNavController().navigate(R.id.action_initFragment_to_patientDataListFragment)
        }
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        subscribeToViewModel()
    }

    private fun initViews() {}

    private fun setListeners() {
        binding.button.setOnClickListener {
            val login = binding.loginSignInTextEdit.text.toString()
            val password = binding.passwordSignInTextEdit.text.toString()
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
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { response ->
                            saveToSharedPreferences(response)
                        }
                        findNavController()
                            .navigate(R.id.action_initFragment_to_patientDataListFragment)
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        )
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
