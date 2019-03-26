package com.sarkisian.gh.ui.main


import android.os.Bundle
import com.sarkisian.gh.R
import com.sarkisian.gh.ui.base.BaseActivity
import com.sarkisian.gh.ui.favorites.RepoFavoritesFragment
import com.sarkisian.gh.ui.repos.RepoListFragment
import com.sarkisian.gh.ui.search.RepoSearchFragment
import com.sarkisian.gh.util.extensions.switchFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override val layoutResource: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBarTitle(getString(R.string.app_name))
        showActionBarHomeButton(false)

        if (savedInstanceState == null)
            switchFragment(fragment = RepoListFragment.newInstance(), frameId = R.id.fl_main_container)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list -> {
                    switchFragment(fragment = RepoListFragment.newInstance(), frameId = R.id.fl_main_container)
                    setActionBarTitle(getString(R.string.screen_list))
                }
                R.id.favorites -> {
                    switchFragment(fragment = RepoFavoritesFragment.newInstance(), frameId = R.id.fl_main_container)
                    setActionBarTitle(getString(R.string.screen_favorites))
                }
                R.id.search -> {
                    switchFragment(fragment = RepoSearchFragment.newInstance(), frameId = R.id.fl_main_container)
                    setActionBarTitle(getString(R.string.screen_search))
                }
            }
            true
        }
    }

}

