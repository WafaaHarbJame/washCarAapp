package com.washcar.app.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
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
                binding.swAnnounced.visibility = View.VISIBLE
                binding.swAnnounced.isChecked = memberModel.announced == true
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

            binding.swAnnounced.setOnClickListener {
                var memberModel = list?.get(bindingAdapterPosition)
                memberModel?.announced = memberModel?.announced == false
                setProviderAnnounced(
                    memberModel,
                    memberModel?.announced == true,
                    bindingAdapterPosition
                )
            }

        }

    }

    fun setProviderAnnounced(memberModel: MemberModel?, announced: Boolean, position: Int) {
        GlobalData.progressDialog(activity, R.string.service_provider, R.string.please_wait_sending)
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()
                if (func == Constants.SUCCESS) {
                    GlobalData.Toast(activity, R.string.success_change_provider)
                } else {
                    list?.get(position)?.announced = !announced
                    notifyItemChanged(position)
                    GlobalData.Toast(activity, R.string.fail_to_change_provider)
                }

            }
        }).setProviderAnnounced(memberModel?.email ?: "", announced)
    }

}

