package com.washcar.app.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.adapters.ServiceTextAdapter
import com.washcar.app.databinding.ActivityRequestCarDetailsBinding
import com.washcar.app.models.CategoryModel

class RequestCarActivity : ActivityBase() {
    lateinit var binding: ActivityRequestCarDetailsBinding
    var serviceList: MutableList<CategoryModel?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestCarDetailsBinding.inflate(layoutInflater)
        binding.toolBar.mainTitleTxt.text=getString(R.string.send_oder)
       setContentView(binding.root)

        serviceList= mutableListOf()

        serviceList?.add(CategoryModel("1","Washing"))
        serviceList?.add(CategoryModel("2", "Washing Steam"))
        serviceList?.add(CategoryModel("3", "Shine"))
        serviceList?.add(CategoryModel("4", "Detailing"))
        serviceList?.add(CategoryModel("5", "Detailing Steam"))
        serviceList?.add(CategoryModel("6", "Car disInfection"))
        serviceList?.add(CategoryModel("7", "Exterior Wash"))

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