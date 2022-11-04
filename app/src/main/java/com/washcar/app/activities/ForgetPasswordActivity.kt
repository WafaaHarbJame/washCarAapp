package com.washcar.app.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import com.github.dhaval2404.form_validation.rule.LengthRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityForgetPasswordBinding
import com.washcar.app.models.MemberModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ForgetPasswordActivity : ActivityBase() {


    val TAG: String? = "Log"

    var selectedCountryCode = 966

    lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_forget_password)
        title = ""


        homeBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        binding.resetPasswordBtn.setOnClickListener {

            if (isValidForm())
                forgetPassword()
        }

        initLocalCountryCode()


    }

    private fun initLocalCountryCode() {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountryIso = tm.networkCountryIso
        val simCountryIso = tm.simCountryIso
        var isoCode = "SR"
        if (networkCountryIso != null && !networkCountryIso.isEmpty()) {
            isoCode = networkCountryIso
        } else if (simCountryIso != null && !simCountryIso.isEmpty()) {
            isoCode = simCountryIso
        }
//        val phoneUtils = PhoneNumberUtil.createInstance(getActiviy())

        //   selectedCountryCode = phoneUtils.getCountryCodeForRegion(isoCode.toUpperCase())
    }

    private fun forgetPassword() {

        try {

            val mobileStr = NumberHandler.arabicToDecimal(binding.mobileTxt.text.toString())

            val mobile =
                if (mobileStr.startsWith("0")) mobileStr.replaceFirst(
                    "0",
                    ""
                ) else mobileStr


            val phoneNumber = selectedCountryCode.toString().plus(mobile)

            GlobalData.progressDialog(
                getActiviy(),
                R.string.reset_password,
                R.string.please_wait_sending
            )

            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    GlobalData.progressDialogHide()

                    if (func == Constants.SUCCESS) {
                        Log.d(TAG, "phoneNumber:${phoneNumber}")

                        val intent = Intent(getActiviy(), ResetPasswordActivity::class.java)
                        intent.putExtra(Constants.KEY_MOBILE, phoneNumber)
                        startActivity(intent)
                    } else {
                        GlobalData.errorDialog(
                            getActiviy(),
                            R.string.reset_password,
                            getString(R.string.not_have_account_q)
                        )
                    }


                }
            }).forgetPassword(phoneNumber)

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(responseEvent: com.washcar.app.models.ResponseEvent) {

        if (responseEvent.api == "registerHandle") {
//            GlobalData.progressDialog(
//                getActiviy(),
//                R.string.register,
//                R.string.please_wait_register,
//                false
//            )
            if (responseEvent.type == Constants.ERROR_DATA) {
                val result = responseEvent.data as com.washcar.app.models.ResultAPIModel<Any>?
                var message = getString(R.string.fail_to_register)
                if (result?.error != null) {
                    val errors = result.error.details
                    for (error in errors) {
                        message += ("\n---\n $error")
                    }
                }

                GlobalData.errorDialog(getActiviy(), R.string.register, message)

            } else if (responseEvent.type == Constants.FAIL_DATA) {
                Toast(R.string.fail_to_register)
            } else if (responseEvent.type == Constants.NO_CONNECTION) {
                Toast(R.string.no_internet_connection)
            } else {
                val result: com.washcar.app.models.ResultAPIModel<MemberModel> =
                    responseEvent.data as com.washcar.app.models.ResultAPIModel<MemberModel>
                if (responseEvent.type == Constants.SUCCESS) {
                    val user: MemberModel = result.data
                    UtilityApp.userData = user

                    val intent = Intent(getActiviy(), Constants.MAIN_ACTIVITY_CLASS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                }

            }
        }

    }

    private fun isValidForm(): Boolean {
        return FormValidator.getInstance()
            .addField(
                binding.mobileTxt,
                NonEmptyRule(R.string.enter_phone_number),
                LengthRule(10, R.string.valid_phone_number)
            )

            .validate()
    }


}
