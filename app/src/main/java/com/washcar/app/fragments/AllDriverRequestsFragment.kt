package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.washcar.app.adapters.RequestsAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentAllRequestBinding
import com.washcar.app.models.RequestModel

/**
 * A simple [Fragment] subclass.
 */
class AllDriverRequestsFragment : FragmentBase() {

    var allRequeststList: MutableList<RequestModel>? = null

    private var _binding: FragmentAllRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllRequestBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.layoutManager = GridLayoutManager(getActivity(), 1)

        binding.swipeDataContainer.setOnRefreshListener {
            if (UtilityApp.isLogin) getAllRequests(true)
            else binding.swipeDataContainer.isRefreshing = false
        }

        getAllRequests(true)

    }


    private fun initAdapter() {

        val adapter = RequestsAdapter(getActivity(), allRequeststList)
        binding.rv.adapter = adapter
    }


    private fun getAllRequests(loading: Boolean) {
        if (loading) {
            binding.lyLoading.loadingProgressLY.visibility = visible
            binding.lyFail.failGetDataLY.visibility = gone
            binding.dataLY.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                binding.lyLoading.loadingProgressLY.visibility = gone

                if (binding.swipeDataContainer.isRefreshing) binding.swipeDataContainer.isRefreshing =
                    false

                if (func == Constants.SUCCESS) {

                    binding.dataLY.visibility = visible
                    allRequeststList = obj as MutableList<RequestModel>?

                    if (allRequeststList?.isNotEmpty() == true) {
                        binding.lyEmpty.noDataLY.visibility = gone
                        binding.rv.visibility = visible
                        initAdapter()

                    } else {
                        binding.dataLY.visibility = gone
                        binding.lyEmpty.noDataLY.visibility = visible
                        binding.rv.visibility = gone
                    }
                } else {
                    binding.lyFail.failGetDataLY.visibility = visible
                    binding.dataLY.visibility = gone
                }


            }
        }).getAllRequests(UtilityApp.userData?.email)
    }


}

