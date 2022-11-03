package com.washcar.app.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.Utils.MapHandler
import com.washcar.app.activities.DriversMapActivity
import com.washcar.app.activities.MapActivity
import com.washcar.app.adapters.AnnouncementsAdapter
import com.washcar.app.adapters.CarWashAdapter
import com.washcar.app.adapters.RequestsAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentMainScreenBinding
import com.washcar.app.databinding.FragmentServiceProviderBinding
import com.washcar.app.models.CarWashModel
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RequestModel
import kotlinx.android.synthetic.main.fragment_main_screen.*
import kotlinx.android.synthetic.main.layout_fail_get_data.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_pre_loading.*


class HomeClientFragment : FragmentBase() {
    var activity: Activity? = null
    private lateinit var binding: FragmentMainScreenBinding

    private var selectedDestinationLat = 0.0
    private var selectedDestinationLng = 0.0

    var user: MemberModel? = null
    var allAnnouncement: MutableList<CarWashModel?>? = null
    var bestRateList: MutableList<CarWashModel?>? = null
    var nearList: MutableList<CarWashModel?>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        activity = getActivity()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
        binding.toolBar.mainTitleTxt.text=getString(R.string.home)

        user = UtilityApp.userData
        allAnnouncement = mutableListOf()


        binding.announceRecycler.isNestedScrollingEnabled = false
        binding.carsWashRecycler.isNestedScrollingEnabled = false
        binding.bestRateRecycler.isNestedScrollingEnabled = false

        binding.announceRecycler.layoutManager =
            LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false)
        binding.carsWashRecycler.layoutManager =
            GridLayoutManager(requireActivity(), 2, RecyclerView.HORIZONTAL, false)
        binding.bestRateRecycler.layoutManager =
            LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false)


        allAnnouncement?.add(CarWashModel())
        allAnnouncement?.add(CarWashModel())
        allAnnouncement?.add(CarWashModel())
        allAnnouncement?.add(CarWashModel())
        allAnnouncement?.add(CarWashModel())
        allAnnouncement?.add(CarWashModel())
        allAnnouncement?.add(CarWashModel())

        initAnnouncementsAdapter()
        initCarWashNearAdapter()
        initBestRateAdapter()


        //getAllOrders(true)


    }


    override fun onResume() {
        super.onResume()
        activity = getActivity()
    }


//    private fun getAllOrders(loading: Boolean) {
//        if (loading) {
//            loadingProgressLY.visibility = visible
//            failGetDataLY.visibility = gone
//            dataLY.visibility = gone
//        }
//        DataFeacher(object : DataFetcherCallBack {
//            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//
//                loadingProgressLY.visibility = gone
//
//                if (swipeDataContainer.isRefreshing)
//                    swipeDataContainer.isRefreshing = false
//
//                if (func == Constants.SUCCESS) {
//
//                    dataLY.visibility = visible
//                    allRequestList = obj as MutableList<RequestModel>?
//
//                    if (allRequestList?.isNotEmpty() == true) {
//                        noDataLY.visibility = gone
//                        rv.visibility = visible
//                        initAdapter()
//
//                    } else {
//                        dataLY.visibility = gone
//                        noDataLY.visibility = visible
//                        rv.visibility = gone
//                    }
//                } else {
//                    failGetDataLY.visibility = visible
//                    dataLY.visibility = gone
//                }
//
//
//            }
//        }).getAllClientRequests(UtilityApp.userData?.mobileWithCountry)
//    }

    private fun initAnnouncementsAdapter() {

        val adapter = AnnouncementsAdapter(requireContext(), allAnnouncement)
        binding.announceRecycler.adapter = adapter
    }

    private fun initCarWashNearAdapter() {

        val adapter = CarWashAdapter(requireContext(), allAnnouncement)
        binding.carsWashRecycler.adapter = adapter
    }



    private fun initBestRateAdapter() {

        val adapter = CarWashAdapter(requireContext(), allAnnouncement)
        binding.bestRateRecycler.adapter = adapter
    }


}