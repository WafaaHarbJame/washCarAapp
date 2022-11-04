package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.databinding.RowRequestBinding
import com.washcar.app.dialogs.AddRateDialog
import com.washcar.app.models.CategoryModel
import com.washcar.app.models.RequestModel
import com.washcar.app.models.ReviewModel

class RequestsAdapter(private val context: Context, private var list: MutableList<RequestModel?>?) :
    RecyclerView.Adapter<RequestsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = RowRequestBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val requestModel = list?.get(position)

        val lln: RecyclerView.LayoutManager = LinearLayoutManager(context)
        holder.binding.rv.layoutManager = lln

        holder.bind(requestModel)

    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(var binding: RowRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(requestModel: RequestModel?) {
            var categoryList: MutableList<CategoryModel?>? = null


            categoryList = mutableListOf()

            categoryList.add(CategoryModel("1", "Washing"))
            categoryList.add(CategoryModel("2", "Washing Steam"))
            categoryList.add(CategoryModel("3", "Shine"))

            val selectedServiceAdapter = SelectedServiceAdapter(
                context, categoryList
            )

//            val selectedServiceAdapter = SelectedServiceAdapter(
//                context, requestModel?.categoryModels
            binding.rv.adapter = selectedServiceAdapter

        }

        init {
            binding.rateBut.setOnClickListener {
                val reviewModel = ReviewModel()
                var addCommentDialog: AddRateDialog? = null
                val note = addCommentDialog?.findViewById<EditText>(R.id.rateEt)
                val ratingBar =
                    addCommentDialog?.findViewById<RatingBar>(R.id.ratingBar)
                val notes = note?.text.toString()
                reviewModel.comment = notes
                reviewModel.rate = ratingBar?.rating?.toInt()
                when {
                    ratingBar?.rating == 0f -> {
                        Toast.makeText(context, R.string.please_fill_rate, Toast.LENGTH_SHORT)
                            .show()
                        ratingBar.requestFocus()
                    }
                    note?.text.toString().isEmpty() -> {
                        note?.requestFocus()
                        note?.error = context.getString(R.string.please_fill_comment)
                    }
                    else -> {
                        addComment(reviewModel)
                    }
                }
            }


        }

    }

    private fun addComment(reviewModel: ReviewModel) {

    }


}