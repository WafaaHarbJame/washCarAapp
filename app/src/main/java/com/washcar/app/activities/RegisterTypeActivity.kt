package com.washcar.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.washcar.app.R
import com.washcar.app.classes.Constants
import com.washcar.app.databinding.ActivityLoginBinding
import com.washcar.app.databinding.ActivityRegisterTypeBinding
import kotlinx.android.synthetic.main.activity_register_type.*

class RegisterTypeActivity : ActivityBase() {
    private var isUser = true
    lateinit var binding: ActivityRegisterTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterTypeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.userSelectLY.setOnClickListener {

            selectType(true)
            isUser = true

        }

        binding.driverSelectLY.setOnClickListener {

            selectType(false)
            isUser = false

        }

        binding.nextBtn.setOnClickListener {

            val intent = Intent(getActiviy(), RegisterActivity::class.java)
            intent.putExtra(Constants.KEY_IS_CUSTOMER, isUser)
            startActivity(intent)

        }

        backBtn.setOnClickListener {

            onBackPressed()

        }

    }

    private fun selectType(isCustomer: Boolean) {

        if (isCustomer) {
            binding.userSelectTxt.setTextColor(
                ContextCompat.getColor(
                    getActiviy(),
                    R.color.colorPrimaryDark
                )
            )
            binding.userSelectLY.background = ContextCompat.getDrawable(
                getActiviy(),
                R.drawable.round_corner_white_fill_border_primary_dark
            )

            binding.driverSelectTxt.setTextColor(
                ContextCompat.getColor(
                    getActiviy(),
                    R.color.colorAccent2
                )
            )
            binding.driverSelectLY.background = ContextCompat.getDrawable(
                getActiviy(),
                R.drawable.round_corner_white_fill_border_gray
            )
        } else {
            binding.driverSelectTxt.setTextColor(
                ContextCompat.getColor(
                    getActiviy(),
                    R.color.colorPrimaryDark
                )
            )
            binding.driverSelectLY.background = ContextCompat.getDrawable(
                getActiviy(),
                R.drawable.round_corner_white_fill_border_primary_dark
            )

            binding.userSelectTxt.setTextColor(
                ContextCompat.getColor(
                    getActiviy(),
                    R.color.colorAccent2
                )
            )
            binding.userSelectLY.background = ContextCompat.getDrawable(
                getActiviy(),
                R.drawable.round_corner_white_fill_border_gray
            )
        }

    }
}