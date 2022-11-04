package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.databinding.RowSelectedServiceBinding
import com.washcar.app.databinding.RowServiceTextBinding
import com.washcar.app.models.CategoryModel


class SelectedServiceAdapter(private val context: Context, private var list: MutableList<CategoryModel?>?) :
        RecyclerView.Adapter<SelectedServiceAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowSelectedServiceBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val categoryModel = list?.get(position)
        holder.bind(categoryModel)


    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowSelectedServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(categoryModel: CategoryModel?) {

         // binding.serviceName.text=categoryModel?.name
        }

        init {




        }

    }
}