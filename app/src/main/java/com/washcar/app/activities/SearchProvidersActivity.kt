package com.washcar.app.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack
import com.kcode.permissionslib.main.PermissionCompat
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.adapters.CarWashAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivitySearchProviderBinding
import com.washcar.app.models.CategoryModel
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RequestModel
import io.nlopez.smartlocation.SmartLocation


class SearchProvidersActivity : ActivityBase() {
    var providerCategoriesList: MutableList<CategoryModel?>? = null
    var providersList: MutableList<MemberModel?>? = null

    private var selectedLat = 0.0
    private var selectedLng = 0.0
    private var user: MemberModel? = null

    lateinit var binding: ActivitySearchProviderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        user = UtilityApp.userData

        binding.rv.layoutManager = LinearLayoutManager(getActiviy())
        binding.rv.setHasFixedSize(true)

        initListeners()


    }

    private fun initListeners() {
//        binding.myLocationBtn.setOnClickListener {
//            checkLocationPermission()
//        }
//        binding.requestBtn.setOnClickListener {
//            sendRequest()
//        }
    }

    private fun initAdapter(list: MutableList<MemberModel?>?) {
        val adapter = CarWashAdapter(getActiviy(), list)
        binding.rv.adapter = adapter
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

//    private fun getProviderCategories(providerEmail: String?) {
//
//        DataFeacher(object : DataFetcherCallBack {
//            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//
//                if (func == Constants.SUCCESS) {
//
//                    providerCategoriesList = obj as MutableList<CategoryModel?>?
//                    if (providerCategoriesList?.isNotEmpty() == true) {
//
//
//                    }
//
//                }
//
//
//            }
//        }).getProviderCategories(providerEmail ?: "")
//    }

    private fun getData(loading: Boolean) {
        if (loading) {
            binding.lyLoading.loadingProgressLY.visibility = visible
            binding.lyFail.failGetDataLY.visibility = gone
            binding.swipeDataContainer.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                binding.lyLoading.loadingProgressLY.visibility = gone

                if (func == Constants.SUCCESS) {

                    binding.swipeDataContainer.visibility = visible
                    val list = obj as MutableList<MemberModel>?
                    providersList = list?.filter {
                        it.type == MemberModel.TYPE_SERVICE_PROVIDER
                    }?.toMutableList()

                    if (providersList?.isNotEmpty() == true) {
                        filterListToAdapters()
                    }
                } else {
                    binding.lyFail.failGetDataLY.visibility = visible
                    binding.swipeDataContainer.visibility = gone
                }


            }
        }).getUsers()
    }

    fun filterListToAdapters() {
        val filteredList = providersList?.filter {
            it?.announced == true
        }?.toMutableList()
        initAdapter(filteredList)

    }


}