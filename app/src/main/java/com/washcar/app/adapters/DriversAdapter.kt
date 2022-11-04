package com.washcar.app.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.dialogs.MyConfirmDialog
import com.washcar.app.models.DriverModel

class DriversAdapter(
    private val activity: Activity,
    var list: MutableList<DriverModel?>?,
    val dataFetcherCallBack: com.washcar.app.apiHandlers.DataFetcherCallBack?
) :
    RecyclerView.Adapter<DriversAdapter.MyHolder>() {

    var confirmDialog: MyConfirmDialog? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_driver, null, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

//        val countryCodeModel: CountryModel = list[position]

        if (list != null) {
            holder.bind(list?.get(position)!!)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class MyHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {

        private val nameTV: TextView = itemView!!.findViewById(R.id.nameTV)
        private val ageTV: TextView = itemView!!.findViewById(R.id.ageTV)
        private val mobileTV: TextView = itemView!!.findViewById(R.id.mobileTV)
        private val busNumTV: TextView = itemView!!.findViewById(R.id.busNumTV)
        private val busModelTV: TextView = itemView!!.findViewById(R.id.busModelTV)
        private val editDriverBtn: TextView = itemView!!.findViewById(R.id.editDriverBtn)
        private val deleteDriverBtn: TextView = itemView!!.findViewById(R.id.deleteDriverBtn)

        fun bind(driverModel: DriverModel) {

            nameTV.text = driverModel.fullName
            ageTV.text = driverModel.age.toString().plus(" " + activity.getString(R.string.years))
            mobileTV.text = driverModel.mobileWithCountry
            busNumTV.text = driverModel.busNumber.toString()
            busModelTV.text = driverModel.busModel

        }

        init {

            editDriverBtn.setOnClickListener {

                var driverModel: DriverModel?
                list?.let {

                    driverModel = list!![adapterPosition]

//                    val intent = Intent(activity, AddDriverActivity::class.java)
//                    intent.putExtra(Constants.KEY_DRIVER_MODEL, driverModel)
//                    activity.startActivity(intent)

                }


            }

            deleteDriverBtn.setOnClickListener {

                var driverModel: DriverModel?
                list?.let {


                    if (confirmDialog == null) {
                        val okClick = object : MyConfirmDialog.Click() {
                            override fun click() {

                                driverModel = list!![adapterPosition]

                                deleteDriver(driverModel!!)
                            }
                        }
                        confirmDialog = MyConfirmDialog(
                            activity,
                            activity.getString(R.string.want_delete_driver),
                            R.string.delete,
                            R.string.cancel2,
                            okClick,
                            null
                        )
                    }

                }


            }

        }
    }

    private fun deleteDriver(driverModel: DriverModel) {

        GlobalData.progressDialog(
            activity,
            R.string.delete_driver,
            R.string.please_wait_sending
        )

        val dataFeacher = DataFeacher(object : com.washcar.app.apiHandlers.DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()

                if (func == Constants.SUCCESS) {

                    GlobalData.successDialog(activity,
                        R.string.delete_driver,
                        activity.getString(R.string.success_delete_driver),
                        object : com.washcar.app.apiHandlers.DataFetcherCallBack {
                            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                                dataFetcherCallBack?.Result("", "", true)
                            }
                        })

                } else {

                    GlobalData.errorDialog(
                        activity,
                        R.string.delete_driver,
                        activity.getString(R.string.fail_to_send)
                    )
                }


            }
        })

        dataFeacher.deleteDriver(driverModel)


    }

}

