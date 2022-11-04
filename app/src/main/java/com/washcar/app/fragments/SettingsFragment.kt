package com.washcar.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.washcar.app.R
import com.washcar.app.SplashScreen
import com.washcar.app.Utils.ActivityHandler
import com.washcar.app.activities.LoginActivity
import com.washcar.app.activities.ProfileActivity
import com.washcar.app.classes.UtilityApp
import com.washcar.app.databinding.FragmentSettingsBinding
import com.washcar.app.dialogs.ChangePasswordDialog
import com.washcar.app.models.MemberModel


class SettingsFragment : FragmentBase() {

    private var changePasswordDialog: ChangePasswordDialog? = null
    lateinit var container: FrameLayout

    var user: MemberModel? = null

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = UtilityApp.userData

        binding.toolBar.mainTitleTxt.text = getString(R.string.settings)
        binding.toolBar.homeBtn.visibility = gone


        if (UtilityApp.isLogin) {
            if (user?.type == MemberModel.TYPE_ADMIN) {
                binding.profileBut.visibility = gone
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

        binding.ratingBtn.setOnClickListener {

            ActivityHandler.OpenGooglePlay(requireActivity())

        }


        binding.profileBut.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)

        }



        binding.logoutBtn.setOnClickListener {

            var intent = Intent(requireActivity(), LoginActivity::class.java)

            if (UtilityApp.isLogin) {
                UtilityApp.logOut()
                FirebaseAuth.getInstance().signOut()
                intent = Intent(requireActivity(), SplashScreen::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }
    }


}
