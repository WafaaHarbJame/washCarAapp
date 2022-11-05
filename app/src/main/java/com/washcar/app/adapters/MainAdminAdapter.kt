package com.washcar.app.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.databinding.RowAdminUserBinding
import com.washcar.app.models.MemberModel

class MainAdminAdapter(
    private val activity: Activity,
    var list: MutableList<MemberModel>?,
    val dataFetcherCallBack: DataFetcherCallBack
) :
    RecyclerView.Adapter<MainAdminAdapter.MyHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolder {
        val binding =
            RowAdminUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        if (list != null) {
            holder.bind(list!![position])
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class MyHolder(val binding: RowAdminUserBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(memberModel: MemberModel) {

            binding.tvName.text = memberModel.fullName
            binding.tvEmail.text = memberModel.email
            if (memberModel.type == MemberModel.TYPE_SERVICE_PROVIDER) {
                binding.tvWorkTime.visibility = View.VISIBLE
                binding.tvWorkTime.text = memberModel.startTime?.plus(" - ${memberModel.endTime}")
            } else {
                binding.tvWorkTime.visibility = View.GONE
            }


//            binding.tvType.text =
//                activity.getString(if (memberModel.type == MemberModel.TYPE_SERVICE_PROVIDER) R.string.service_provider else R.string.customer)

//            var userType = ""
//            val backgroundResId: Drawable?
//            when (memberModel.type) {
//                MemberModel.TYPE_ADMIN -> {
//                    userType = activity.getString(R.string.admin)
//                    backgroundResId =
//                        ContextCompat.getDrawable(activity, R.drawable.round_corner_yellow_fill)
//                }
//                MemberModel.TYPE_SERVICE_PROVIDER -> {
//                    userType = activity.getString(R.string.service_provider)
//                    backgroundResId =
//                        ContextCompat.getDrawable(activity, R.drawable.round_corner_primary_fill)
//                }
//                else -> {
//                    userType = activity.getString(R.string.customer)
//                    backgroundResId =
//                        ContextCompat.getDrawable(activity, R.drawable.round_corner_green_fill)
//                }
//            }
//            binding.tvType.text = userType
//            binding.tvType.background = backgroundResId
        }

        init {

            itemView.setOnClickListener {

                list?.let {

                    var memberModel = list?.get(bindingAdapterPosition)


                }


            }
        }
    }

}

