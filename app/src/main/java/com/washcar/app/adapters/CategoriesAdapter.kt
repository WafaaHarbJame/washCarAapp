package com.washcar.app.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.databinding.RowCategoryBinding
import com.washcar.app.models.CategoryModel

class CategoriesAdapter(
    private val activity: Activity?,
    var list: MutableList<CategoryModel>?,
    var dataFetcherCallBack: DataFetcherCallBack?
) :
    RecyclerView.Adapter<CategoriesAdapter.MyHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolder {
        val binding =
            RowCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), null, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val categoryModel = list?.get(position)

        holder.binding.tvName.text = categoryModel?.name

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class MyHolder(val binding: RowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            itemView.setOnClickListener {

                val categoryModel = list?.get(bindingAdapterPosition)

                dataFetcherCallBack?.Result(categoryModel, "", true)
            }
        }
    }

}

