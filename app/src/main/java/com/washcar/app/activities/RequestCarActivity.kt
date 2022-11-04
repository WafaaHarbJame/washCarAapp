package com.washcar.app.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.adapters.ServiceTextAdapter
import com.washcar.app.databinding.ActivityRequestCarDetailsBinding
import com.washcar.app.models.ServiceModel

class RequestCarActivity : ActivityBase() {
    lateinit var binding: ActivityRequestCarDetailsBinding
    var serviceList: MutableList<ServiceModel?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestCarDetailsBinding.inflate(layoutInflater)
       setContentView(binding.root)

        serviceList= mutableListOf()

        serviceList?.add(ServiceModel(1,"Washing"))
        serviceList?.add(ServiceModel(2, "Washing Steam"))
        serviceList?.add(ServiceModel(3, "Shine"))
        serviceList?.add(ServiceModel(4, "Detailing"))
        serviceList?.add(ServiceModel(5, "Detailing Steam"))
        serviceList?.add(ServiceModel(6, "Car disInfection"))
        serviceList?.add(ServiceModel(6, "Exterior Wash"))

        val lyManger = LinearLayoutManager(getActiviy(), RecyclerView.VERTICAL, false)
        binding.serviceRecycler.layoutManager = lyManger
        binding.serviceRecycler.setHasFixedSize(true)
        binding.serviceRecycler.itemAnimator = null

        initAdapter()




    }

    private fun initAdapter() {
        val adapter = ServiceTextAdapter(getActiviy(),serviceList)
        binding.serviceRecycler.adapter = adapter
    }
}