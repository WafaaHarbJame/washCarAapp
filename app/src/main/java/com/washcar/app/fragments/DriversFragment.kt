package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.washcar.app.R
import com.washcar.app.adapters.DriversAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.databinding.FragmentDriversBinding
import com.washcar.app.models.DriverModel


class DriversFragment : FragmentBase() {

    var driversList: MutableList<DriverModel?>? = null

    private var _binding: FragmentDriversBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriversBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainTitleTxt.text = getString(R.string.manage_drivers)

        binding.rv.layoutManager = LinearLayoutManager(requireActivity())

        binding.addDoctorBtn.setOnClickListener {

//            val intent = Intent(requireActivity(), AddDriverActivity::class.java)
//            intent.putExtra(Constants.KEY_DOCTOR_MODEL, doctorModel)
//            startActivity(intent)

        }

        getDrivers()

        binding.swipeDataContainer.setColorSchemeColors(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorPrimary
            )
        )
        binding.swipeDataContainer.setOnRefreshListener {
            getDrivers()

        }

    }

    override fun onResume() {
        super.onResume()
        if (GlobalData.REFRESH_DRIVERS) {
            GlobalData.REFRESH_DRIVERS = false
            getDrivers()
        }
    }


    private fun getDrivers() {

        binding.swipeDataContainer.isRefreshing = true
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                binding.swipeDataContainer.isRefreshing = false
                if (func == Constants.SUCCESS) {
                    driversList = obj as MutableList<DriverModel?>?
                    if (driversList?.isNotEmpty() == true) {
                        binding.rv.visibility = visible
                        binding.lyEmpty.noDataLY.visibility = gone
                        initAdapter()
                    } else {
                        binding.rv.visibility = gone
                        binding.lyEmpty.noDataLY.visibility = visible
                        binding.lyEmpty.noDataTxt.text = getString(R.string.no_drivers)
                    }
                } else {
                    binding.rv.visibility = gone
                    binding.lyEmpty.noDataLY.visibility = visible
                    binding.lyEmpty.noDataTxt.text = getString(R.string.fail_to_get_data)
                }
            }
        }).getAllDrivers()

    }

    fun initAdapter() {
        val adapter = DriversAdapter(requireActivity(), driversList!!,
            object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    if (IsSuccess)
                        getDrivers()
                }
            })
        binding.rv.adapter = adapter
    }
}