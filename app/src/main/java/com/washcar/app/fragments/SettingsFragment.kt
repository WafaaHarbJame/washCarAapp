package com.washcar.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.washcar.app.R
import com.washcar.app.activities.LoginActivity
import com.washcar.app.activities.ProfileActivity
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentSettingsBinding
import com.washcar.app.dialogs.ChangePasswordDialog
import com.washcar.app.dialogs.MyConfirmDialog
import com.washcar.app.models.MemberModel


class SettingsFragment : FragmentBase() {

    private var changePasswordDialog: ChangePasswordDialog? = null
    lateinit var container: FrameLayout
    var confirmDialog: MyConfirmDialog? = null

    var user: MemberModel? = null
    var type: String = ""


    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = UtilityApp.userData
        type = user?.type ?: MemberModel.TYPE_CUSTOMER
        binding.toolBar.mainTitleTxt.text = getString(R.string.settings)
        user = UtilityApp.userData



        if (UtilityApp.isLogin) {
            if (user?.type == MemberModel.TYPE_ADMIN) {
                binding.profileBut.visibility = gone
                binding.passwordBtn.visibility = gone
            }
            binding.signOutIcon.text = getString(R.string.fal_sign_out)
            binding.signOutLabel.text = getString(R.string.sign_out)
        } else {
            binding.signOutIcon.text = getString(R.string.fal_sign_in)
            binding.signOutLabel.text = getString(R.string.sign_in)
        }


        binding.passwordBtn.setOnClickListener {

            if (changePasswordDialog == null) {
                changePasswordDialog = ChangePasswordDialog(requireActivity())
                changePasswordDialog!!.setOnDismissListener { changePasswordDialog = null }
            }
        }



        binding.profileBut.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)

        }



        binding.logoutBtn.setOnClickListener {

            showConfirmDialog()


        }
    }


    private fun showConfirmDialog() {
        if (confirmDialog == null) {
            val okClick = object : MyConfirmDialog.Click() {
                override fun click() {
                    var intent = Intent(requireActivity(), LoginActivity::class.java)

                    if (UtilityApp.isLogin) {
                        UtilityApp.logOut()
                        FirebaseAuth.getInstance().signOut()
                        intent = Intent(requireActivity(), LoginActivity::class.java)
                    }
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
            confirmDialog = MyConfirmDialog(
                requireActivity(),
                getString(R.string.want_signout),
                R.string.sign_out,
                R.string.cancel2,
                okClick,
                null
            )
            confirmDialog!!.setOnDismissListener {
                confirmDialog = null
            }
        }
    }

}
