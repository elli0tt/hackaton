package com.example.hackatonapp.presentation.screen.camera

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentCameraBinding
import com.example.hackatonapp.presentation.extensions.viewBinding

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val binding by viewBinding(FragmentCameraBinding::bind)

    private val viewModel: CameraViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}