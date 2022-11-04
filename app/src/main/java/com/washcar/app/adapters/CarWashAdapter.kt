package com.washcar.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.activities.LoginActivity
import com.washcar.app.activities.ProfileActivity
import com.washcar.app.activities.RequestCarActivity
import com.washcar.app.databinding.RowCarAnnouceBinding
import com.washcar.app.databinding.RowCarWashBinding
import com.washcar.app.databinding.RowServiceTextBinding
import com.washcar.app.models.CarWashModel
import com.washcar.app.models.CategoryModel


class CarWashAdapter(private val context: Context, private var list: MutableList<CarWashModel?>?) :
    RecyclerView.Adapter<CarWashAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowCarWashBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val carWashModel = list?.get(position)
        holder.bind(carWashModel)

    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowCarWashBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(carWashModel: CarWashModel?) {
            binding.NameTxt.text = carWashModel?.name
            binding.addressTxt.text = carWashModel?.address

        }

        init {
            binding.requestCarWashBut.setOnClickListener {

                val intent = Intent(context, RequestCarActivity::class.java)
                context.startActivity(intent)
            }

            binding.detailsBut.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                context.startActivity(intent)
            }

        }

    }


}