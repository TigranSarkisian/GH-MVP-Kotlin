package com.sarkisian.gh.ui.repos

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.sarkisian.gh.R
import com.sarkisian.gh.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoListFragmentTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testRecyclerViewDisplayed() {
        onView(withId(R.id.rv_repo_list)).check(matches(isDisplayed()))
    }

    @Test
    fun testScrollRecycleView() {
        onView(withId(R.id.rv_repo_list))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(20), click())
                .perform(scrollToPosition<RecyclerView.ViewHolder>(0), click())
                .perform(scrollToPosition<RecyclerView.ViewHolder>(15), click())
    }
}