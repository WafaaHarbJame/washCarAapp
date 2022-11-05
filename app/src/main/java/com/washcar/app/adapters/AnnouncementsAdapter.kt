package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.washcar.app.R
import com.washcar.app.databinding.RowCarAnnouceBinding
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

        holder.binding.tvName.text = memberModel?.fullName
        holder.binding.tvAddress.text = memberModel?.address
        holder.binding.tvDesc.text = memberModel?.description
        holder.binding.tvTime.text = memberModel?.startTime?.plus(" | ${memberModel.endTime}")

        Glide.with(context)
            .asBitmap()
            .load(memberModel?.photoUrl)
            .placeholder(R.drawable.error_logo)
            .into(holder.binding.ivUser)
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowCarAnnouceBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}