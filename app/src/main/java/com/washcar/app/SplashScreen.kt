package com.washcar.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.washcar.app.activities.ActivityBase
import com.washcar.app.activities.LoginActivity
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.models.MemberModel


class SplashScreen : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        startSplash()
    }

    private fun startSplash() {
        setContentView(R.layout.splash_screen)
        initData()
    }


    private fun initData() {

        DataFeacher(null).getCountries()

        Handler(Looper.getMainLooper()).postDelayed({

            if (UtilityApp.isLogin) {
                val mobile = UtilityApp.userData?.mobileWithCountry

                DataFeacher(object : DataFetcherCallBack {
                    override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                        if (func == Constants.SUCCESS) {
                            UtilityApp.userData = obj as MemberModel?
                            val intent = Intent(getActiviy(), MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast(getString(R.string.fail_confirm_account))
                            UtilityApp.logOut()

                            val intent = Intent(getActiviy(), LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                    }
                }).getMyAccount(mobile)

            } else {
                val intent = Intent(getActiviy(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIMER.toLong())
    }


    companion object {
        private const val SPLASH_TIMER = 3000
    }
}
