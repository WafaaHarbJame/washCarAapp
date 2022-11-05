package com.washcar.app.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.activities.SearchProvidersActivity
import com.washcar.app.adapters.AnnouncementsAdapter
import com.washcar.app.adapters.CarWashAdapter
import com.washcar.app.adapters.HorizontalCarWashAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentMainScreenBinding
import com.washcar.app.models.MemberModel


class HomeClientFragment : FragmentBase() {
    var activity: Activity? = null
    private lateinit var binding: FragmentMainScreenBinding

    var user: MemberModel? = null
    var providersList: MutableList<MemberModel?>? = null
//    var bestRateList: MutableList<MemberModel?>? = null
//    var otherList: MutableList<MemberModel?>? = null


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
        binding.toolBar.mainTitleTxt.text = getString(R.string.home)

        user = UtilityApp.userData
//        allAnnouncement = mutableListOf()


        binding.announceRecycler.isNestedScrollingEnabled = false
        binding.carsWashRecycler.isNestedScrollingEnabled = false
        binding.bestRateRecycler.isNestedScrollingEnabled = false

        binding.announceRecycler.layoutManager =
            LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false)
        binding.bestRateRecycler.layoutManager =
            LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false)
        binding.carsWashRecycler.layoutManager = LinearLayoutManager(requireActivity())


        binding.filterBut.setOnClickListener {
            val intent = Intent(requireActivity(), SearchProvidersActivity::class.java)
            startActivity(intent)
        }

        getData(true)

    }


    override fun onResume() {
        super.onResume()
        activity = getActivity()
    }


    private fun getData(loading: Boolean) {
        if (loading) {
            binding.lyLoading.loadingProgressLY.visibility = visible
            binding.lyFail.failGetDataLY.visibility = gone
            binding.lyData.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                binding.lyLoading.loadingProgressLY.visibility = gone

                if (func == Constants.SUCCESS) {

                    binding.lyData.visibility = visible
                    val list = obj as MutableList<MemberModel>?
                    providersList = list?.filter {
                        it.type == MemberModel.TYPE_SERVICE_PROVIDER
                    }?.toMutableList()

                    if (providersList?.isNotEmpty() == true) {

                        filterListToAdapters()

                    }
                } else {
                    binding.lyFail.failGetDataLY.visibility = visible
                    binding.lyData.visibility = gone
                }


            }
        }).getUsers()
    }

    fun filterListToAdapters() {
        val announcedProvidersList = providersList?.filter {
            it?.announced == true
        }?.toMutableList()
        initAnnouncementsAdapter(announcedProvidersList)

        val bestRateList = providersList?.toMutableList()
        bestRateList?.sortByDescending {
            it?.rate
        }
        initBestRateAdapter(bestRateList)

        initCarWashOtherAdapter(providersList)
    }


    private fun initAnnouncementsAdapter(list: MutableList<MemberModel?>?) {
        if (list?.isNotEmpty() == true) {
            binding.announceRecycler.visibility = visible
            binding.announceLy.visibility = visible
            val adapter = AnnouncementsAdapter(requireContext(), list)
            binding.announceRecycler.adapter = adapter
        } else {
            binding.announceRecycler.visibility = gone
            binding.announceLy.visibility = gone
        }

    }

    private fun initCarWashOtherAdapter(list: MutableList<MemberModel?>?) {

        if (list?.isNotEmpty() == true) {
            binding.carsWashRecycler.visibility = visible
            binding.carWashOtherLy.visibility = visible
            val adapter = CarWashAdapter(requireContext(), list)
            binding.carsWashRecycler.adapter = adapter
        } else {
            binding.carsWashRecycler.visibility = gone
            binding.carWashOtherLy.visibility = gone
        }


    }


    private fun initBestRateAdapter(list: MutableList<MemberModel?>?) {
        if (list?.isNotEmpty() == true) {
            binding.bestRateRecycler.visibility = visible
            binding.bestRateLy.visibility = visible
            val adapter = HorizontalCarWashAdapter(requireContext(), list)
            binding.bestRateRecycler.adapter = adapter
        } else {
            binding.bestRateRecycler.visibility = gone
            binding.bestRateLy.visibility = gone
        }


    }


}