package com.washcar.app.activities

import android.content.Intent
import android.os.Bundle
import com.google.firebase.firestore.FieldValue
import com.washcar.app.R
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.models.MemberModel
import kotlinx.android.synthetic.main.activity_complete_order.*

class CompleteOrderActivity : ActivityBase() {
    private var destinationLat = 0.0
    private var destinationLng = 0.0
    private var lat = 0.0
    private var lng = 0.0
    private var driverlat = 0.0
    private var driverlng = 0.0
    private var driverId: String? = null
//    private var toOrder: Boolean = true

    var user: MemberModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_order)
        title = getString(R.string.complete_order)

        user = UtilityApp.userData

        homeBtn.setOnClickListener {
           onBackPressedDispatcher.onBackPressed()
        }

        val bundle = intent.extras;
        if (bundle != null) {
            lat = bundle.getDouble(Constants.KEY_LAT)
            lng = bundle.getDouble(Constants.KEY_LNG)
            destinationLat = bundle.getDouble(Constants.KEY_DESTINATION_LAT)
            destinationLng = bundle.getDouble(Constants.KEY_DESTINATION_LNG)
            driverId = bundle.getString(Constants.KEY_DRIVER_ID)

//            Log.i("TAG", "Log CompleteOrderActivity destinationLat  $destinationLat")
//            Log.i("TAG", "Log CompleteOrderActivity destinationLng  $destinationLng")

            destinationTv.text =
                com.washcar.app.Utils.MapHandler.getGpsAddress(getActiviy(), destinationLat, destinationLng)
            locationTv.text = com.washcar.app.Utils.MapHandler.getGpsAddress(
                getActiviy(),
                lat,
                lng
            )

        }
//        getData(driverId!!)


        confirmBtn.setOnClickListener {
            makeOrder()

        }
    }

//    private fun getData(driverId: String) {
//        Log.i("TAG", "Log driverId $driverId")
//
//        DataFeacher(object : DataFetcherCallBack {
//            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//
//                if (func == Constants.SUCCESS) {
//                    val user = obj as MemberModel?
//                    Log.i("TAG", "Log getData ${user!!.mobileWithCountry}")
//                    driverlat = user.lat
//                    driverlng = user.lng
//
//                }
//
//            }
//        }).getMyAccount(driverId)
//    }

    private fun makeOrder() {

        try {

            val requestMap = mutableMapOf<String, Any?>().apply {

                this["orderId"] = ""
                this["clientId"] = user?.email
                this["address"] = user?.address
                this["client_name"] = user?.fullName
                this["destinationLat"] = destinationLat
                this["destinationLng"] = destinationLng
                this["lat"] = lat
                this["lng"] = lng
                this["driver_id"] = driverId
                this["requestDate"] = com.washcar.app.Utils.DateHandler.GetDateOnlyNowString()
                this["requestStatus"] = 0
                this["createdAt"] = FieldValue.serverTimestamp()
            }

            GlobalData.progressDialog(
                getActiviy(),
                R.string.make_order,
                R.string.please_wait_to_make_order
            )

            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    GlobalData.progressDialogHide()

                    if (func == Constants.SUCCESS) {
                        Toast(R.string.make_order_sucess)
                        val intent = Intent(getActiviy(), com.washcar.app.MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        val message = getString(R.string.fail_to_order)
                        GlobalData.errorDialog(
                            getActiviy(),
                            R.string.make_order,
                            message
                        )
                    }


                }
            }).orderHandler(requestMap)

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }


}