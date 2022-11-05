package com.washcar.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.washcar.app.R
import com.washcar.app.Utils.DateHandler
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.RowRequestBinding
import com.washcar.app.dialogs.AddRateDialog
import com.washcar.app.dialogs.MyConfirmDialog
import com.washcar.app.models.CategoryModel
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RequestModel
import com.washcar.app.models.ReviewModel
import java.text.DateFormat


class RequestsAdapter(private val context: Context, private var list: MutableList<RequestModel?>?) :
    RecyclerView.Adapter<RequestsAdapter.Holder>() {
    var addCommentDialog: AddRateDialog? = null
    var confirmDialog: MyConfirmDialog? = null

    var user: MemberModel? = null

    init {
        user = UtilityApp.userData

    }

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


    inner class Holder(var binding: RowRequestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(requestModel: RequestModel?) {
            binding.carNameTxt.text = requestModel?.carName
            binding.carModelTxt.text = requestModel?.carModel
            binding.carTypeTxt.text = requestModel?.carType
            binding.carPlatNumberTxt.text = requestModel?.carPlateNumber
            binding.carNameTxt.text = requestModel?.carName
            binding.nameTV.text = requestModel?.providerName
            binding.dateTxt.text =
                DateFormat.getDateInstance().format(requestModel?.createdAt ?: "")
            binding.timeTxt.text = DateHandler.GetTimeFromDateString(
                DateHandler.GetDateTimeLong(
                    DateHandler.GetDateString(requestModel?.createdAt)
                )
            )
            binding.totalTv.text =
                requestModel?.total.toString().plus(" " + context.getString(R.string.currency))

            val categoryList: MutableList<CategoryModel?>? = Gson().fromJson(
                requestModel?.selectedService, object : TypeToken<List<CategoryModel?>?>() {}.type
            )

            val selectedServiceAdapter = SelectedServiceAdapter(
                context, categoryList
            )

            binding.rv.adapter = selectedServiceAdapter

            if (requestModel?.status == RequestModel.STATUS_FINISHED) {
                binding.rateBut.visibility = View.VISIBLE
//                binding.lyProviderButtons.visibility = View.GONE
            } else {
                binding.rateBut.visibility = View.GONE
                if (user?.type == MemberModel.TYPE_SERVICE_PROVIDER) {
                    binding.btnFinish.visibility = View.VISIBLE
                } else {
                    binding.btnFinish.visibility = View.GONE
//                    binding.lyProviderButtons.visibility = View.GONE
                }
            }

        }

        init {

            binding.btnFinish.setOnClickListener {
                val requestModel = list?.get(bindingAdapterPosition)
                showConfirmDialog(requestModel?.requestId, bindingAdapterPosition)
            }

            binding.rateBut.setOnClickListener {
//                val user = UtilityApp.userData
                val requestModel = list?.get(bindingAdapterPosition)

                val okClick: AddRateDialog.Click = object : AddRateDialog.Click() {
                    override fun click() {
                        val note = addCommentDialog?.findViewById<EditText>(R.id.rateEt)
                        val ratingBar = addCommentDialog?.findViewById<RatingBar>(R.id.ratingBarBut)
                        val notes = note?.text.toString()
                        val reviewModel = ReviewModel()
                        reviewModel.comment = notes
                        reviewModel.userName = user?.fullName
                        reviewModel.rate = ratingBar?.rating?.toInt()
                        reviewModel.providerId = requestModel?.providerId
                        when {
                            ratingBar?.rating == 0f -> {
                                Toast.makeText(
                                    context, R.string.please_fill_rate, Toast.LENGTH_SHORT
                                ).show()
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

                val cancelClick: AddRateDialog.Click = object : AddRateDialog.Click() {
                    override fun click() {
                        addCommentDialog!!.dismiss()
                    }
                }
                addCommentDialog = AddRateDialog(
                    context,
                    context.getString(R.string.add_comment),
                    R.string.ok,
                    R.string.cancel2,
                    okClick,
                    cancelClick
                )
                addCommentDialog?.show()
            }
        }


    }


    private fun addComment(reviewModel: ReviewModel) {
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()

                if (func == Constants.SUCCESS) {
                    Toast.makeText(
                        context, context.getString(R.string.success_send), Toast.LENGTH_SHORT
                    ).show()
                    addCommentDialog?.dismiss()

                } else {
                    val message = context.getString(R.string.fail_to_send)
                    GlobalData.errorDialog(
                        context, R.string.reviews, message
                    )
                }


            }
        }).sendReviewHandle(reviewModel)

    }


    private fun finishRequest(requestId: String?, position: Int) {
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()

                if (func == Constants.SUCCESS) {
                    Toast.makeText(
                        context, context.getString(R.string.success_send), Toast.LENGTH_SHORT
                    ).show()

                    list?.removeAt(position)
                    notifyItemRemoved(position)

                } else {
                    val message = context.getString(R.string.fail_to_send)
                    GlobalData.errorDialog(
                        context, R.string.finish_request, message
                    )
                }


            }
        }).finishRequestByProvider(requestId ?: "")

    }

    fun showConfirmDialog(requestId: String?, position: Int) {
        if (confirmDialog == null) {
            val okClick = object : MyConfirmDialog.Click() {
                override fun click() {
                    finishRequest(requestId, position)
                }
            }
            confirmDialog = MyConfirmDialog(
                context,
                context.getString(R.string.want_finish_request),
                R.string.ok,
                R.string.cancel2,
                okClick,
                null
            )
            confirmDialog!!.setOnDismissListener {
                confirmDialog = null
            }
        }
    }


}