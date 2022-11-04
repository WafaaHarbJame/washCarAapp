package com.washcar.app

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.washcar.app.activities.ActivityBase
import com.washcar.app.classes.GlobalData
import com.washcar.app.databinding.ActivityMainBottomNavBinding
import com.washcar.app.fragments.*
import com.washcar.app.models.MemberModel
import com.washcar.app.models.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : ActivityBase() {
    private var gui_position = 0
    lateinit var binding: ActivityMainBottomNavBinding

    private var mTitle: CharSequence? = null
    private var fragmentManager: FragmentManager? = null
    private var ft: FragmentTransaction? = null
    private var newFragment: Fragment? = null

    private lateinit var tabTextArr: Array<TextView>
    private lateinit var tabIconsArr: Array<TextView>
    private var userType: Int = 0
    var user: MemberModel? = null
    private var toOrder: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBottomNavBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        isMainActivityBottomNav = true
        mTitle = getString(com.washcar.app.R.string.app_name)

        tabTextArr = arrayOf(binding.botoomNav.tab1Txt, binding.botoomNav.tab2Txt, binding.botoomNav.tab3Txt)
        tabIconsArr = arrayOf(binding.botoomNav.tab1Icon, binding.botoomNav.tab2Icon, binding.botoomNav.tab3Icon)



       // user = UtilityApp.userData

//        userType = user?.type?:0

        selectBottomTab( R.id.mainBtn)



        initListeners()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

//        val bundle = intent?.extras
//
//        if (bundle != null && bundle.containsKey(Constants.KEY_TO_ORDERS)) {
//            toOrder = bundle.getBoolean(Constants.KEY_TO_ORDERS)
//        }
//
//        if (toOrder) {
//            selectBottomTab(com.washcar.app.R.id.ordersBtn)
//        } else {
//            selectBottomTab(com.washcar.app.R.id.mainBtn)
//        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initListeners() {
        binding.botoomNav.mainBtn.setOnClickListener {
            selectBottomTab(
                 R.id.mainBtn
            )
        }

        binding.botoomNav.ordersBtn.setOnClickListener {
            selectBottomTab(
                 R.id.ordersBtn
            )
        }
        binding.botoomNav.settingsBtn.setOnClickListener {
            selectBottomTab(
                 R.id.settingsBtn
            )
        }
    }


    private fun selectBottomTab(resId: Int) {
        when (resId) {
             R.id.mainBtn -> {
                newFragment = when (userType) {
                    2 -> {
                        DriversFragment()
                    }
                    1 -> {
                        HomeDriverFragment()
                    }
                    else -> {
                        HomeClientFragment()
                    }
                }
                gui_position = 0
                mTitle = getString( R.string.home)
            }
             R.id.ordersBtn -> {

                newFragment = if (userType == 1) {
                    AllDriverRequestsFragment()
                } else {
                    AllDriverRequestsFragment()

                }
                gui_position = 1
                mTitle = getString( R.string.all_orders)
            }


             R.id.settingsBtn -> {
                newFragment =
                    SettingsFragment()
                gui_position = 2
                mTitle = getString( R.string.profile)
            }
        }
        changeColor(gui_position)

        if (newFragment != null) {
            fragmentManager = supportFragmentManager
            ft = fragmentManager!!.beginTransaction()
            ft!!.replace( R.id.container, newFragment!!).commitNowAllowingStateLoss()
        }

    }

    private fun changeColor(pos: Int) {
        for (i in tabTextArr.indices) {
            if (i == pos) {
                tabTextArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                         R.color.bottomNavActive
                    )
                )
                tabIconsArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                         R.color.bottomNavActive
                    )
                )

            } else {
                tabIconsArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                         R.color.bottomNavInactive
                    )
                )
                tabTextArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                         R.color.bottomNavInactive
                    )
                )
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (gui_position == 0) {
                if (GlobalData.Position == 1) {
                    EventBus.getDefault()
                        .post( MessageEvent(MessageEvent.TYPE_PAGER, 0))
                    return false
                } else {
                   onBackPressedDispatcher.onBackPressed()
                }
            } else {
                selectBottomTab( R.id.mainBtn)
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        if (event.type ==  MessageEvent.TYPE_POSITION) {
            val pos = event.data as Int
            if (pos == 0) selectBottomTab( R.id.mainBtn)
        }
    }



}