package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.databinding.RowServiceTextBinding
import com.washcar.app.models.CategoryModel


class ServiceTextAdapter(
    private val context: Context,
    private var list: MutableList<CategoryModel?>?,
    val showPrice: Boolean = true
) :
    RecyclerView.Adapter<ServiceTextAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowServiceTextBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val categoryModel = list?.get(position)
        holder.bind(categoryModel)


    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowServiceTextBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(categoryModel: CategoryModel?) {

            binding.serviceName.text = categoryModel?.name
            if (showPrice) {
                binding.servicePrice.text = categoryModel?.price.toString()
                binding.servicePrice.visibility = View.VISIBLE
            } else {
                binding.servicePrice.visibility = View.GONE
            }
        }

        init {


            binding.serviceName.setOnCheckedChangeListener { _, isChecked ->
                binding.serviceName.isSelected = isChecked
                val categoryModel: CategoryModel = list?.get(bindingAdapterPosition)!!
                categoryModel.userSelected = true

            }


        }

    }
}