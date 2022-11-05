package com.washcar.app.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack
import com.kcode.permissionslib.main.PermissionCompat
import com.washcar.app.MainActivity
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.AESCrypt
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityRegisterTypeBinding
import com.washcar.app.models.MemberModel
import io.nlopez.smartlocation.SmartLocation

class RegisterActivity : ActivityBase() {

    private var userType: String = ""

    private var selectedLat = 0.0
    private var selectedLng = 0.0

    private lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityRegisterTypeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.mainTitleTxt.text = getString(R.string.register)

        auth = FirebaseAuth.getInstance()

        binding.btnCustomer.setOnClickListener {
            userType = MemberModel.TYPE_CUSTOMER
            selectType(userType == MemberModel.TYPE_CUSTOMER)
        }

        binding.btnProvider.setOnClickListener {
            userType = MemberModel.TYPE_SERVICE_PROVIDER
            selectType(userType == MemberModel.TYPE_CUSTOMER)
        }

        binding.myLocationBtn.setOnClickListener {
            checkLocationPermission()
        }


        binding.registerBtn.setOnClickListener {
            registerUser()
        }

        binding.btnCustomer.performClick()

    }


    fun selectType(isCustomer: Boolean) {

        binding.tvCustomer.setTextColor(
            ContextCompat.getColor(
                this, if (isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcCustomer.setBackgroundColor(
            ContextCompat.getColor(
                this, if (isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.tvProvider.setTextColor(
            ContextCompat.getColor(
                this, if (!isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcProvider.setBackgroundColor(
            ContextCompat.getColor(
                this, if (!isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )

        if (isCustomer) {
            binding.descInput.visibility = gone
            binding.lyAddress.visibility = gone
            binding.startTimeInput.visibility = gone
            binding.endTimeInput.visibility = gone
        } else {
            binding.descInput.visibility = visible
            binding.lyAddress.visibility = visible
            binding.startTimeInput.visibility = visible
            binding.endTimeInput.visibility = visible
        }

    }

//    fun createAuthUser(email: String, password: String) {
//    }

    private fun registerUser() {

        try {

            val fullNameStr = NumberHandler.arabicToDecimal(binding.etName.text.toString())
            val emailStr = NumberHandler.arabicToDecimal(binding.etEmail.text.toString())
            val mobileStr = NumberHandler.arabicToDecimal(binding.etMobile.text.toString())
            val passwordStr = NumberHandler.arabicToDecimal(binding.etPassword.text.toString())
            val confirmPasswordStr =
                NumberHandler.arabicToDecimal(binding.etConfirmPassword.text.toString())
            val addressStr = NumberHandler.arabicToDecimal(binding.etAddress.text.toString())
            val descriptionStr = NumberHandler.arabicToDecimal(binding.etDesc.text.toString())
            val startTimeStr = NumberHandler.arabicToDecimal(binding.etStartTime.text.toString())
            val endTimeStr = NumberHandler.arabicToDecimal(binding.etEndTime.text.toString())

            var hasError = false
            if (fullNameStr.isEmpty()) {
                binding.fullNameInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (emailStr.isEmpty()) {
                binding.emailInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (mobileStr.isEmpty()) {
                binding.mobileInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (passwordStr.isEmpty()) {
                binding.passwordInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (confirmPasswordStr.isEmpty()) {
                binding.confirmPasswordInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (passwordStr != confirmPasswordStr) {
                binding.confirmPasswordInput.error = getString(R.string.invalid_confirm_password)
                hasError = true
            }
            if (userType == MemberModel.TYPE_SERVICE_PROVIDER) {
                if (descriptionStr.isEmpty()) {
                    binding.descInput.error = getString(R.string.invalid_input)
                    hasError = true
                }
                if (addressStr.isEmpty()) {
                    binding.addressInput.error = getString(R.string.invalid_input)
                    hasError = true
                }
                if (startTimeStr.isEmpty()) {
                    binding.startTimeInput.error = getString(R.string.invalid_input)
                    hasError = true
                }
                if (endTimeStr.isEmpty()) {
                    binding.endTimeInput.error = getString(R.string.invalid_input)
                    hasError = true
                }
                if (selectedLat == 0.0 && selectedLng == 0.0) {
                    Toast(R.string.please_select_your_location)
                    hasError = true
                }
            }

            if (hasError) return

            val registerUserModel = MemberModel()

            registerUserModel.fullName = fullNameStr
            registerUserModel.email = emailStr
            registerUserModel.mobile = mobileStr
            registerUserModel.password = AESCrypt.encrypt(passwordStr);
//            registerUserModel.passwordConfirm = AESCrypt.encrypt(passwordStr)
            registerUserModel.type = userType
            if (userType == MemberModel.TYPE_SERVICE_PROVIDER) {
                registerUserModel.address = addressStr
                registerUserModel.description = descriptionStr
                registerUserModel.lat = selectedLat
                registerUserModel.lng = selectedLng
                registerUserModel.startTime = startTimeStr
                registerUserModel.endTime = endTimeStr
            }

            GlobalData.progressDialog(
                this, R.string.register, R.string.please_wait_register
            )
            auth.createUserWithEmailAndPassword(
                registerUserModel.email ?: "",
                passwordStr ?: ""
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val authUser = auth.currentUser
                        registerUserModel.token = authUser?.uid
                        sendUserToFirebase(registerUserModel)
                    } else {
                        GlobalData.progressDialogHide()
                        // If sign in fails, display a message to the user.
                        val message = task.exception?.message
                        GlobalData.errorDialog(
                            this@RegisterActivity, R.string.register, message
                        )
                        task.exception?.printStackTrace()
//                        Log.w(
//                            javaClass.simpleName,
//                            "Log createUserWithEmail:failure",
//                            task.exception
//                        )
//                        Toast("Auth Failed")
                    }
                }

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun sendUserToFirebase(registerUserModel: MemberModel) {

        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()

                if (func == Constants.SUCCESS) {

                    UtilityApp.userData = registerUserModel

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    var message = getString(R.string.fail_to_register)
                    if (func == Constants.USER_EXIST) message = getString(R.string.email_exist)

                    GlobalData.errorDialog(
                        this@RegisterActivity, R.string.register, message
                    )
                }


            }
        }).registerHandle(registerUserModel)
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


}

