package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.washcar.app.R
import com.washcar.app.databinding.FragmentRequestsBinding

/**
 * A simple [Fragment] subclass.
 */
class CategoriesFragment : FragmentBase() {

    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.mainTitleTxt.text = getString(R.string.categories)
        binding.toolBar.homeBtn.visibility = gone

    }


}