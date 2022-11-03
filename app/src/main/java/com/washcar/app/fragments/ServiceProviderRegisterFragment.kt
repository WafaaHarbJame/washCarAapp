package com.washcar.app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.washcar.app.R
import com.washcar.app.databinding.FragmentServiceProviderBinding

class ServiceProviderRegisterFragment : FragmentBase() {

    private lateinit var binding: FragmentServiceProviderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiceProviderBinding.inflate(inflater, container, false)

        return binding.root
    }
}