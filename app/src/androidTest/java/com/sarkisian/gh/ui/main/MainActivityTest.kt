package com.sarkisian.gh.ui.main

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.sarkisian.gh.R
import com.sarkisian.gh.ui.repos.RepoListFragment
import com.sarkisian.gh.util.extensions.addFragment
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        activityRule.activity.addFragment(
                RepoListFragment.newInstance(),
                R.id.fl_main_container
        )
    }

    @Test
    fun testIsFragmentAttached() {
        val fragment = activityRule.activity.supportFragmentManager
                .findFragmentById(R.id.fl_main_container)
        assertTrue(fragment?.isAdded!!)
    }

}