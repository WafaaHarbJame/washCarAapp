package com.washcar.app

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.washcar.app.models.MessageEvent
import com.washcar.app.activities.ActivityBase
import com.washcar.app.classes.Constants
import com.washcar.app.classes.GlobalData
import com.washcar.app.classes.UtilityApp
import com.washcar.app.fragments.*
import com.washcar.app.models.MemberModel
import kotlinx.android.synthetic.main.layout_bottom_nav.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : ActivityBase() {
    //    @BindView(R2.id.user) EditText username;
    private var gui_position = 0

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
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
        setContentView(com.washcar.app.R.layout.activity_main_bottom_nav)

        isMainActivityBottomNav = true
        mTitle = getString(com.washcar.app.R.string.app_name)

        tabTextArr = arrayOf(tab1Txt, tab2Txt, tab3Txt, tab4Txt)
        tabIconsArr = arrayOf(tab1Icon, tab2Icon, tab3Icon, tab4Icon)

        val bundle = intent?.extras

        if (bundle != null && bundle.containsKey(Constants.KEY_TO_ORDERS)) {
            toOrder = bundle.getBoolean(Constants.KEY_TO_ORDERS)
        }

        user = UtilityApp.userData

        userType = user?.type!!

        // getData()


        when (userType) {
            1 -> {
                finishOrderBtn.visibility = visible
                ordersBtn.visibility = gone
            }
            2 -> {
                finishOrderBtn.visibility = gone
                ordersBtn.visibility = gone
                tab2Txt.text = getString(com.washcar.app.R.string.all_orders)
            }
            3 -> {
                finishOrderBtn.visibility = gone
                ordersBtn.visibility = gone
            }
        }

        if (toOrder) {
            selectBottomTab(com.washcar.app.R.id.ordersBtn)
        } else {
            selectBottomTab(com.washcar.app.R.id.mainBtn)
        }

        initListeners()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val bundle = intent?.extras

        if (bundle != null && bundle.containsKey(Constants.KEY_TO_ORDERS)) {
            toOrder = bundle.getBoolean(Constants.KEY_TO_ORDERS)
        }

        if (toOrder) {
            selectBottomTab(com.washcar.app.R.id.ordersBtn)
        } else {
            selectBottomTab(com.washcar.app.R.id.mainBtn)
        }
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
        mainBtn.setOnClickListener {
            selectBottomTab(
                com.washcar.app.R.id.mainBtn
            )
        }
        finishOrderBtn.setOnClickListener {
            selectBottomTab(
                com.washcar.app.R.id.finishOrderBtn
            )
        }
        ordersBtn.setOnClickListener {
            selectBottomTab(
                com.washcar.app.R.id.ordersBtn
            )
        }
        settingsBtn.setOnClickListener {
            selectBottomTab(
                com.washcar.app.R.id.settingsBtn
            )
        }
    }


    private fun selectBottomTab(resId: Int) {
        when (resId) {
            com.washcar.app.R.id.mainBtn -> {
                newFragment = when (userType) {
                    3 -> {
                        DriversFragment()
                    }
                    2 -> {
                        HomeDriverFragment()
                    }
                    else -> {
                        HomeClientFragment()
                    }
                }
                gui_position = 0
                mTitle = getString(com.washcar.app.R.string.home)
            }
            com.washcar.app.R.id.ordersBtn -> {

                newFragment = if (userType == 1) {
                    AllDriverRequestsFragment()
                } else {
                    AllDriverRequestsFragment()

                }
                gui_position = 1
                mTitle = getString(com.washcar.app.R.string.all_orders)
            }

            com.washcar.app.R.id.finishOrderBtn -> {
                newFragment = if (userType == 1) {
                    FinishedClientFragment()
                } else {
                    FinishedDriveragment()

                }
                gui_position = 2
                mTitle = getString(com.washcar.app.R.string.finshed_order)
            }

            com.washcar.app.R.id.settingsBtn -> {
                newFragment =
                    SettingsFragment()
                gui_position = 3
                mTitle = getString(com.washcar.app.R.string.profile)
            }
        }
        changeColor(gui_position)

        if (newFragment != null) {
            fragmentManager = supportFragmentManager
            ft = fragmentManager!!.beginTransaction()
            ft!!.replace(com.washcar.app.R.id.container, newFragment!!).commitNowAllowingStateLoss()
        }

    }

    private fun changeColor(pos: Int) {
        for (i in tabTextArr.indices) {
            if (i == pos) {
                tabTextArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                        com.washcar.app.R.color.bottomNavActive
                    )
                )
                tabIconsArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                        com.washcar.app.R.color.bottomNavActive
                    )
                )

            } else {
                tabIconsArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                        com.washcar.app.R.color.bottomNavInactive
                    )
                )
                tabTextArr[i].setTextColor(
                    ContextCompat.getColor(
                        getActiviy(),
                        com.washcar.app.R.color.bottomNavInactive
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
                        .post(com.washcar.app.models.MessageEvent(MessageEvent.TYPE_PAGER, 0))
                    return false
                } else {
                    onBackPressed()
                }
            } else {
                selectBottomTab(com.washcar.app.R.id.mainBtn)
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
    fun onMessageEvent(event: com.washcar.app.models.MessageEvent) {
        if (event.type == com.washcar.app.models.MessageEvent.TYPE_POSITION) {
            val pos = event.data as Int
            if (pos == 0) selectBottomTab(com.washcar.app.R.id.mainBtn)
        }
    }


//    private fun getData() {
//        var allDrivesList: MutableList<RegisterUserModel>? = null
//
//        DataFeacher(object : DataFetcherCallBack {
//            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//
//                if (func == Constants.SUCCESS) {
//                    allDrivesList = obj as MutableList<RegisterUserModel>?
//                    Log.i("TAG", "Log getAllAccount"+ allDrivesList!![0].mobileWithCountry)
//
//                }
//
//            }
//        }).getAllAccount()
//    }

}