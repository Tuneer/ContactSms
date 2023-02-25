package com.tuneer.contactsms.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tuneer.contactsms.R
import com.tuneer.contactsms.adapters.ViewPagerAdapter
import com.tuneer.contactsms.databinding.ActivityMainBinding
import com.tuneer.contactsms.fragments.FirstFragment
import com.tuneer.contactsms.fragments.SecondFragment
import com.tuneer.contactsms.helper.UtilFile

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var slidingtab: TabLayout
    private lateinit var pager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        slidingtab = findViewById(R.id.sliding_tab)
        pager = findViewById(R.id.viewPager)
        // Initializing the ViewPagerAdapter
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // add fragment to the list
        adapter.addFragment(FirstFragment(), "Contact List")
        adapter.addFragment(SecondFragment(), "Message List")

        // Adding the Adapter to the ViewPager
        pager.adapter = adapter
        // bind the viewPager with the TabLayout.
        slidingtab.setupWithViewPager(pager)

        //Listener attach to tabs
        getTabsListener()


    }


    private fun getTabsListener() {
        slidingtab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        // Toast.makeText(this@MainActivity, "Contact", Toast.LENGTH_SHORT).show();
                        commonChangeTabBackground(R.drawable.ic_left_toggle)
                    }

                    1 -> {
                        //Toast.makeText(this@MainActivity, "Message", Toast.LENGTH_SHORT).show();
                        commonChangeTabBackground(R.drawable.ic_right_toggle)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun commonChangeTabBackground(drawable: Int) {
        slidingtab.background = ResourcesCompat.getDrawable(
            resources,
            drawable,
            null
        )
    }

    override fun onResume() {
        super.onResume()
        if (!UtilFile.checkForInternet(this)) {
            Toast.makeText(this, R.string.disconnected_msg, Toast.LENGTH_SHORT).show()
        }
        selectTabContact()

    }

    private fun selectTabContact() {
        //selecting the tab for the first time when main load infront of user.
        slidingtab.selectTab(slidingtab.getTabAt(0))
        commonChangeTabBackground(R.drawable.ic_left_toggle)
    }


}