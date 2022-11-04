package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.washcar.app.R
import com.washcar.app.adapters.MainAdminAdapter
import com.washcar.app.databinding.FragmentAdminMainScreenBinding
import com.washcar.app.models.MainAdminModel


class AdminMainScreenFragment : FragmentBase() {

    var mainList: MutableList<MainAdminModel>? = null

    private var _binding: FragmentAdminMainScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminMainScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.mainTitleTxt.text = getString(R.string.home)
        binding.toolBar.homeBtn.visibility = gone
        mainList = ArrayList<MainAdminModel>()


        binding.rv.layoutManager = LinearLayoutManager(requireActivity())
        binding.rv.hasFixedSize()
        getMainContent()
    }


    private fun initAdapter() {

        val adapter = MainAdminAdapter(requireActivity(), mainList)
        binding.rv.adapter = adapter

    }

    private fun getMainContent() {
        mainList?.add(
            MainAdminModel(
                MainAdminModel.MANAGE_DRIVERS,
                getString(R.string.manage_drivers)
            )
        )

        initAdapter()


    }

}