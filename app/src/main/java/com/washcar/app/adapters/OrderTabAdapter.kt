package com.washcar.app.adapters

import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.washcar.app.R
import com.washcar.app.classes.Constants
import com.washcar.app.fragments.*
import com.washcar.app.models.RequestModel


class OrderTabAdapter(val mContext: Context, fm: FragmentManager?) :
        FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val TAB_TITLES =intArrayOf(R.string.on_going, R.string.complete)

    private val fragments: SparseArray<Fragment> = SparseArray()
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        if (position == 0) {
            fragment = OrdersListFragment()
            val bundle = Bundle()
            bundle.putString(Constants.KEY_REQUEST_TYPE, RequestModel.STATUS_UPCOMING)
            fragment.setArguments(bundle)
        }
        if (position == 1) {
            fragment = OrdersListFragment()
            val bundle = Bundle()
            bundle.putString(Constants.KEY_REQUEST_TYPE, RequestModel.STATUS_FINISHED)
            fragment.setArguments(bundle)
        }



        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence{
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }


    fun getCurrentFragment(index: Int): Fragment? {
        return if (fragments.size() > 0) fragments[index] else null
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        fragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        fragments.remove(position)
        super.destroyItem(container, position, `object`)
    }






}