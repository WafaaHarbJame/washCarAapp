package com.washcar.app.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.github.dhaval2404.form_validation.rule.EmailRule
import com.github.dhaval2404.form_validation.rule.EqualRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack
import com.kcode.permissionslib.main.PermissionCompat
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.activities.ConfirmActivity
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.AESCrypt
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.databinding.FragmentServiceProviderBinding
import com.washcar.app.models.MemberModel
import io.nlopez.smartlocation.SmartLocation
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.confirmPasswordInput
import kotlinx.android.synthetic.main.activity_register.emailInput
import kotlinx.android.synthetic.main.activity_register.fullNameInput
import kotlinx.android.synthetic.main.activity_register.mobileInput
import kotlinx.android.synthetic.main.activity_register.passwordInput
import kotlinx.android.synthetic.main.activity_register.passwordTxt
import kotlinx.android.synthetic.main.fragment_service_provider.*

class RegisterCarWashFragment : FragmentBase() {

    private lateinit var binding: FragmentServiceProviderBinding
    var isGrantPermission = false
    private var selectedLat= 0.0
    private  var selectedLng=0.0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiceProviderBinding.inflate(inflater, container, false)

        initListeners()

        return binding.root
    }

    private fun initListeners() {
        binding.registerBtn.setOnClickListener {

            if (isValidForm()) {
                registerUser()
            }

        }


        binding.myLocationBtn.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun registerUser() {

        try {

            val mobileStr = NumberHandler.arabicToDecimal(binding.mobileTxt.text.toString())
            val passwordStr = NumberHandler.arabicToDecimal(binding.passwordTxt.text.toString())
            val fullNameStr = NumberHandler.arabicToDecimal(binding.NameTxt.text.toString())
            val emailStr = NumberHandler.arabicToDecimal(binding.emailTxt.text.toString())
            val addressStr = NumberHandler.arabicToDecimal(binding.addressTxt.text.toString())
            val descriptionStr = NumberHandler.arabicToDecimal(binding.descTxt.toString())
            val endTimeStr = NumberHandler.arabicToDecimal(binding.endTimeTxt.toString())
            val startTimeStr = NumberHandler.arabicToDecimal(binding.startTxt.toString())


            val registerUserModel = MemberModel()
            registerUserModel.mobile =
                if (mobileStr.startsWith("0")) mobileStr.replaceFirst(
                    "0",
                    ""
                ) else mobileStr

            registerUserModel.password = AESCrypt.encrypt(passwordStr);
            registerUserModel.passwordConfirm = AESCrypt.encrypt(passwordStr)
            registerUserModel.fullName = fullNameStr
            registerUserModel.description = descriptionStr
            registerUserModel.type = Constants.provider_type
            registerUserModel.address = addressStr
            registerUserModel.email = emailStr
            registerUserModel.lat = selectedLat
            registerUserModel.startTime = startTimeStr
            registerUserModel.endTime = endTimeStr


            GlobalData.progressDialog(
               requireContext(),
                R.string.register,
                R.string.please_wait_register
            )

            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    GlobalData.progressDialogHide()

                    if (func == Constants.SUCCESS) {


                    } else {
                        var message = getString(R.string.fail_to_register)
                        if (func == Constants.USER_EXIST)
                            message = getString(R.string.email_exist)

                        GlobalData.errorDialog(
                          requireContext(),
                            R.string.register,
                            message
                        )
                    }


                }
            }).registerHandle(registerUserModel)

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    private fun isValidForm(): Boolean {
        return FormValidator.getInstance()
            .addField(
                binding.fullNameInput,
                NonEmptyRule(R.string.enter_fill_name),
            )

            .addField(
                binding.addressInput,
                NonEmptyRule(R.string.enter_address),

                )

            .addField(
                binding.descInput,
                NonEmptyRule(R.string.enter_desc),

                )
            .addField(
                binding.startTimeInput,
                NonEmptyRule(R.string.enter_start_time),

                )


            .addField(
                binding.endTimeInput,
                NonEmptyRule(R.string.enter_end_time),

                )

            .addField(
                mobileInput,
                NonEmptyRule(R.string.enter_phone_number),
//                    LengthRule(10, R.string.valid_phone_number)

            )

            .addField(
                emailInput,
                NonEmptyRule(R.string.ENTER_EMAIL),
                EmailRule(R.string.enter_vaild_email)

            )
            .addField(
                passwordInput,
                NonEmptyRule(R.string.enter_password)
            )
            .addField(
                confirmPasswordInput,
                NonEmptyRule(R.string.enter_password),
                EqualRule(
                    passwordTxt.text.toString(),
                    R.string.password_confirm_not_match
                )
            )

            .validate()

    }


    private fun checkLocationPermission() {
        try {
            val builder = PermissionCompat.Builder(requireContext())
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
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            SmartLocation.with(requireContext()).location()
                .oneFix()
                .start { location ->

                 selectedLat = location.latitude
                  selectedLng  = location.longitude


                }

        } else {
            showGPSDisabledAlertToUser()
        }


    }

    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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

}