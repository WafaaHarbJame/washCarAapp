package com.washcar.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.activities.ProfileActivity
import com.washcar.app.activities.RequestCarActivity
import com.washcar.app.classes.Constants
import com.washcar.app.databinding.RowCarWashHorizontalBinding
import com.washcar.app.models.MemberModel


class HorizontalCarWashAdapter(
    private val context: Context,
    private var list: MutableList<MemberModel?>?
) :
    RecyclerView.Adapter<HorizontalCarWashAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView =
            RowCarWashHorizontalBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memberModel = list?.get(position)
        holder.bind(memberModel)

    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowCarWashHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(carWashModel: MemberModel?) {
            binding.tvName.text = carWashModel?.fullName
            binding.tvAddress.text = carWashModel?.address
            binding.tvRate.text = carWashModel?.rate.toString()
            binding.ratingBar.rating = carWashModel?.rate ?: 0f

        }

        init {

            binding.requestCarWashBut.setOnClickListener {
                val carWashModel = list?.get(bindingAdapterPosition)
                val intent = Intent(context, RequestCarActivity::class.java)
                intent.putExtra(Constants.key_provider_data, carWashModel)
                context.startActivity(intent)
            }

            binding.detailsBut.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                context.startActivity(intent)
            }

        }

    }


}