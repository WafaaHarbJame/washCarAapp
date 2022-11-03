package com.washcar.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.washcar.app.R
import com.washcar.app.databinding.FragmentCustomerRegisterBinding
import com.washcar.app.databinding.FragmentServiceProviderBinding

class CustomerRegisterFragment : Fragment() {
    private lateinit var binding: FragmentCustomerRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }
}