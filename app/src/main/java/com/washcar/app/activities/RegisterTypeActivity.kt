package com.washcar.app.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.washcar.app.R
import com.washcar.app.adapters.TypesAdapter
import com.washcar.app.databinding.ActivityRegisterTypeBinding

class RegisterTypeActivity : ActivityBase() {
    private var isUser = true
    lateinit var binding: ActivityRegisterTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterTypeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        binding.toolBar.mainTitleTxt.text=getString(R.string.register)

         val adapter = TypesAdapter(getActiviy(), supportFragmentManager)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
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
        binding.tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActiviy(), R.color.colorAccent))

    }



    }

