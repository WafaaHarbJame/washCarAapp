package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.washcar.app.R
import com.washcar.app.classes.Constants
import com.washcar.app.databinding.FragmentHomeDriverBinding


class HomeDriverFragment : FragmentBase() {

    var isGrantPermission = false
    private var selectedLat = 0.0
    private var selectedLng = 0.0
    var address: String = ""
    var isSelectLocation = false

    private var _binding: FragmentHomeDriverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDriverBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainTitleTxt.text = getString(R.string.app_name)
        binding.homeBtn.visibility = gone

        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);

    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)

        val currentBundle = Bundle()
        currentBundle.putString(Constants.KEY_TYPE, Constants.CURRENT)
        val currentFragment: Fragment = CurrentDriverFragment()
        currentFragment.arguments = currentBundle

        adapter.addFragment(currentFragment, getString(R.string.current_request))


        viewPager.adapter = adapter
    }

    class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val mFragmentList: MutableList<Fragment> = ArrayList<Fragment>()
        private val mFragmentTitleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }


//    private fun updateData() {
//        try {
//            DataFeacher(object : DataFetcherCallBack {
//                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                }
//            }).updateData(mobileStr,selectedLat,selectedLng,address,isSelectLocation);
//
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//
//        }
//    }


}