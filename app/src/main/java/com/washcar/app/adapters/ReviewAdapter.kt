package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.washcar.app.R
import com.washcar.app.Utils.DateHandler
import com.washcar.app.classes.UtilityApp

import com.washcar.app.databinding.RowItemReviewBinding
import com.washcar.app.databinding.RowRequestBinding
import com.washcar.app.dialogs.AddRateDialog
import com.washcar.app.models.CategoryModel
import com.washcar.app.models.RequestModel
import com.washcar.app.models.ReviewModel
import java.text.DateFormat


class ReviewAdapter(private val context: Context, private var list: MutableList<ReviewModel?>?) :
        RecyclerView.Adapter<ReviewAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowItemReviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val reviewModel = list?.get(position)
        holder.bind(reviewModel)

    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder(var binding: RowItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewModel: ReviewModel?) {

          binding.reviewTv.text = reviewModel?.comment
            binding.tvUserName.text = reviewModel?.userName
            binding.ratingBar.rating = reviewModel?.rate?.toFloat() ?: 0f
            binding.reviewDate.text= DateFormat.getDateInstance().format(reviewModel?.createdAt?:"",)

        }

        init {
        }


    }
}