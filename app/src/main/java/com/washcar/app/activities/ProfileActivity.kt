package com.washcar.app.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.washcar.app.R
import com.washcar.app.adapters.ReviewAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.ActivityProfileBinding
import com.washcar.app.models.MemberModel
import com.washcar.app.models.ReviewModel

class ProfileActivity : ActivityBase() {
    lateinit var binding: ActivityProfileBinding
    var activity: Activity? = null
    var user: MemberModel? = null
    var email: String = ""
    var showProfile: Boolean=false
    private var type: String = ""
    private var carWashModel: MemberModel? = null
    var reviewList: MutableList<ReviewModel?>? = null
    private var userType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.mainTitleTxt.text = getString(R.string.profile)
        activity = getActiviy()


        binding.toolBar.homeBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        user = UtilityApp.userData
        type = user?.type ?: MemberModel.TYPE_CUSTOMER

        val bundle = intent.extras
        if (bundle != null) {
            showProfile=bundle.getBoolean(Constants.key_show_profile)
            type=bundle.getString(Constants.KEY_TYPE)?: MemberModel.TYPE_CUSTOMER
            carWashModel = bundle.getSerializable(Constants.key_provider_data) as MemberModel?

        }

        if(showProfile){
            binding.editProfile.visibility=gone
            initServiceData(carWashModel)
        }


        reviewList= mutableListOf()

        reviewList?.add(ReviewModel())
        reviewList?.add(ReviewModel())
        reviewList?.add(ReviewModel())
        reviewList?.add(ReviewModel())

        val reviewManger = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.reviewRecycler.layoutManager = reviewManger
        binding.reviewRecycler.setHasFixedSize(true)

        initReviewAdapter()


        initData()
        initListeners()


    }

    private fun initServiceData(carWashModel: MemberModel?) {
        getReview()
        binding.serviceProviderLy.visibility= View.VISIBLE
        binding.customerLy.visibility= View.GONE
        binding.providerEmailTv.text=carWashModel?.email
        binding.fullNameTv.text=carWashModel?.fullName
        binding.aboutTv.text=carWashModel?.description
        binding.mobilelTv.text=carWashModel?.mobile
        binding.startTv.text=carWashModel?.startTime
        binding.endTv.text=carWashModel?.endTime

    }

    private fun initListeners() {


        binding.btnCustomer.setOnClickListener {
            userType = MemberModel.TYPE_CUSTOMER
            selectType(userType == MemberModel.TYPE_CUSTOMER)
        }

        binding.btnProvider.setOnClickListener {
            userType = MemberModel.TYPE_SERVICE_PROVIDER
            selectType(userType == MemberModel.TYPE_CUSTOMER)
        }

    }


    private fun initData() {

        binding.tvUserName.text = user?.fullName
        Glide.with(this)
            .asBitmap()
            .load(user?.photoUrl)
            .placeholder(R.drawable.error_logo)
            .into(binding.userImage)


        if(type==MemberModel.TYPE_SERVICE_PROVIDER){
            getReview()
            binding.serviceProviderLy.visibility= View.VISIBLE
            binding.customerLy.visibility= View.GONE
            binding.providerEmailTv.text=user?.email
            binding.fullNameTv.text=user?.fullName
            binding.aboutTv.text=user?.description
            binding.mobilelTv.text=user?.mobile
            binding.startTv.text=user?.startTime
            binding.endTv.text=user?.endTime
        }
        else{
            binding.serviceProviderLy.visibility= View.GONE
            binding.customerLy.visibility= View.VISIBLE
            binding.customerEmailTv.text=user?.email
            binding.customerNameTv.text=user?.fullName

        }
    }

    private fun selectType(isCustomer: Boolean) {

        binding.tvCustomer.setTextColor(
            ContextCompat.getColor(
                this, if (isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcCustomer.setBackgroundColor(
            ContextCompat.getColor(
                this, if (isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.tvProvider.setTextColor(
            ContextCompat.getColor(
                this, if (!isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcProvider.setBackgroundColor(
            ContextCompat.getColor(
                this, if (!isCustomer) R.color.colorPrimary else R.color.gray6
            )
        )

        if (isCustomer) {

            if(type == MemberModel.TYPE_CUSTOMER){
                binding.customerLy.visibility=visible
                binding.serviceProviderLy.visibility=gone
            }
            else{
                binding.customerLy.visibility=gone
                binding.serviceProviderLy.visibility=visible
            }


            binding.reviewRecycler.visibility=gone


        } else {
            binding.reviewRecycler.visibility=visible
            binding.customerLy.visibility=gone
            binding.serviceProviderLy.visibility=gone

        }

    }


    private fun initReviewAdapter() {
        val reviewAdapter = ReviewAdapter(activity!!, reviewList)
        binding.reviewRecycler.adapter = reviewAdapter
    }


    private fun getReview(){

        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {


                if (func == Constants.SUCCESS) {

                    reviewList = obj as MutableList<ReviewModel?>?

                    if (reviewList?.isNotEmpty() == true) {
                        initReviewAdapter()
                    } else {
                        binding.noDataTv.visibility = visible
                    }
                }


            }
        }).getReviews(user?.email)
    }


}

