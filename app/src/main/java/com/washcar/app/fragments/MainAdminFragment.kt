package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.washcar.app.R
import com.washcar.app.adapters.MainAdminAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.databinding.FragmentMainAdminBinding
import com.washcar.app.models.MemberModel


class MainAdminFragment : FragmentBase() {

    var usersList: MutableList<MemberModel>? = null

    var userType = MemberModel.TYPE_CUSTOMER

    private var _binding: FragmentMainAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainTitleTxt.text = getString(R.string.manage_drivers)

        binding.rv.layoutManager = LinearLayoutManager(requireActivity())

//        binding.addDoctorBtn.setOnClickListener {
//
////            val intent = Intent(requireActivity(), AddDriverActivity::class.java)
////            intent.putExtra(Constants.KEY_DOCTOR_MODEL, doctorModel)
////            startActivity(intent)
//
//        }

        binding.btnCustomer.setOnClickListener {
            userType = MemberModel.TYPE_CUSTOMER
            selectType(userType == MemberModel.TYPE_CUSTOMER)
            filterList()
        }

        binding.btnProvider.setOnClickListener {
            userType = MemberModel.TYPE_SERVICE_PROVIDER
            selectType(userType == MemberModel.TYPE_CUSTOMER)
            filterList()
        }


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

    override fun onResume() {
        super.onResume()
        if (GlobalData.REFRESH_DRIVERS) {
            GlobalData.REFRESH_DRIVERS = false
        }
    }

    fun selectType(isCustomer: Boolean) {

        binding.tvCustomer.setTextColor(
            ContextCompat.getColor(
                requireContext(), if (isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcCustomer.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), if (isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.tvProvider.setTextColor(
            ContextCompat.getColor(
                requireContext(), if (!isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcProvider.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), if (!isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )

    }

    fun initAdapter(list: MutableList<MemberModel>) {
        val adapter = MainAdminAdapter(requireActivity(), list,
            object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                }
            })
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

                if (func == Constants.SUCCESS) {

                    binding.swipeDataContainer.visibility = visible
                    usersList = obj as MutableList<MemberModel>?

                    if (usersList?.isNotEmpty() == true) {
                        binding.lyEmpty.noDataLY.visibility = gone
                        binding.rv.visibility = visible

                        filterList()

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
        }).getUsers()
    }

    fun filterList() {
        val list = usersList?.filter {
            it.type == userType
        }
        initAdapter(list?.toMutableList() ?: mutableListOf())
    }

}