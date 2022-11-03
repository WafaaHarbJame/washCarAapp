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
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.models.MemberModel
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity : ActivityBase() {
    var activity: Activity? = null
    var user: MemberModel? = null
    private var userType: Int = 0;
    var yearStr: Int? = 0
    var monthStr: Int? = 0
    var dayStr: Int? = 0
    private var ageNumber: Int? = 0
    private var changeAge: Boolean = false
    var email: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        title = getString(R.string.profile)
        activity = getActiviy()


        homeBtn.setOnClickListener {
            onBackPressed()
        }

        user = UtilityApp.userData
        userType = user!!.type

        getData()

        if (userType == 1) {
            busLayout.visibility = View.GONE

        } else if (userType == 2) {
            busLayout.visibility = View.VISIBLE

        }


        ageEt.setOnClickListener {
            changeAge = true;
            val dpd = DatePickerDialog(this, { view2, thisYear, thisMonth, thisDay ->
                monthStr = thisMonth + 1
                dayStr = thisDay
                yearStr = thisYear
                ageEt.setText(" " + monthStr + "/" + dayStr + "/" + yearStr)
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

        updateBtn.setOnClickListener {
            if (userType == 1) {
                updateProfile()
            } else {
                updateDriverProfile()
            }

        }


    }


    private fun getData() {
        val mobile = UtilityApp.userData?.mobileWithCountry
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                if (func == Constants.SUCCESS) {
                    val user = obj as MemberModel
                    nameEt.text = Editable.Factory.getInstance().newEditable(user.fullName)
                    ageEt.text = Editable.Factory.getInstance().newEditable(user.age.toString())
                    addressTxt.text = Editable.Factory.getInstance().newEditable(user.address)
                    busModeTxt.text = Editable.Factory.getInstance().newEditable(user.busModel)
                    busColoTxt.text = Editable.Factory.getInstance().newEditable(user.busColor)
                    busCapacityTv.text =
                        Editable.Factory.getInstance().newEditable(user.busLoading.toString())
                    busNumbTxt.text =
                        Editable.Factory.getInstance().newEditable(user.busNumber.toString())
                    emailTxt.text =
                        Editable.Factory.getInstance().newEditable(user.email.toString())
                }


            }
        }).getMyAccount(mobile!!)
    }

    private fun updateProfile() {
        val address = addressTxt.text.toString()
        val name = nameEt.text.toString()
        var age: Int
        val email = emailTxt.text.toString()

        if (changeAge) {
            age = ageNumber!!
        } else {
            age = ageEt.text.toString().toInt()

        }

        try {

            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    GlobalData.progressDialogHide()

                    if (func == Constants.SUCCESS) {
                        GlobalData.successDialog(
                            getActiviy(),
                            R.string.update_profile,
                            getString(R.string.success_edit), null
                        )

                    } else {
                        GlobalData.errorDialog(
                            getActiviy(),
                            R.string.update_profile,
                            getString(R.string.fail_to_edit)
                        )
                    }


                }
            }).updateUserData(UtilityApp.userData!!.mobileWithCountry, name, address, age, email)

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun updateDriverProfile() {
        val address = addressTxt.text.toString()
        val name = nameEt.text.toString()
        var age: Int
        val busColor = busColoTxt.text.toString()
        val busModel = busModeTxt.text.toString()
        val busNumber = busNumbTxt.text.toString().toInt()
        val busCapacity = busCapacityTv.text.toString().toInt()
        val email = emailTxt.text.toString()

        if (changeAge) {
            age = ageNumber!!
        } else {
            age = ageEt.text.toString().toInt()

        }

        try {

            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    GlobalData.progressDialogHide()

                    if (func == Constants.SUCCESS) {
                        GlobalData.successDialog(
                            getActiviy(),
                            R.string.update_profile,
                            getString(R.string.success_edit), null
                        )

                    } else {
                        GlobalData.errorDialog(
                            getActiviy(),
                            R.string.update_profile,
                            getString(R.string.fail_to_edit)
                        )
                    }


                }
            }).updateDriverData(
                UtilityApp.userData!!.mobileWithCountry,
                name,
                address,
                age,
                busNumber,
                busColor,
                busModel,
                busCapacity,
                email
            )

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }


}

