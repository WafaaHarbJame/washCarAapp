package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.databinding.RowServiceTextBinding
import com.washcar.app.models.CategoryModel


class ServiceTextAdapter(private val context: Context, private var list: MutableList<CategoryModel?>?) :
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

          binding.serviceName.text=categoryModel?.name
        }

        init {


           binding.serviceName.setOnCheckedChangeListener { _, isChecked ->
              binding.serviceName.isSelected = isChecked
            }


        }

    }
}