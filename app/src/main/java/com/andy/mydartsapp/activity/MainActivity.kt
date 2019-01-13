package com.andy.mydartsapp.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import com.andy.mydartsapp.R
import com.andy.mydartsapp.fragment.HomeFragment
import com.andy.mydartsapp.fragment.MoreFragment
import com.andy.mydartsapp.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = OnNavigationItemSelectedListener {item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                title = "Home"
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                title = "Profile"
                val prolfileFragment = ProfileFragment.newInstance()
                openFragment(prolfileFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                title = "More"
                val moreFragment = MoreFragment.newInstance()
                openFragment(moreFragment)
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                return@OnNavigationItemSelectedListener false
            }
        }
    }

    private fun openFragment(fragment: android.support.v4.app.Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout_container, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }
}
