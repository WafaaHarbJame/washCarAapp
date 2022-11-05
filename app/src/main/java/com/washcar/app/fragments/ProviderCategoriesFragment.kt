package com.washcar.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.washcar.app.R
import com.washcar.app.adapters.ProviderCategoriesAdapter
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentProviderCategoriesBinding
import com.washcar.app.dialogs.SelectCategoryDialog
import com.washcar.app.models.CategoryModel
import com.washcar.app.models.MemberModel

/**
 * A simple [Fragment] subclass.
 */
class ProviderCategoriesFragment : FragmentBase() {

    var categoriesList: MutableList<CategoryModel?>? = null
    var providerCategoriesList: MutableList<CategoryModel>? = null

    var user: MemberModel? = null

    var selectCategoryDialog: SelectCategoryDialog? = null

    private var _binding: FragmentProviderCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProviderCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.mainTitleTxt.text = getString(R.string.categories)
        binding.toolBar.homeBtn.visibility = gone

        user = UtilityApp.userData

        getCategories()
        getProviderCategories(true)

    }

    private fun initAdapter() {

        val adapter = ProviderCategoriesAdapter(requireActivity(), categoriesList,
            object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                    val categoryModel = obj as CategoryModel
                    showEditCategoryDialog(categoryModel)
                }
            })
        binding.rv.adapter = adapter
    }


    private fun getProviderCategories(loading: Boolean) {
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
                    providerCategoriesList = obj as MutableList<CategoryModel>?

                    if (categoriesList?.isNotEmpty() == true) {
                        binding.lyEmpty.noDataLY.visibility = gone
                        binding.rv.visibility = visible
                        categoriesList?.forEach { mainCategory ->
                            val providerCategoryModel = providerCategoriesList?.firstOrNull {
                                it.id == mainCategory?.id
                            }
                            mainCategory?.price = providerCategoryModel?.price
                            mainCategory?.selected = providerCategoryModel?.selected
                        }
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
        }).getProviderCategories(user?.email ?: "")
    }

    private fun getCategories() {

        categoriesList = UtilityApp.getCategoriesList()
        if (categoriesList == null) {
            DataFeacher(object : DataFetcherCallBack {
                override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {

                    if (func == Constants.SUCCESS) {

                        categoriesList = obj as MutableList<CategoryModel?>?

                    }

                }
            }).getCategories()
        }
    }


    fun showEditCategoryDialog(categoryModel: CategoryModel) {
        if (selectCategoryDialog == null) {
            selectCategoryDialog = SelectCategoryDialog(
                requireActivity(),
                user?.email ?: "",
                categoryModel,
                object : DataFetcherCallBack {
                    override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                        getProviderCategories(false)
                    }
                })
            selectCategoryDialog!!.setOnDismissListener {
                selectCategoryDialog = null
            }
        }
    }

//    fun editProviderCategory(name: String) {
//
//        GlobalData.progressDialog(
//            activity,
//            R.string.add_category,
//            R.string.please_wait_sending
//        )
//
//        DataFeacher(object : DataFetcherCallBack {
//            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                GlobalData.progressDialogHide()
//                if (func == Constants.SUCCESS) {
//                    selectCategoryDialog?.dismiss()
//                    GlobalData.successDialog(
//                        activity,
//                        R.string.add_category,
//                        activity?.getString(R.string.success_add_category), null
//                    )
//                    getData(false)
//
//
//                } else {
//                    var message = obj as String?
//                    if (message.isNullOrEmpty())
//                        message = getString(R.string.fail_to_add_category)
//
//                    GlobalData.errorDialog(
//                        activity,
//                        R.string.add_category,
//                        message
//                    )
//                }
//
//
//            }
//        }).editProviderCategory(name)
//
//
//    }
}