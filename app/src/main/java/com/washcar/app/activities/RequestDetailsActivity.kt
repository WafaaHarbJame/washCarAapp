package com.washcar.app.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack
import com.kcode.permissionslib.main.PermissionCompat
import com.washcar.app.R
import com.washcar.app.RootApplication
import com.washcar.app.Utils.ImageHandler
import com.washcar.app.apiHandlers.ApiUrl
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityRequestDetailsBinding
import com.washcar.app.models.MemberModel
import com.washcar.app.models.PayWayImage
import io.nlopez.smartlocation.SmartLocation

class RequestDetailsActivity : ActivityBase(), OnMapReadyCallback {

    var map: GoogleMap? = null
    var fragment: SupportMapFragment? = null
    var zoomLevel = 14f
    var isGrantPermission = false
    private var destinationLat = 0.0
    private var destinationLng = 0.0
    private var lat = 0.0
    private var lng = 0.0
    private var driverId: String? = null
    private var orderID: String? = null

    var driverMarker: Marker? = null
    var user: MemberModel? = null

    lateinit var binding: ActivityRequestDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val bundle = intent.extras;
        if (bundle != null) {
            lat = bundle.getDouble(Constants.KEY_LAT)
            lng = bundle.getDouble(Constants.KEY_LNG)
            destinationLat = bundle.getDouble(Constants.KEY_DESTINATION_LAT)
            destinationLng = bundle.getDouble(Constants.KEY_DESTINATION_LNG)
            driverId = bundle.getString(Constants.KEY_DRIVER_ID)
            orderID = bundle.getString(Constants.KEY_ORDER_ID)

        }


        binding.myLocationBtn.setOnClickListener {
            checkLocationPermission()
        }

        val fm: FragmentManager = supportFragmentManager
        fragment = fm.findFragmentById(R.id.map) as SupportMapFragment
        fragment?.getMapAsync(this)

        user = UtilityApp.userData

        if (user?.type == MemberModel.TYPE_SERVICE_PROVIDER) {
            binding.finishOrder.visibility = View.GONE
            payNow(orderID!!)

        } else if (user?.type == MemberModel.TYPE_CUSTOMER) {
            binding.finishOrder.visibility = View.VISIBLE
        }

        binding.finishOrder.setOnClickListener {
            //   updateOrderStatus(orderID, 3)
        }



        binding.payBtn.setOnClickListener {

        }

    }

    private fun getOrderTracking() {
        if (map != null) {

            createMarker(
                lat,
                lng,
                getString(R.string.my_location),
                "",
                R.drawable.ic_map_marker
            )!!

            createMarker(
                destinationLat,
                destinationLng,
                getString(R.string.destination_location),
                "",
                R.drawable.ic_map_destination_marker
            )!!

            val latLn = LatLng(lat, lng)
            val cameraUpdate =
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        latLn,
                        zoomLevel
                    )
                );
            map?.animateCamera(cameraUpdate)

        }

        setDriverLocation(driverId!!)

    }

    fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String?,
        snippet: String?,
        iconResID: Int
    ): Marker? {
        return map?.addMarker(
            MarkerOptions().position(LatLng(latitude, longitude)).anchor(0.5f, 0.5f).title(title)
                .snippet(snippet).icon(
                    BitmapDescriptorFactory.fromBitmap(
                        ImageHandler.getBitmap(
                            getActiviy(), iconResID
                        )
                    )
                )
        )

    }


    private fun setDriverLocation(driverId: String) {

        RootApplication.fireStoreDB?.collection(ApiUrl.Users.name)?.document(driverId)
            ?.addSnapshotListener { value, error ->

                if (error != null)
                    return@addSnapshotListener

                val currDriverLat: Double = value?.get("lat") as Double
                val currDriverLng: Double = value.get("lng") as Double
                if (driverMarker != null)
                    driverMarker?.remove()
                driverMarker = createMarker(
                    currDriverLat,
                    currDriverLng,
                    getString(R.string.driver_location),
                    "",
                    R.drawable.bus_icon1
                )!!

                val cameraUpdate =
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            LatLng(currDriverLat, currDriverLng),
                            zoomLevel
                        )
                    )
                map?.animateCamera(cameraUpdate)
//                Toast("LatLng $currDriverLat,$currDriverLng")

            }
    }

    private fun payNow(orderNumber: String) {
        println("Log payNow $orderID")
        RootApplication.fireStoreDB?.collection(ApiUrl.Orders.name)?.document(orderNumber)
            ?.addSnapshotListener { value, error ->

                if (error != null)
                    return@addSnapshotListener

                val requestStatus = value!!.getLong("requestStatus")?.toInt()
                println("Log payNow requestStatus $requestStatus")

                if (requestStatus!! == 3) {
                    binding.payBtn.visibility = visible
                } else {
                    binding.payBtn.visibility = gone

                }


            }
    }


    private fun checkLocationPermission() {
        try {
            val builder = PermissionCompat.Builder(getActiviy())
            builder.addPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            builder.addPermissionRationale(getString(R.string.about_app))
            builder.addRequestPermissionsCallBack(object : OnRequestPermissionsCallBack {
                override fun onGrant() {
                    isGrantPermission = true
                    if (map != null)
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
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            SmartLocation.with(getActiviy()).location()
                .oneFix()
                .start { location ->

                    val lat: Double = location.latitude
                    val lng: Double = location.longitude

                    map?.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(lat, lng)
                            )
                            .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    ImageHandler.getBitmap(
                                        getActiviy(), R.drawable.ic_map_marker
                                    )
                                )
                            )
                            .title(getString(R.string.my_location))
                    )

                    val latLn = LatLng(lat, lng)
                    val cameraUpdate =
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(
                                latLn,
                                zoomLevel
                            )
                        );
                    map?.animateCamera(cameraUpdate)

                }

        } else {
            showGPSDisabledAlertToUser()
        }


    }

    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(getActiviy())
        alertDialogBuilder.setMessage(getString(R.string.open_gps))
            .setCancelable(false)
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

//    private fun updateOrderStatus(orderNumber: String?, orderStatus: Int?) {
//        try {
//            DataFeacher(object : DataFetcherCallBack {
//                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                    GlobalData.progressDialogHide()
//                    if (func == Constants.SUCCESS) {
//
//                        AwesomeSuccessDialog(getActiviy())
//                            .setTitle(R.string.change_order_status)
//                            .setMessage(getString(R.string.sucess_change_satus))
//                            .setColoredCircle(R.color.white)
//                            .setDialogIconAndColor(R.drawable.ic_check, R.color.white)
//                            .setCancelable(true)
//                            .show()
//                            .setOnDismissListener {
//                                finish()
//                            }
//
//
//                        DataFeacher(object : DataFetcherCallBack {
//                            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                                GlobalData.progressDialogHide()
//
//                                if (func == Constants.SUCCESS) {
////                                    UtilityApp.userData!!.emptySeat = emptySeat
//
//                                }
//
//                            }
//                        }).updateSeatData(UtilityApp.userData!!.mobileWithCountry, emptySeat);
//
//
//                    } else {
//                        var message = getActiviy()?.getString(R.string.fail_to_change_status)
//                        GlobalData.errorDialog(
//                            getActiviy(),
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

//        if (map != null) {
//
//            map?.addMarker(
//                MarkerOptions()
//                    .position(
//                        LatLng(lat, lng)
//                    )
//                    .icon(
//                        BitmapDescriptorFactory.fromBitmap(
//                            ImageHandler.getBitmap(
//                                getActiviy(), R.drawable.ic_map_marker
//                            )
//                        )
//                    )
//                    .title(getString(R.string.my_location))
//            )
//
//
//            map?.addMarker(
//                MarkerOptions()
//                    .position(
//                        LatLng(destinationLat, destinationLng)
//                    )
//                    .icon(
//                        BitmapDescriptorFactory.fromBitmap(
//                            ImageHandler.getBitmap(
//                                getActiviy(), R.drawable.ic_map_destination_marker
//                            )
//                        )
//                    )
//                    .title(getString(R.string.destination_location))
//            )
//
//            map?.addMarker(
//                MarkerOptions()
//                    .position(
//                        LatLng(driverlat, driverlng)
//                    )
//                    .icon(
//                        BitmapDescriptorFactory.fromBitmap(
//                            ImageHandler.getBitmap(
//                                getActiviy(), R.drawable.ic_map_driver
//                            )
//                        )
//                    )
//                    .title(getString(R.string.driver_location))
//            )
//
//
//            val latLn = LatLng(driverlat, driverlng)
//            val cameraUpdate =
//                CameraUpdateFactory.newCameraPosition(
//                    CameraPosition.fromLatLngZoom(
//                        latLn,
//                        zoomLevel
//                    )
//                );
//            map?.animateCamera(cameraUpdate)
//        }

        getOrderTracking()
    }

}