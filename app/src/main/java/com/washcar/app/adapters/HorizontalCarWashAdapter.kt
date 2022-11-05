package com.washcar.app.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.washcar.app.R
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


        fun bind(memberModel: MemberModel?) {
            binding.tvName.text = memberModel?.fullName
            binding.tvAddress.text = memberModel?.address
            binding.tvRate.text = memberModel?.rate.toString()
            binding.ratingBar.rating = memberModel?.rate ?: 0f

            Glide.with(context)
                .asBitmap()
                .load(memberModel?.photoUrl)
                .placeholder(R.drawable.error_logo)
                .into(binding.ivUser)


        }

        init {

            binding.requestCarWashBut.setOnClickListener {
                val carWashModel = list?.get(bindingAdapterPosition)
                val intent = Intent(context, RequestCarActivity::class.java)
                intent.putExtra(Constants.key_provider_data, carWashModel)
                intent.putExtra(Constants.KEY_EMAIL, carWashModel?.email)
                Log.i(javaClass.simpleName, "Log carWashModel adapter email ${carWashModel?.email}")
                context.startActivity(intent)
            }

            binding.detailsBut.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                val carWashModel = list?.get(bindingAdapterPosition)
                intent.putExtra(Constants.KEY_TYPE, MemberModel.TYPE_SERVICE_PROVIDER)
                intent.putExtra(Constants.key_provider_data, carWashModel)
                intent.putExtra(Constants.key_show_profile, true)
                context.startActivity(intent)
            }

        }

    }


}