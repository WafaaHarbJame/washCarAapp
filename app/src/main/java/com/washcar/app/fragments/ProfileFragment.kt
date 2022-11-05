package com.washcar.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentProfileBinding
import com.washcar.app.models.MemberModel
import com.washcar.app.models.ReviewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    var user: MemberModel? = null
    var type:String=""
    var showProfile: Boolean=false
    private var carWashModel: MemberModel? = null
    var reviewList: MutableList<ReviewModel?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        user = UtilityApp.userData
        type=user?.type?:MemberModel.TYPE_CUSTOMER

        initData()


        val bundle = requireActivity().intent.extras
        if (bundle != null) {
            showProfile=bundle.getBoolean(Constants.key_show_profile)
            type=bundle.getString(Constants.KEY_TYPE)?: MemberModel.TYPE_CUSTOMER
            carWashModel = bundle.getSerializable(Constants.key_provider_data) as MemberModel?

        }

        if(showProfile){

        }

        return  binding.root
    }


    private fun initData() {
        if(type==MemberModel.TYPE_SERVICE_PROVIDER){
            binding.serviceProviderLy.visibility=View.VISIBLE
            binding.providerEmailTv.text=user?.email
            binding.fullNameTv.text=user?.fullName
            binding.aboutTv.text=user?.description
            binding.mobilelTv.text=user?.mobile
            binding.startTv.text=user?.startTime
            binding.endTv.text=user?.endTime
        }
        else{
            binding.serviceProviderLy.visibility=View.GONE
            binding.customerEmailTv.text=user?.email
            binding.customerNameTv.text=user?.fullName

        }

    }


}