package com.washcar.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.adapters.ReviewAdapter
import com.washcar.app.databinding.FragmentReviewBinding
import com.washcar.app.models.ReviewModel


class ReviewFragment : Fragment() {
    var reviewList: MutableList<ReviewModel?>? = null


    private lateinit var binding: FragmentReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)

        reviewList= mutableListOf()

        reviewList?.add(ReviewModel())
        reviewList?.add(ReviewModel())
        reviewList?.add(ReviewModel())
        reviewList?.add(ReviewModel())

        val reviewManger = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.reviewRecycler.layoutManager = reviewManger
        binding.reviewRecycler.setHasFixedSize(true)

        initReviewAdapter()


        return  binding.root
    }


    private fun initReviewAdapter() {
        val reviewAdapter = ReviewAdapter(requireContext(), reviewList)
        binding.reviewRecycler.adapter = reviewAdapter
    }

}