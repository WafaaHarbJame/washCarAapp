package com.washcar.app.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack
import com.kcode.permissionslib.main.PermissionCompat
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.adapters.ServiceTextAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityRequestCarDetailsBinding
import com.washcar.app.models.CategoryModel
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RequestModel
import io.nlopez.smartlocation.SmartLocation


class RequestCarActivity : ActivityBase() {
    var providerCategoriesList: MutableList<CategoryModel?>? = null
    lateinit var binding: ActivityRequestCarDetailsBinding
    var serviceList: MutableList<CategoryModel?>? = null
    private var selectedLat = 0.0
    private var selectedLng = 0.0
    private var total: Double? = 0.0
    private var selectedService: String? = null
    private var carWashModel: MemberModel? = null
    private var user: MemberModel? = null
    var selectedServiceList: MutableList<CategoryModel?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestCarDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = UtilityApp.userData


        val lyManger = LinearLayoutManager(getActiviy(), RecyclerView.VERTICAL, false)
        binding.serviceRecycler.layoutManager = lyManger
        binding.serviceRecycler.setHasFixedSize(true)

        binding.serviceRecycler.itemAnimator = null
        binding.toolBar.mainTitleTxt.text = getString(R.string.send_oder)

        selectedServiceList = mutableListOf()

        val bundle = intent.extras
        if (bundle != null) {
            carWashModel = bundle.getSerializable(Constants.key_provider_data) as MemberModel?

        }
        providerCategoriesList = mutableListOf()


        initListeners()

        getProviderCategories(carWashModel?.email)

    }

    private fun initListeners() {
        binding.myLocationBtn.setOnClickListener {
            checkLocationPermission()
        }
        binding.requestBtn.setOnClickListener {
            sendRequest()
        }
    }

    private fun initAdapter() {
        val adapter = ServiceTextAdapter(getActiviy(), providerCategoriesList)
        binding.serviceRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun checkLocationPermission() {
        try {
            val builder = PermissionCompat.Builder(this)
            builder.addPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            builder.addPermissionRationale(getString(R.string.about_app))
            builder.addRequestPermissionsCallBack(object : OnRequestPermissionsCallBack {
                override fun onGrant() {
                    getMyLocation()
                }

                override fun onDenied(permission: String) {
                    Toast(R.string.some_permission_denied)
                }
            })
            builder.build().request()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMyLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            GlobalData.progressDialog(this, R.string.my_location, R.string.please_wait_get_location)
            SmartLocation.with(this).location().oneFix().start { location ->
                GlobalData.progressDialogHide()
                selectedLat = location.latitude
                selectedLng = location.longitude


            }

        } else {
            showGPSDisabledAlertToUser()
        }

    }

    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(getString(R.string.open_gps)).setCancelable(false)
            .setPositiveButton(
                getString(R.string.enable)
            ) { dialog, id ->
                dialog.cancel()
                val callGPSSettingIntent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                startActivity(callGPSSettingIntent)
                dialog.cancel()
            }
        alertDialogBuilder.setNegativeButton(
            getString(R.string.cancel2)
        ) { dialog, id -> dialog.cancel() }
        val alert: AlertDialog = alertDialogBuilder.create()
        alert.show()
    }

    private fun getProviderCategories(providerEmail: String?) {
        binding.loadingLY.visibility = visible
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                binding.loadingLY.visibility = gone

                if (func == Constants.SUCCESS) {

                    providerCategoriesList = obj as MutableList<CategoryModel?>?
                    if (providerCategoriesList?.isNotEmpty() == true) {

                        initAdapter()

                    }

                }


            }
        }).getProviderCategories(providerEmail ?: "")
    }


    private fun sendRequest() {

        try {
            val carNameStr = NumberHandler.arabicToDecimal(binding.carNameEt.text.toString())
            val carTypeStr = NumberHandler.arabicToDecimal(binding.carTxt.text.toString())
            val carModelStr = NumberHandler.arabicToDecimal(binding.carModelTxt.text.toString())
            val plateNumberStr =
                NumberHandler.arabicToDecimal(binding.plateNumberTxt.text.toString())
            val addressStr = NumberHandler.arabicToDecimal(binding.etAddress.text.toString())

            var hasError = false
            if (carNameStr.isEmpty()) {
                binding.carNameInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (carTypeStr.isEmpty()) {
                binding.carTypeInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (carModelStr.isEmpty()) {
                binding.carModelInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (plateNumberStr.isEmpty()) {
                binding.plateNumberInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (addressStr.isEmpty()) {
                binding.addressInput.error = getString(R.string.please_select_your_location)
                hasError = true
            }

            if (selectedLat == 0.0 && selectedLng == 0.0) {
                Toast(R.string.please_select_your_location)
                hasError = true
            }


            if (selectedService?.isEmpty() == true) {
                Toast(R.string.select_service)
                hasError = true
            }

            if (hasError) return

            selectedServiceList = providerCategoriesList?.filter {
                it?.userSelected == true
            }?.toMutableList()


            for (selectService in selectedServiceList ?: mutableListOf()) {
                total = total?.plus(selectService?.price ?: 0.0)

            }
            selectedService = Gson().toJson(selectedServiceList)

            val requestModel = RequestModel()

            requestModel.fullName = carWashModel?.fullName
            requestModel.providerId = carWashModel?.email
            requestModel.customerId = user?.email
            requestModel.customerName = user?.fullName
            requestModel.customerLat = selectedLat
            requestModel.customerLng = selectedLng
            requestModel.carName = carNameStr
            requestModel.carModel = carModelStr
            requestModel.carType = carTypeStr
            requestModel.carPlateNumber = plateNumberStr
            requestModel.providerId = carWashModel?.email
            requestModel.providerName = carWashModel?.fullName
            requestModel.selectedService = selectedService
            requestModel.status = RequestModel.STATUS_UPCOMING
            requestModel.total = total

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
            }).sendOrderHandle(requestModel)


        } catch (e: Exception) {

            e.printStackTrace()

        }
    }


}