package com.washcar.app.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.washcar.app.R
import com.washcar.app.adapters.RequestsAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.models.RequestModel
import kotlinx.android.synthetic.main.fragment_all_request.*
import kotlinx.android.synthetic.main.layout_fail_get_data.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_pre_loading.*

class CurrentDriverFragment : FragmentBase() {

    var activity: Activity? = null
    var finishRequestList: MutableList<RequestModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_all_request, container, false)
        activity = getActivity()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()

        rv.layoutManager = GridLayoutManager(getActivity(), 1)

        swipeDataContainer.setOnRefreshListener {
            if (UtilityApp.isLogin)
                getCurrentRequests(true)
            else
                swipeDataContainer.isRefreshing = false
        }

        getCurrentRequests(true)


    }

    override fun onResume() {
        super.onResume()
        activity = getActivity()
    }

    private fun initAdapter() {

        val adapter = RequestsAdapter(getActivity(), finishRequestList)
        rv.adapter = adapter
    }


    private fun getCurrentRequests(loading: Boolean) {
        if (loading) {
            loadingProgressLY.visibility = visible
            failGetDataLY.visibility = gone
            dataLY.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                if (isVisible){
                    loadingProgressLY.visibility = gone

                    if (swipeDataContainer.isRefreshing)
                        swipeDataContainer.isRefreshing = false

                    if (func == Constants.SUCCESS) {

                        finishRequestList = obj as MutableList<RequestModel>?

                        dataLY.visibility = visible
                        if (finishRequestList?.isNotEmpty() == true) {
                            noDataLY.visibility = gone
                            rv.visibility = visible
                            initAdapter()

                        } else {
                            noDataLY.visibility = visible
                            rv.visibility = gone
                        }
                    } else {
                        failGetDataLY.visibility = visible
                        dataLY.visibility = gone
                    }
                }

            }
        }).getCurrentRequests(UtilityApp.userData?.email)
    }


}

