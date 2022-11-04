package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.databinding.RowServiceTextBinding
import com.washcar.app.models.ServiceModel


class ServiceTextAdapter(private val context: Context, private var list: MutableList<ServiceModel?>?) :
        RecyclerView.Adapter<ServiceTextAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowServiceTextBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val serviceModel = list?.get(position)
        holder.binding.serviceName.text=serviceModel?.name

        holder.binding.serviceName.setOnCheckedChangeListener { _, isChecked -> //set your object's last status
            holder.binding.serviceName.isSelected = isChecked
        }


    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder(var binding: RowServiceTextBinding) : RecyclerView.ViewHolder(
        binding.root)




    init {


    }
}