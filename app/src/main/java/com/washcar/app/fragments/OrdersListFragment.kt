package com.washcar.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.adapters.RequestsAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentAllOrdersBinding
import com.washcar.app.models.MemberModel
import com.washcar.app.models.RequestModel

class OrdersListFragment : FragmentBase() {

    private var orderList: MutableList<RequestModel?>? = null
    var ordersList: MutableList<RequestModel?>? = null
    var user: MemberModel? = null
    private var requestType: String? = null

    private lateinit var binding: FragmentAllOrdersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllOrdersBinding.inflate(inflater, container, false)
        user = UtilityApp.userData




        val bundle = this.arguments
        if (bundle != null) {
            requestType=bundle.getString(Constants.KEY_REQUEST_TYPE)
            Log.i("tag", "Log requestType$requestType")
        }
        orderList= mutableListOf()

        val lm = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rv.layoutManager = lm
        binding.rv.setHasFixedSize(true)




        getData(true,requestType)

       initListeners()
        return binding.root

    }

    private fun initListeners() {

        binding.swipeDataContainer.setColorSchemeColors(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorPrimary
            )
        )

        binding.swipeDataContainer.setOnRefreshListener {
//            getData(false)
        }

        binding.lyFail.refreshBtn.setOnClickListener {
            getData(true,requestType)
        }    }


    private fun initAdapter() {
        val adapter = RequestsAdapter(requireActivity(), ordersList)
        binding.rv.adapter = adapter
    }


    private fun getData(loading: Boolean,requestType:String?) {
        if (loading) {
            binding.lyLoading.loadingProgressLY.visibility = visible
            binding.lyFail.failGetDataLY.visibility = gone
            binding.swipeDataContainer.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                binding.lyLoading.loadingProgressLY.visibility = gone

                if (func == Constants.SUCCESS) {

                    binding.swipeDataContainer.visibility = visible
                    ordersList = obj as MutableList<RequestModel?>?

                    if (ordersList?.isNotEmpty() == true) {
                        binding.lyEmpty.noDataLY.visibility = gone
                        binding.rv.visibility = visible
                        initAdapter()
                    } else {
                        binding.lyEmpty.noDataLY.visibility = visible
                        binding.lyEmpty.noDataTxt.text = getString(R.string.no_order)
                        binding.rv.visibility = gone
                    }
                } else {
                    binding.lyFail.failGetDataLY.visibility = visible
                    binding.swipeDataContainer.visibility = gone
                }


            }
        }).getAllClientRequests(user?.email, requestType?:RequestModel.STATUS_UPCOMING)
    }

}