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
import com.washcar.app.classes.UtilityApp
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

    //    private var userType: Int = 0
    var user: MemberModel? = null
    private var toOrder: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBottomNavBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        isMainActivityBottomNav = true
        mTitle = getString(com.washcar.app.R.string.app_name)

        user = UtilityApp.userData

//        tabTextArr = if (user?.type == MemberModel.TYPE_ADMIN)
//            arrayOf(binding.botoomNav.tab1Txt, binding.botoomNav.tab3Txt)
//        else

        tabTextArr =
            arrayOf(binding.botoomNav.tab1Txt, binding.botoomNav.tab2Txt, binding.botoomNav.tab3Txt)

//        tabIconsArr = if (user?.type == MemberModel.TYPE_ADMIN)
//            arrayOf(
//                binding.botoomNav.tab1Icon,
//                binding.botoomNav.tab3Icon
//            )
//        else
        tabIconsArr = arrayOf(
            binding.botoomNav.tab1Icon,
            binding.botoomNav.tab2Icon,
            binding.botoomNav.tab3Icon
        )

        if (user?.type == MemberModel.TYPE_ADMIN)
            binding.botoomNav.tab2Txt.text = getString(R.string.categories)
        else if (user?.type == MemberModel.TYPE_SERVICE_PROVIDER)
            binding.botoomNav.tab2Txt.text = getString(R.string.my_categories)
        else
            binding.botoomNav.tab2Txt.text = getString(R.string.all_orders)

        selectBottomTab(R.id.mainBtn)

        initListeners()

//        if (UtilityApp.isLogin)
//            getMyProfile()
    }

//    fun getMyProfile() {
//
//        DataFeacher(object : DataFetcherCallBack {
//            override fun Result(obj: Any?, func: String?, IsSuccess: Boolean) {
//                if (func == Constants.SUCCESS) {
//                    user = obj as MemberModel
//                    UtilityApp.userData = user
//                }
//            }
//        }).getMyAccount(user?.email)
//    }

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
                newFragment =
                    when (user?.type) {
                        MemberModel.TYPE_ADMIN -> {
                            MainAdminFragment()
                        }
                        MemberModel.TYPE_SERVICE_PROVIDER -> {
                            HomeDriverFragment()
                        }
                        else -> {
                            HomeClientFragment()
                        }
                    }
                gui_position = 0
                mTitle = getString(R.string.home)
            }
            R.id.ordersBtn -> {

                newFragment = when (user?.type) {
                    MemberModel.TYPE_SERVICE_PROVIDER -> {
                        ProviderCategoriesFragment()
                    }
                    MemberModel.TYPE_ADMIN -> {
                        CategoriesFragment()
                    }
                    else -> {
                        RequestsFragment()
                    }
                }
                gui_position = 1
            }


            R.id.settingsBtn -> {
                newFragment =
                    SettingsFragment()
                gui_position = if (user?.type == MemberModel.TYPE_ADMIN) 1 else 2
                mTitle = getString(R.string.profile)
            }
        }
        changeColor(gui_position)

        if (newFragment != null) {
            fragmentManager = supportFragmentManager
            ft = fragmentManager!!.beginTransaction()
            ft!!.replace(R.id.container, newFragment!!).commitNowAllowingStateLoss()
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
                        .post(MessageEvent(MessageEvent.TYPE_PAGER, 0))
                    return false
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            } else {
                selectBottomTab(R.id.mainBtn)
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
        if (event.type == MessageEvent.TYPE_POSITION) {
            val pos = event.data as Int
            if (pos == 0) selectBottomTab(R.id.mainBtn)
        }
    }


}