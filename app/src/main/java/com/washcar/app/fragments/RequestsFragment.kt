package com.washcar.app.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.washcar.app.R
import com.washcar.app.adapters.OrderTabAdapter
import com.washcar.app.adapters.SettingTabAdapter
import com.washcar.app.databinding.FragmentRequestsBinding

/**
 * A simple [Fragment] subclass.
 */
class RequestsFragment : FragmentBase() {

    private lateinit var binding: FragmentRequestsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.mainTitleTxt.text = getString(R.string.orders)
        binding.toolBar.homeBtn.visibility = gone

        val adapter = OrderTabAdapter(requireContext(), requireActivity().supportFragmentManager)
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
        binding.tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))


    }


}