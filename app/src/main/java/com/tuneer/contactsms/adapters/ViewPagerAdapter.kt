package com.tuneer.contactsms.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 12-02-2023.
 */
class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(supportFragmentManager) {

    // declare arrayList to contain fragments and its title
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        // return a particular fragment page
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        // return the number of tabs
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence{
        // return title of the tab
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        // add each fragment and its title to the array list
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}