package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.washcar.app.adapters.RequestsAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentAllRequestBinding
import com.washcar.app.models.RequestModel

class CurrentDriverFragment : FragmentBase() {

    var finishRequestList: MutableList<RequestModel?>? = null

    private var _binding: FragmentAllRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.layoutManager = GridLayoutManager(requireContext(), 1)



        getCurrentRequests(true)


    }


    private fun initAdapter() {

        val adapter = RequestsAdapter(requireActivity(), finishRequestList)
        binding.rv.adapter = adapter
    }


    private fun getCurrentRequests(loading: Boolean) {
        if (loading) {
            binding.lyLoading.loadingProgressLY.visibility = visible
            binding.lyFail.failGetDataLY.visibility = gone
            binding.dataLY.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                if (isVisible) {
                    binding.lyLoading.loadingProgressLY.visibility = gone

                    if (func == Constants.SUCCESS) {

                        finishRequestList = obj as MutableList<RequestModel?>?

                        binding.dataLY.visibility = visible
                        if (finishRequestList?.isNotEmpty() == true) {
                            binding.lyEmpty.noDataLY.visibility = gone
                            binding.rv.visibility = visible
                            initAdapter()

                        } else {
                            binding.lyEmpty.noDataLY.visibility = visible
                            binding.rv.visibility = gone
                        }
                    } else {
                        binding.lyFail.failGetDataLY.visibility = visible
                        binding.dataLY.visibility = gone
                    }
                }

            }
        }).getCurrentRequests(UtilityApp.userData?.email)
    }


}

