package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.Utils.DateHandler
import com.washcar.app.databinding.RowItemReviewBinding
import com.washcar.app.models.ReviewModel


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
            val date =
                DateHandler.GetLongToDateString(reviewModel?.createdAt?.time ?: 0L)
            val formattedDate =
                DateHandler.FormatDate4(date, "yyyy-MM-dd HH:mm", "yyyy-MM-dd hh:mm aa")
            binding.reviewDate.text = formattedDate

        }

        init {
        }


    }
}