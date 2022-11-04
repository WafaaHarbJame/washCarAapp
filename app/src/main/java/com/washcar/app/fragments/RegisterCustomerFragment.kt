package com.washcar.app.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.dhaval2404.form_validation.rule.EmailRule
import com.github.dhaval2404.form_validation.rule.EqualRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.activities.ConfirmActivity
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.AESCrypt
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.databinding.FragmentCustomerRegisterBinding
import com.washcar.app.models.MemberModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterCustomerFragment : Fragment() {
    private lateinit var binding: FragmentCustomerRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerRegisterBinding.inflate(inflater, container, false)

        initListeners()


        return binding.root
    }

    private fun initListeners() {
        binding.registerBtn.setOnClickListener {

            if (isValidForm()) {
                registerUser()
            }

        }
    }


    private fun registerUser() {

        try {

            val fullNameStr = NumberHandler.arabicToDecimal(NameTxt.text.toString())
            val mobileStr = NumberHandler.arabicToDecimal(mobileTxt.text.toString())
            val passwordStr = NumberHandler.arabicToDecimal(passwordTxt.text.toString())
            val emailStr = NumberHandler.arabicToDecimal(emailTxt.text.toString())

            val registerUserModel = MemberModel()
            registerUserModel.mobile =
                if (mobileStr.startsWith("0")) mobileStr.replaceFirst(
                    "0",
                    ""
                ) else mobileStr

            registerUserModel.fullName = fullNameStr
            registerUserModel.password = AESCrypt.encrypt(passwordStr);
            registerUserModel.passwordConfirm = AESCrypt.encrypt(passwordStr)
            registerUserModel.type = Constants.user_type
            registerUserModel.isSelectLocation = false
            registerUserModel.email = emailStr


            GlobalData.progressDialog(
                activity,
                R.string.register,
                R.string.please_wait_register
            )

            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    GlobalData.progressDialogHide()

                    if (func == Constants.SUCCESS) {

                        val intent = Intent(activity, ConfirmActivity::class.java)
                        intent.putExtra(Constants.KEY_MEMBER, registerUserModel)
                        intent.putExtra(Constants.KEY_MOBILE, registerUserModel.email)
                        startActivity(intent)
                    } else {
                        var message = getString(R.string.fail_to_register)
                        if (func == Constants.USER_EXIST)
                            message = getString(R.string.email_exist)

                        GlobalData.errorDialog(
                            activity,
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
                fullNameInput,
                NonEmptyRule(R.string.enter_fill_name),
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


}