package com.washcar.app.activities

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.washcar.app.R
import com.washcar.app.adapters.SettingTabAdapter
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityProfileBinding
import com.washcar.app.models.MemberModel
import java.util.*

class ProfileActivity : ActivityBase() {
    lateinit var binding: ActivityProfileBinding
    var activity: Activity? = null
    var user: MemberModel? = null
    var email: String = ""
    var showProfile: Boolean=false

    var type: String = ""
    private var carWashModel: MemberModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.mainTitleTxt.text = getString(R.string.profile)
        activity = getActiviy()


        binding.toolBar.homeBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        user = UtilityApp.userData
        type = user?.type ?: MemberModel.TYPE_CUSTOMER

        val bundle = intent.extras
        if (bundle != null) {
            showProfile=bundle.getBoolean(Constants.key_show_profile)
            type=bundle?.getString(Constants.KEY_TYPE)?:""
            carWashModel = bundle.getSerializable(Constants.key_provider_data) as MemberModel?

        }

        if(showProfile){

        }

        initData()

        val adapter = SettingTabAdapter(this, supportFragmentManager, type)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                    }
                    1 -> {
                    }
                    2 -> {
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.viewPager.adapter = adapter
        binding.tabs.setSelectedTabIndicatorColor(Color.WHITE)
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent))


    }


    private fun initData() {
        binding.tvUserName.text = user?.fullName

        Glide.with(this)
            .asBitmap()
            .load(user?.photoUrl)
            .placeholder(R.drawable.error_logo)
            .into(binding.userImage)
    }


}

