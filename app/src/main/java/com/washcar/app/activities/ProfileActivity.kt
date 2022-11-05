package com.washcar.app.activities

import android.content.Intent
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

    var user: MemberModel? = null

    private var carWashModel: MemberModel? = null
    var reviewList: MutableList<ReviewModel?>? = null

    val PROFILE = "profile"
    val REVIEWS = "reviews"

    var tabType: String? = PROFILE

    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.mainTitleTxt.text = getString(R.string.profile)


        binding.toolBar.homeBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        user = UtilityApp.userData

        val bundle = intent.extras
        if (bundle != null) {
            carWashModel = bundle.getSerializable(Constants.key_provider_data) as MemberModel?

        }

//        if (showProfile) {
//            binding.editProfile.visibility = gone
//            initServiceData(carWashModel)
//        }


        val reviewManger = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.reviewRecycler.layoutManager = reviewManger
        binding.reviewRecycler.setHasFixedSize(true)

//        initReviewAdapter()

        initListeners()

        initData()

    }

    private fun initServiceData(carWashModel: MemberModel?) {
        getReview()
        binding.serviceProviderLy.visibility = View.VISIBLE
        binding.customerLy.visibility = View.GONE
        binding.providerEmailTv.text = carWashModel?.email
        binding.fullNameTv.text = carWashModel?.fullName
        binding.aboutTv.text = carWashModel?.description
        binding.mobilelTv.text = carWashModel?.mobile
        binding.tvTime.text = carWashModel?.startTime?.plus(" > ${carWashModel.endTime}")

    }

    private fun initListeners() {


        binding.btnCustomer.setOnClickListener {
            tabType = PROFILE
            selectType(true)
        }

        binding.btnReview.setOnClickListener {
            tabType = REVIEWS
            selectType(false)
        }

        binding.requestBtn.setOnClickListener {
            val intent = Intent(this, RequestCarActivity::class.java)
            intent.putExtra(Constants.key_provider_data, carWashModel)
            intent.putExtra(Constants.KEY_EMAIL, carWashModel?.email)
            startActivity(intent)
        }

    }


    private fun initData() {

        binding.tvUserName.text = carWashModel?.fullName


        binding.customerEmailTv.text = carWashModel?.email
        binding.customerNameTv.text = carWashModel?.fullName

        if (user?.email == carWashModel?.email) {
//            binding.editProfile.visibility = visible
        } else {
//            binding.editProfile.visibility = gone
        }

        if (carWashModel?.type == MemberModel.TYPE_SERVICE_PROVIDER) {
            getReview()
            binding.serviceProviderLy.visibility = View.VISIBLE
            binding.customerLy.visibility = View.GONE
            binding.providerEmailTv.text = carWashModel?.email
            binding.fullNameTv.text = carWashModel?.fullName
            binding.aboutTv.text = carWashModel?.description
            binding.mobilelTv.text = carWashModel?.mobile
            binding.tvTime.text = carWashModel?.startTime?.plus(" > ${carWashModel?.endTime}")
            binding.userImage.visibility = visible
            Glide.with(this)
                .asBitmap()
                .load(carWashModel?.photoUrl)
                .placeholder(R.drawable.error_logo)
                .into(binding.userImage)

            binding.btnReview.visibility = visible
        } else {
            binding.serviceProviderLy.visibility = View.GONE
            binding.customerLy.visibility = View.VISIBLE
            binding.customerEmailTv.text = carWashModel?.email
            binding.customerNameTv.text = carWashModel?.fullName
            binding.userImage.visibility = gone
            binding.btnReview.visibility = gone
        }
    }

    private fun selectType(isProfile: Boolean) {

        binding.tvCustomer.setTextColor(
            ContextCompat.getColor(
                this, if (isProfile) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcCustomer.setBackgroundColor(
            ContextCompat.getColor(
                this, if (isProfile) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.tvReview.setTextColor(
            ContextCompat.getColor(
                this, if (!isProfile) R.color.colorPrimary else R.color.gray6
            )
        )
        binding.indcReviews.setBackgroundColor(
            ContextCompat.getColor(
                this, if (!isProfile) R.color.colorPrimary else R.color.gray6
            )
        )

        if (isProfile) {

            if (carWashModel?.type == MemberModel.TYPE_CUSTOMER) {
                binding.customerLy.visibility = visible
                binding.serviceProviderLy.visibility = gone
            } else {
                binding.customerLy.visibility = gone
                binding.serviceProviderLy.visibility = visible
            }

            binding.reviewRecycler.visibility = gone


        } else {
            binding.reviewRecycler.visibility = visible
            binding.customerLy.visibility = gone
            binding.serviceProviderLy.visibility = gone

        }

    }


    private fun initReviewAdapter() {
        val reviewAdapter = ReviewAdapter(this, reviewList)
        binding.reviewRecycler.adapter = reviewAdapter
    }


    private fun getReview() {

        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {


                if (func == Constants.SUCCESS) {

                    reviewList = obj as MutableList<ReviewModel?>?

                    if (reviewList?.isNotEmpty() == true) {
                        binding.noDataTv.visibility = gone
                        initReviewAdapter()
                    } else {
                        binding.noDataTv.visibility = visible
                    }
                }


            }
        }).getReviews(carWashModel?.email)
    }


}

