package com.example.hackatonapp.presentation.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.hackatonapp.presentation.base.FragmentViewBindingDelegate

fun <Binding : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> Binding) =
    FragmentViewBindingDelegate(this, viewBindingFactory)