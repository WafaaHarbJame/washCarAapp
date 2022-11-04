package com.washcar.app.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.washcar.app.R
import com.washcar.app.databinding.DialogConfirmSendBinding

class SuccessSendDialog(
    var activity: Activity?
) :
    Dialog(activity!!) {

    val dialog: SuccessSendDialog
        get() = this
    lateinit var binding: DialogConfirmSendBinding

    init {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogConfirmSendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog.window?.setGravity(Gravity.BOTTOM);
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation;


        binding.okBtn.setOnClickListener {

            dismiss()

        }


        try {
            if (activity != null && !activity!!.isFinishing) show()
        } catch (e: Exception) {
            dismiss()
        }


    }


}