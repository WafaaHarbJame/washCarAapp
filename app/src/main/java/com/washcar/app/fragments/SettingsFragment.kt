package com.washcar.app.fragments

import android.app.Activity
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
import com.washcar.app.activities.AboutAppActivity
import com.washcar.app.activities.LoginActivity
import com.washcar.app.activities.ProfileActivity
import com.washcar.app.classes.UtilityApp
import com.washcar.app.dialogs.ChangePasswordDialog
import com.washcar.app.models.MemberModel
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.tool_bar.*


class SettingsFragment : FragmentBase() {
    var activity: Activity? = null

    private var changePasswordDialog: ChangePasswordDialog? = null
    lateinit var container: FrameLayout

    var user: MemberModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        this.container = container as FrameLayout

        activity = getActivity()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()

        user = UtilityApp.userData

        mainTitleTxt.text = getString(R.string.settings)
        homeBtn.visibility = gone


        if (UtilityApp.isLogin) {
            if (user?.type == 3) {
                profileBut.visibility = gone
            }
            signOutIcon.text = getString(R.string.fal_sign_out)
            signOutLabel.text = getString(R.string.sign_out)
        } else {
            signOutIcon.text = getString(R.string.fal_sign_in)
            signOutLabel.text = getString(R.string.sign_in)
        }


        passwordBtn.setOnClickListener {

            if (changePasswordDialog == null) {
                changePasswordDialog = ChangePasswordDialog(requireActivity())
                changePasswordDialog!!.setOnDismissListener { changePasswordDialog = null }
            }
        }

        ratingBtn.setOnClickListener {

            ActivityHandler.OpenGooglePlay(requireActivity())

        }


        profileBut.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)

        }

        aboutAppBtn.setOnClickListener {

            val intent = Intent(requireActivity(), AboutAppActivity::class.java)
            startActivity(intent)

        }

        logoutBtn.setOnClickListener {

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


    override fun onResume() {
        super.onResume()
        activity = getActivity()
    }


}
