package com.washcar.app.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.washcar.app.databinding.DialogAddRateBinding

class AddRateDialog(
    context: Context?,
    message: String?,
    okStr: Int,
    cancelStr: Int,
    okCall: Click?,
    cancelCall: Click?
) :
        Dialog(context!!) {
    var activity: Activity?
    var binding: DialogAddRateBinding


    abstract class Click {

        abstract fun click()
    }

    init {
        activity = context as Activity?
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE) //before
        binding = DialogAddRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)
        setCancelable(true)
        setCancelable(false)
        binding.yesBtn.setOnClickListener { okCall?.click() }
        binding.noBtn.setOnClickListener {
            cancelCall?.click()
            dismiss()
        }
        try {
            if (activity != null && !activity!!.isFinishing) show()
        } catch (e: Exception) {
            dismiss()
        }
    }
}