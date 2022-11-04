package com.washcar.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.adapters.RequestsAdapter
import com.washcar.app.databinding.FragmentAllRequestBinding
import com.washcar.app.models.CategoryModel
import com.washcar.app.models.RequestModel

class OrdersListFragment : Fragment() {

    private var orderList: MutableList<RequestModel?>? = null

    private lateinit var binding: FragmentAllRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllRequestBinding.inflate(inflater, container, false)

        orderList= mutableListOf()

        val lm = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rv.layoutManager = lm
        binding.rv.setHasFixedSize(true)




        orderList?.add(RequestModel())
        orderList?.add(RequestModel())
        orderList?.add(RequestModel())
        orderList?.add(RequestModel())
        orderList?.add(RequestModel())
        orderList?.add(RequestModel())
        orderList?.add(RequestModel())


        initAdapter()
        return binding.root

    }



    private fun initAdapter() {

        val adapter = RequestsAdapter(requireActivity(), orderList)
        binding.rv.adapter = adapter
    }
}