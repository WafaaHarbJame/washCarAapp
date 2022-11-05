package com.washcar.app.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.databinding.RowProviderCategoryBinding
import com.washcar.app.models.CategoryModel

class ProviderCategoriesAdapter(
    private val activity: Activity,
    var list: MutableList<CategoryModel?>?,
    var dataFetcherCallBack: DataFetcherCallBack?
) :
    RecyclerView.Adapter<ProviderCategoriesAdapter.MyHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolder {
        val binding =
            RowProviderCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), null, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val categoryModel = list?.get(position)

        holder.binding.tvCount.text = position.plus(1).toString()
        holder.binding.tvName.text = categoryModel?.name
        holder.binding.tvPrice.text = NumberHandler.formatDouble(categoryModel?.price ?: 0.0)

        if (categoryModel?.selected == true) {
            holder.binding.ivCheck.setImageDrawable(
                ContextCompat.getDrawable(
                    activity,
                    R.drawable.ic_check_box_fill
                )
            )
        } else {
            holder.binding.ivCheck.setImageDrawable(
                ContextCompat.getDrawable(
                    activity,
                    R.drawable.ic_check_box_empty
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class MyHolder(val binding: RowProviderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            itemView.setOnClickListener {

                val categoryModel = list?.get(bindingAdapterPosition)

                dataFetcherCallBack?.Result(categoryModel, "", true)
            }
        }
    }

}

