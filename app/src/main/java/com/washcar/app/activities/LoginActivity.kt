package com.washcar.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.github.dhaval2404.form_validation.rule.EmailRule
import com.washcar.app.models.*
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.Utils.SharedPManger
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.AESCrypt
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.google.firebase.firestore.DocumentSnapshot

import com.washcar.app.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.emailInput
import kotlinx.android.synthetic.main.activity_login.emailTxt
import kotlinx.android.synthetic.main.activity_login.loginBtn

import kotlinx.android.synthetic.main.activity_login.passwordInput
import kotlinx.android.synthetic.main.activity_login.passwordTxt
import kotlinx.android.synthetic.main.activity_login.registerBtn


class LoginActivity : ActivityBase() {

    lateinit var binding: ActivityLoginBinding

    companion object {
        const val REQUEST_LOGIN = 110
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = ""


       binding.toolBar.homeBtn.setOnClickListener {
            onBackPressed()
        }

        binding.loginBtn.setOnClickListener {

            if (isValidForm()) {
                LoginByEmail()


            }


        }


        binding.registerBtn.setOnClickListener {

            val intent = Intent(getActiviy(), RegisterTypeActivity::class.java)
            startActivity(intent)

        }


    }

    private fun loginUser() {
        val memberModel = com.washcar.app.models.RegisterUserModel()

        try {

            val passwordStr = NumberHandler.arabicToDecimal(passwordTxt.text.toString())


            memberModel.password = passwordStr
            GlobalData.progressDialog(
                getActiviy(),
                R.string.sign_in,
                R.string.please_wait_login
            )
            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    GlobalData.progressDialogHide()
                    if (func == Constants.USER_NOT_EXIST) {
                        GlobalData.errorDialog(
                            getActiviy(),
                            R.string.login,
                            getString(R.string.not_have_account_q)
                        )
                    } else if (func == Constants.SUCCESS) {
                        val document: DocumentSnapshot = obj as DocumentSnapshot
                        Log.d(javaClass.simpleName, "DocumentSnapshot data: ${document.data}")

                        val user = document.toObject(MemberModel::class.java)
                        val password = AESCrypt.decrypt(user?.password)

                        val isVerified = document.get("isVerified") as Boolean

                        val fullName = document.get("fullName")
                        Log.d(javaClass.simpleName, "DocumentSnapshot data1: $fullName")
                        Log.d(
                            javaClass.simpleName,
                            "DocumentSnapshot isVerified: ${document.getBoolean("isVerified")}"
                        )

                        if (password == memberModel.password) {
                            if (isVerified) {
                                UtilityApp.userData = user
                                val intent = Intent(getActiviy(), com.washcar.app.MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                val intent = Intent(getActiviy(), ConfirmActivity::class.java)
                                intent.putExtra(Constants.KEY_MEMBER, user)
                                intent.putExtra(
                                    Constants.KEY_MOBILE,
                                    memberModel.mobileWithCountry
                                )
                                startActivity(intent)

                            }

                        } else {
                            GlobalData.errorDialog(
                                getActiviy(),
                                R.string.login,
                                getString(R.string.mobile_password_not_match)
                            )
                        }

                    } else {
                        GlobalData.errorDialog(
                            getActiviy(),
                            R.string.login,
                            getString(R.string.fail_to_sign_in)
                        )

                    }

                }
            }).loginHandle(memberModel)

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    private fun LoginByEmail() {

        val emailStr = NumberHandler.arabicToDecimal(emailTxt.text.toString())
        val passwordStr = NumberHandler.arabicToDecimal(passwordTxt.text.toString())
        GlobalData.progressDialog(
            getActiviy(),
            R.string.sign_in,
            R.string.please_wait_login
        )
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()
                if (func == Constants.SUCCESS) {

                    val user = obj as RegisterUserModel?


                    loginUser()

                } else {
                    GlobalData.errorDialog(
                        getActiviy(),
                        R.string.login,
                        getString(R.string.mobile_password_not_match)
                    )
                }

            }
        }).getAccountByEmail(emailStr)


//        }

    }

    private fun isValidForm(): Boolean {

            return FormValidator.getInstance()
                .addField(
                    emailInput,
                    NonEmptyRule(R.string.ENTER_EMAIL),
                    EmailRule(R.string.enter_vaild_email)

                )

                .addField(
                    passwordInput,
                    NonEmptyRule(R.string.enter_password)
                )
                .setErrorListener {

                }
                .validate()
        }



    }





