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


class MainAdminFragment : FragmentBase() {

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

        binding.swipeDataContainer.setColorSchemeColors(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorPrimary
            )
        )
        binding.swipeDataContainer.setOnRefreshListener {

        }

    }

    override fun onResume() {
        super.onResume()
        if (GlobalData.REFRESH_DRIVERS) {
            GlobalData.REFRESH_DRIVERS = false
        }
    }



    fun initAdapter() {
        val adapter = DriversAdapter(requireActivity(), driversList!!,
            object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                }
            })
        binding.rv.adapter = adapter
    }
}