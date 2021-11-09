package com.dave.survey

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView

class Formatter {

    private lateinit var context: Context

    fun customBottomNavigation(context1: Context){

        context = context1

        val bottomNavigation = (context1 as Activity).findViewById<View>(R.id.bottom_navigation)
        val bottomNavigationView : BottomNavigationView = bottomNavigation.findViewById(R.id.bottom_navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

    }

    private var navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(context, Home::class.java)
                    context.startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_add_beacons -> {

                        val intent = Intent(context, AddBeacons::class.java)
                        context.startActivity(intent)
                        return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_view_beacons -> {
                    val intent = Intent(context, ViewBeacons::class.java)
                    context.startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_index_card -> {
                    val intent = Intent(context, IndexCard::class.java)
                    context.startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

}