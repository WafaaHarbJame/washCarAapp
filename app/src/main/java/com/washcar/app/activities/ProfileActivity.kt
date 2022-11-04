package com.washcar.app.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.washcar.app.R
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityProfileBinding
import com.washcar.app.models.MemberModel
import java.util.*

class ProfileActivity : ActivityBase() {
    var activity: Activity? = null
    var user: MemberModel? = null
    var yearStr: Int? = 0
    var monthStr: Int? = 0
    var dayStr: Int? = 0
    private var ageNumber: Int? = 0
    private var changeAge: Boolean = false
    var email: String? = ""

    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.profile)
        activity = getActiviy()


        homeBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        user = UtilityApp.userData

        getData()

        if (user?.type == MemberModel.TYPE_CUSTOMER) {
            binding.busLayout.visibility = View.GONE

        } else if (user?.type == MemberModel.TYPE_SERVICE_PROVIDER) {
            binding.busLayout.visibility = View.VISIBLE

        }


        binding.ageEt.setOnClickListener {
            changeAge = true;
            val dpd = DatePickerDialog(this, { view2, thisYear, thisMonth, thisDay ->
                monthStr = thisMonth + 1
                dayStr = thisDay
                yearStr = thisYear
                binding.ageEt.setText(" " + monthStr + "/" + dayStr + "/" + yearStr)
                ageNumber = com.washcar.app.Utils.DateHandler.getAge(
                    yearStr.toString().toInt(),
                    monthStr.toString().toInt(),
                    dayStr.toString().toInt()
                ).toInt()

                val newDate: Calendar = Calendar.getInstance()
                newDate.set(thisYear, thisMonth, thisDay)
            }, yearStr!!, monthStr!!, dayStr!!)
            dpd.show()


        }

        binding.updateBtn.setOnClickListener {
//            if (userType == 1) {
//                updateProfile()
//            } else {
//                updateDriverProfile()
//            }

        }


    }


    private fun getData() {
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                if (func == Constants.SUCCESS) {
                    val user = obj as MemberModel
                    binding.nameEt.text = Editable.Factory.getInstance().newEditable(user.fullName)
                    binding.addressTxt.text =
                        Editable.Factory.getInstance().newEditable(user.address)
                    binding.emailTxt.text =
                        Editable.Factory.getInstance().newEditable(user.email.toString())
                }


            }
        }).getMyAccount(email ?: "")
    }

//    private fun updateProfile() {
//        val address = addressTxt.text.toString()
//        val name = nameEt.text.toString()
//        var age: Int
//        val email = emailTxt.text.toString()
//
//        if (changeAge) {
//            age = ageNumber!!
//        } else {
//            age = ageEt.text.toString().toInt()
//
//        }
//
//        try {
//
//            DataFeacher(object : DataFetcherCallBack {
//                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                    GlobalData.progressDialogHide()
//
//                    if (func == Constants.SUCCESS) {
//                        GlobalData.successDialog(
//                            getActiviy(),
//                            R.string.update_profile,
//                            getString(R.string.success_edit), null
//                        )
//
//                    } else {
//                        GlobalData.errorDialog(
//                            getActiviy(),
//                            R.string.update_profile,
//                            getString(R.string.fail_to_edit)
//                        )
//                    }
//
//
//                }
//            }).updateUserData(UtilityApp.userData!!.mobileWithCountry, name, address, age, email)
//
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//        }
//    }

//    private fun updateDriverProfile() {
//        val address = addressTxt.text.toString()
//        val name = nameEt.text.toString()
//        var age: Int
//        val busColor = busColoTxt.text.toString()
//        val busModel = busModeTxt.text.toString()
//        val busNumber = busNumbTxt.text.toString().toInt()
//        val busCapacity = busCapacityTv.text.toString().toInt()
//        val email = emailTxt.text.toString()
//
//        if (changeAge) {
//            age = ageNumber!!
//        } else {
//            age = ageEt.text.toString().toInt()
//
//        }
//
//        try {
//
//            DataFeacher(object : DataFetcherCallBack {
//                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                    GlobalData.progressDialogHide()
//
//                    if (func == Constants.SUCCESS) {
//                        GlobalData.successDialog(
//                            getActiviy(),
//                            R.string.update_profile,
//                            getString(R.string.success_edit), null
//                        )
//
//                    } else {
//                        GlobalData.errorDialog(
//                            getActiviy(),
//                            R.string.update_profile,
//                            getString(R.string.fail_to_edit)
//                        )
//                    }
//
//
//                }
//            }).updateDriverData(
//                UtilityApp.userData!!.mobileWithCountry,
//                name,
//                address,
//                age,
//                busNumber,
//                busColor,
//                busModel,
//                busCapacity,
//                email
//            )
//
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//        }
//    }


}

