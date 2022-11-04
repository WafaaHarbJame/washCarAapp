package com.washcar.app.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.washcar.app.R
import com.washcar.app.Utils.NumberHandler
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.databinding.DialogAddCategoryBinding

class AddCategoryDialog(
    var activity: Activity?,
    val dataFetcherCallBack: DataFetcherCallBack?
) :
    Dialog(activity!!) {

    val dialog: AddCategoryDialog
        get() = this

    lateinit var binding: DialogAddCategoryBinding

    init {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        dialog.window?.setGravity(Gravity.BOTTOM)
//        dialog.window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation;


        binding.btnSave.setOnClickListener {

            addCategory()

        }


        try {
            if (activity != null && !activity!!.isFinishing) show()
        } catch (e: Exception) {
            dismiss()
        }


    }

    private fun addCategory() {
        try {


            val categoryStr =
                NumberHandler.arabicToDecimal(binding.tvCategoryName.text.toString())

            if (categoryStr.isEmpty()) {
                binding.inputCategory.error = activity?.getString(R.string.invalid_input)
                return
            }


            dataFetcherCallBack?.Result(categoryStr, "", true)


        } catch (e: Exception) {

            e.printStackTrace()

        }
    }


}