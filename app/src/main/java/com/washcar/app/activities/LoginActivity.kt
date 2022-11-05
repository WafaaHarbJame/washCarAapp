package com.washcar.app.activities

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.washcar.app.MainActivity
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
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

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.login)

        auth = FirebaseAuth.getInstance()

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

            auth.signInWithEmailAndPassword(
                memberModel.email ?: "",
                memberModel.password ?: ""
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                        val authUser = auth.currentUser

                        getMyProfile(memberModel.email)
//                        sendUserToFirebase(registerUserModel)
                    } else {
                        GlobalData.progressDialogHide()
                        // If sign in fails, display a message to the user.
                        val message = task.exception?.message
                        GlobalData.errorDialog(
                            this@LoginActivity, R.string.register, message
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
//            DataFeacher(object : DataFetcherCallBack {
//                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                    GlobalData.progressDialogHide()
//                    if (func == Constants.USER_NOT_EXIST) {
//                        GlobalData.errorDialog(
//                            getActiviy(),
//                            R.string.login,
//                            getString(R.string.not_have_account_q)
//                        )
//                    } else if (func == Constants.SUCCESS) {
//                        val document: DocumentSnapshot = obj as DocumentSnapshot
//                        Log.d(javaClass.simpleName, "DocumentSnapshot data: ${document.data}")
//
//                        val user = document.toObject(MemberModel::class.java)
//                        val password = AESCrypt.decrypt(user?.password)
//
////                        val isVerified = document.get("isVerified") as Boolean
//
////                        val fullName = document.get("fullName")
////                        Log.d(javaClass.simpleName, "DocumentSnapshot data1: $fullName")
////                        Log.d(
////                            javaClass.simpleName,
////                            "DocumentSnapshot isVerified: ${document.getBoolean("isVerified")}"
////                        )
//
//                        if (password == memberModel.password) {
//                            getMyProfile(memberModel.email)
//
//                        } else {
//                            GlobalData.errorDialog(
//                                getActiviy(),
//                                R.string.login,
//                                getString(R.string.mobile_password_not_match)
//                            )
//                        }
//
//                    } else {
//                        GlobalData.errorDialog(
//                            getActiviy(),
//                            R.string.login,
//                            getString(R.string.fail_to_sign_in)
//                        )
//
//                    }
//
//                }
//            }).loginHandle(memberModel)

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun getMyProfile(email: String) {

        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()
                if (func == Constants.SUCCESS) {
                    val user = obj as MemberModel?
                    if (user != null) {
                        UtilityApp.userData = user

                        val intent = Intent(getActiviy(), MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast(R.string.user_not_exist)
                    }

                }
            }
        }).getMyAccount(email)
    }

}





