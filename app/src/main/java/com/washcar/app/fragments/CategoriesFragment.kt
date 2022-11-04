package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.washcar.app.R
import com.washcar.app.adapters.CategoriesAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.databinding.FragmentCategoriesBinding
import com.washcar.app.dialogs.AddCategoryDialog
import com.washcar.app.models.CategoryModel

/**
 * A simple [Fragment] subclass.
 */
class CategoriesFragment : FragmentBase() {

    var categoriesList: MutableList<CategoryModel>? = null

    var addCategoryDialog: AddCategoryDialog? = null

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.mainTitleTxt.text = getString(R.string.categories)
        binding.toolBar.homeBtn.visibility = gone

        getData(true)

        binding.btnAdd.setOnClickListener {
            showAddCategoryDialog()
        }

    }

    private fun initAdapter() {

        val adapter = CategoriesAdapter(requireActivity(), categoriesList,
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
            binding.lyData.visibility = gone
        }
        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                binding.lyLoading.loadingProgressLY.visibility = gone

                if (func == Constants.SUCCESS) {

                    binding.lyData.visibility = visible
                    categoriesList = obj as MutableList<CategoryModel>?

                    if (categoriesList?.isNotEmpty() == true) {
                        binding.lyEmpty.noDataLY.visibility = gone
                        binding.rv.visibility = visible
                        initAdapter()

                    } else {
                        binding.lyEmpty.noDataLY.visibility = visible
                        binding.lyEmpty.noDataTxt.text = getString(R.string.no_categories)
                        binding.rv.visibility = gone
                    }
                } else {
                    binding.lyFail.failGetDataLY.visibility = visible
                    binding.lyData.visibility = gone
                }


            }
        }).getCategories()
    }

    fun showAddCategoryDialog() {
        if (addCategoryDialog == null) {
            addCategoryDialog = AddCategoryDialog(requireActivity(), object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    val categoryName = obj as String
                    addCategoryToFireBase(categoryName)
                }
            })
            addCategoryDialog!!.setOnDismissListener {
                addCategoryDialog = null
            }
        }
    }

    fun addCategoryToFireBase(name: String) {

        GlobalData.progressDialog(
            activity,
            R.string.add_category,
            R.string.please_wait_sending
        )

        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()
                if (func == Constants.SUCCESS) {
                    addCategoryDialog?.dismiss()
                    GlobalData.successDialog(
                        activity,
                        R.string.add_category,
                        activity?.getString(R.string.success_add_category), null
                    )
                    getData(false)


                } else {
                    var message = obj as String?
                    if (message.isNullOrEmpty())
                        message = getString(R.string.fail_to_add_category)

                    GlobalData.errorDialog(
                        activity,
                        R.string.add_category,
                        message
                    )
                }


            }
        }).addCategory(name)


    }
}