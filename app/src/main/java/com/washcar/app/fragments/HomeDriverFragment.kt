package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.washcar.app.R
import com.washcar.app.adapters.RequestsAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentHomeDriverBinding
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RequestModel


class HomeDriverFragment : FragmentBase() {

    var user: MemberModel? = null

    var ordersList: MutableList<RequestModel?>? = null

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

        user = UtilityApp.userData

        binding.toolBar.mainTitleTxt.text = getString(R.string.my_requests)

        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        binding.lyEmpty.noDataTxt.text = getString(R.string.no_order)

        binding.swipeDataContainer.setColorSchemeColors(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorPrimary
            )
        )

        binding.swipeDataContainer.setOnRefreshListener {
            getData(false)
        }

        binding.lyFail.refreshBtn.setOnClickListener {
            getData(true)
        }

        getData(true)

    }

    fun initAdapter(list: MutableList<RequestModel?>?) {
        val adapter = RequestsAdapter(requireActivity(), list)
        binding.rv.adapter = adapter
    }

    private fun getData(loading: Boolean) {
        if (loading) {
            binding.lyLoading.loadingProgressLY.visibility = visible
            binding.lyFail.failGetDataLY.visibility = gone
            binding.swipeDataContainer.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                binding.lyLoading.loadingProgressLY.visibility = gone
                if (binding.swipeDataContainer.isRefreshing)
                    binding.swipeDataContainer.isRefreshing = false

                if (func == Constants.SUCCESS) {

                    binding.swipeDataContainer.visibility = visible
                    ordersList = obj as MutableList<RequestModel?>?

                    if (ordersList?.isNotEmpty() == true) {
                        binding.lyEmpty.noDataLY.visibility = gone
                        binding.rv.visibility = visible
                        initAdapter(ordersList)
                    } else {
                        binding.lyEmpty.noDataLY.visibility = visible
                        binding.lyEmpty.noDataTxt.text = getString(R.string.no_categories)
                        binding.rv.visibility = gone
                    }
                } else {
                    binding.lyFail.failGetDataLY.visibility = visible
                    binding.swipeDataContainer.visibility = gone
                }


            }
        }).getProviderAllRequests(user?.email ?: "", RequestModel.STATUS_UPCOMING)
    }

}