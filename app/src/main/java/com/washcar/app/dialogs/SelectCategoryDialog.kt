package com.washcar.app.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.apiHandlers.DataFeacher
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.databinding.DialogSelectCategoryBinding
import com.washcar.app.models.CategoryModel

class SelectCategoryDialog(
    var activity: Activity,
    val providerEmail: String,
    var categoryModel: CategoryModel,
    val dataFetcherCallBack: DataFetcherCallBack?
) : Dialog(activity) {

    val dialog: SelectCategoryDialog
        get() = this

    var binding: DialogSelectCategoryBinding

    init {

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogSelectCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCategoryName.text = categoryModel.name ?: ""
        binding.etCategoryPrice.setText(categoryModel.price?.toString() ?: "")
        binding.swEnable.isChecked = categoryModel.selected ?: false

        binding.btnSave.setOnClickListener {

            addCategory()

        }


        try {
            if (!activity.isFinishing) show()
        } catch (e: Exception) {
            dismiss()
        }


    }

    private fun addCategory() {
        try {


            val priceStr = NumberHandler.arabicToDecimal(binding.etCategoryPrice.text.toString())

            if (priceStr.isEmpty()) {
                binding.inputCategoryPrice.error = activity.getString(R.string.invalid_input)
                return
            }

            val price = priceStr.toDoubleOrNull() ?: 0.0
            addCategoryToFireBase(price)

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun addCategoryToFireBase(price: Double) {

        categoryModel.price = price
        categoryModel.selected = binding.swEnable.isChecked

        GlobalData.progressDialog(
            activity, R.string.add_category, R.string.please_wait_sending
        )

        DataFeacher(object : DataFetcherCallBack {
            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
                GlobalData.progressDialogHide()
                if (func == Constants.SUCCESS) {
                    dismiss()
                    dataFetcherCallBack?.Result(Constants.SUCCESS, "", true)

                } else {
                    var message = obj as String?
                    if (message.isNullOrEmpty()) message =
                        activity.getString(R.string.fail_to_add_category)

                    GlobalData.errorDialog(
                        activity, R.string.add_category, message
                    )
                }


            }
        }).editProviderCategory(providerEmail, categoryModel)


    }


}