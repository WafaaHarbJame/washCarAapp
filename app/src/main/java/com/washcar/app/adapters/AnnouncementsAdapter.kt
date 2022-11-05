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
import com.washcar.app.databinding.RowCarAnnouceBinding
import com.washcar.app.databinding.RowCarWashBinding
import com.washcar.app.models.MemberModel


class AnnouncementsAdapter(
    private val context: Context,
    private var list: MutableList<MemberModel?>?
) :
    RecyclerView.Adapter<AnnouncementsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowCarAnnouceBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memberModel = list?.get(position)

       holder.bind(memberModel)
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowCarAnnouceBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(memberModel: MemberModel?) {
            binding.tvName.text = memberModel?.fullName
            binding.tvAddress.text = memberModel?.address
            binding.tvDesc.text = memberModel?.description
            binding.tvTime.text = memberModel?.startTime?.plus(" | ${memberModel.endTime}")

            Glide.with(context)
                .asBitmap()
                .load(memberModel?.photoUrl)
                .placeholder(R.drawable.error_logo)
                .into(binding.ivUser)

        }

        init {
            binding.rowLY.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                val carWashModel = list?.get(bindingAdapterPosition)
                intent.putExtra(Constants.KEY_TYPE, MemberModel.TYPE_SERVICE_PROVIDER)
                intent.putExtra(Constants.key_provider_data, carWashModel)
                intent.putExtra(Constants.key_show_profile, true)
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