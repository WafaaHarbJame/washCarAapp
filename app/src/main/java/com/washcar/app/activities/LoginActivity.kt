package com.washcar.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.washcar.app.MainActivity
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.AESCrypt
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityLoginBinding
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RegisterUserModel


class LoginActivity : ActivityBase() {

    lateinit var binding: ActivityLoginBinding

    companion object {
        const val REQUEST_LOGIN = 110
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.login)


        binding.toolBar.homeBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.loginBtn.setOnClickListener {

            loginUser()

        }


        binding.registerBtn.setOnClickListener {

            val intent = Intent(getActiviy(), RegisterActivity::class.java)
            startActivity(intent)

        }


    }

    private fun loginUser() {

        try {

            val emailStr = NumberHandler.arabicToDecimal(binding.emailTxt.text.toString())
            val passwordStr = NumberHandler.arabicToDecimal(binding.passwordTxt.text.toString())

            var hasError = false
            if (emailStr.isEmpty()) {
                binding.emailInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (passwordStr.isEmpty()) {
                binding.passwordInput.error = getString(R.string.invalid_input)
                hasError = true
            }
            if (hasError)
                return

            val memberModel = RegisterUserModel().apply {
                this.email = emailStr
                this.password = passwordStr
            }

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

//                        val isVerified = document.get("isVerified") as Boolean

//                        val fullName = document.get("fullName")
//                        Log.d(javaClass.simpleName, "DocumentSnapshot data1: $fullName")
//                        Log.d(
//                            javaClass.simpleName,
//                            "DocumentSnapshot isVerified: ${document.getBoolean("isVerified")}"
//                        )

                        if (password == memberModel.password) {
                            UtilityApp.userData = user
                            val intent = Intent(getActiviy(), MainActivity::class.java)
                            startActivity(intent)
                            finish()

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

}





