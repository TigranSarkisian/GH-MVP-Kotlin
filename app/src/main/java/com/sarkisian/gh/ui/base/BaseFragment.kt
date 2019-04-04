package com.sarkisian.gh.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    protected fun setActionBarTitle(title: String) {
        (activity as BaseActivity).setActionBarTitle(title)
    }

}
