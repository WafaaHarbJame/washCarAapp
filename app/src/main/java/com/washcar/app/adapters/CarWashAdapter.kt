package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.databinding.RowCarAnnouceBinding
import com.washcar.app.databinding.RowCarWashBinding
import com.washcar.app.models.CarWashModel


class CarWashAdapter(private val context: Context, private var list: MutableList<CarWashModel?>?) :
        RecyclerView.Adapter<CarWashAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowCarWashBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val carWashModel = list?.get(position)
//        holder.binding.descTxt.text = carWashModel?.describtion
//        holder.binding.dateTxt.text = carWashModel?.date
//        holder.binding.addressTxt.text = carWashModel?.address
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowCarWashBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}