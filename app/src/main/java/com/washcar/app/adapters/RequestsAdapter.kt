package com.washcar.app.adapters

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog
import com.washcar.app.R
import com.washcar.app.Utils.MapHandler
import com.washcar.app.activities.RequestDetailsActivity
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RequestModel

class RequestsAdapter(
    private val activity: Activity?,
    var list: MutableList<RequestModel>?
) :
    RecyclerView.Adapter<RequestsAdapter.MyHolder>() {

    var user: MemberModel? = null

    init {
        user = UtilityApp.userData
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_request, null, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        if (list != null) {
            holder.bind(list!![position])
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class MyHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {

        private val nameTV: TextView = itemView!!.findViewById(R.id.nameTV)
        private val dateTxt: TextView = itemView!!.findViewById(R.id.dateTxt)
        private val addressTxt: TextView = itemView!!.findViewById(R.id.addressTxt)
        private val acceptBut: TextView = itemView!!.findViewById(R.id.acceptBut)
        private val rejectBut: TextView = itemView!!.findViewById(R.id.rejectBut)
        private val orderStatusBtn: TextView = itemView!!.findViewById(R.id.orderStatusBtn)

        fun bind(requestModel: RequestModel) {

            nameTV.text = requestModel.getClient_name()
            dateTxt.text = requestModel.getRequestDate()
            addressTxt.text = MapHandler.getGpsAddress(
                activity,
                requestModel.destinationLat,
                requestModel.destinationLng
            )

            // 1 accept 2 reject 3 finish


            when (requestModel.requestStatus) {
                0 -> {
                    orderStatusBtn.visibility = View.VISIBLE
                    orderStatusBtn.text = activity?.getString(R.string.pending)
                    orderStatusBtn.background =
                      ContextCompat.getDrawable(activity!!, R.drawable.circle_corne_order_pending)

                    if (user?.type == 2) {
                        acceptBut.visibility = View.VISIBLE
                        rejectBut.visibility = View.VISIBLE
                        orderStatusBtn.visibility = View.GONE

                    } else {
                        acceptBut.visibility = View.GONE
                        rejectBut.visibility = View.GONE
                    }

                }
                1 -> {
                    orderStatusBtn.visibility = View.VISIBLE
                    acceptBut.visibility = View.GONE
                    rejectBut.visibility = View.GONE
                    orderStatusBtn.text = activity?.getString(R.string.accepted)
                    orderStatusBtn.background = ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.circle_corne_order_accepted
                    )
                }
                2 -> {
                    orderStatusBtn.visibility = View.VISIBLE
                    acceptBut.visibility = View.GONE
                    rejectBut.visibility = View.GONE
                    orderStatusBtn.text = activity?.getString(R.string.rejecttion)
                    orderStatusBtn.background = ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.circle_corne_order_rejected
                    )

                }
                3 -> {
                    orderStatusBtn.text = activity?.getString(R.string.finish)
                    orderStatusBtn.visibility = View.VISIBLE
                    acceptBut.visibility = View.GONE
                    rejectBut.visibility = View.GONE
                    orderStatusBtn.background = ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.circle_corne_order_finished
                    )
                }
                else -> {
                    orderStatusBtn.visibility = View.GONE
                }
            }

            acceptBut.setOnClickListener {
                Log.i(TAG, "Log requestModel.getOrderId()" + requestModel.getOrderId())

             //   updateOrderStatus(requestModel.getOrderId(), 1, adapterPosition)

            }
            rejectBut.setOnClickListener {
                Log.i(TAG, "Log requestModel.getOrderId()" + requestModel.getOrderId())

               // updateOrderStatus(requestModel.getOrderId(), 2, adapterPosition)

            }


        }

        init {

            itemView?.setOnClickListener {

                val requestModel = list!![adapterPosition]

                if (requestModel.requestStatus != 1 && UtilityApp.userData?.type == 1)
                    return@setOnClickListener
                val intent = Intent(activity, RequestDetailsActivity::class.java)
                intent.putExtra(Constants.KEY_DESTINATION_LAT, requestModel.getDestinationLat())
                intent.putExtra(Constants.KEY_DESTINATION_LNG, requestModel.getDestinationLng())
                intent.putExtra(Constants.KEY_LAT, requestModel.getLat())
                intent.putExtra(Constants.KEY_LNG, requestModel.getLng())
                intent.putExtra(Constants.KEY_DRIVER_ID, requestModel.getDriver_id())
                intent.putExtra(Constants.KEY_ORDER_ID, requestModel.getOrderId())
                activity?.startActivity(intent)

            }
        }
    }

//    private fun updateOrderStatus(orderNumber: String?, orderStatus: Int?, position: Int) {
//        try {
//            DataFeacher(object : DataFetcherCallBack {
//                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                    GlobalData.progressDialogHide()
//                    if (func == Constants.SUCCESS) {
////                        val emptySeatBefore = UtilityApp.userData!!.emptySeat
////                        val emptySeat: Int = emptySeatBefore - 1
//                        list!![position].requestStatus = orderStatus!!
//
//                        AwesomeSuccessDialog(activity)
//                            .setTitle(R.string.change_order_status)
//                            .setMessage(activity?.getString(R.string.sucess_change_satus))
//                            .setColoredCircle(R.color.white)
//                            .setDialogIconAndColor(R.drawable.ic_check, R.color.white)
//                            .setCancelable(true)
//                            .show()
//
////                    .setButtonText(getString(R.string.ok))
////            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
//
//                        notifyItemChanged(position)
//                        notifyDataSetChanged()
////                        list!!.removeAt(position)
//
//                        DataFeacher(object : DataFetcherCallBack {
//                            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                                GlobalData.progressDialogHide()
//
//                                if (func == Constants.SUCCESS) {
//                                    UtilityApp.userData!!.emptySeat = emptySeat
//
//                                }
//
//                            }
//                        }).updateSeatData(UtilityApp.userData!!.mobileWithCountry, emptySeat);
//
//
//                    } else {
//                        var message = activity?.getString(R.string.fail_to_change_status)
//                        GlobalData.errorDialog(
//                            activity,
//                            R.string.change_order_status,
//                            message
//                        )
//                    }
//
//                }
//            }).updateOrder(orderNumber, orderStatus);
//
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//
//        }
//    }

}

