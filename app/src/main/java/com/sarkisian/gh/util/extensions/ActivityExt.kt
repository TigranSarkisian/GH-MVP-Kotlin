package com.sarkisian.gh.util.extensions

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


fun AppCompatActivity.switchFragment(
    fragment: Fragment,
    frameId: Int,
    addToBackStack: Boolean = false
) {
    val existingFragment = supportFragmentManager.findFragmentByTag(fragment.name())
    val fragmentTransaction = supportFragmentManager.beginTransaction()

    if (!supportFragmentManager.isStateSaved) {
        if (existingFragment != null) {
            fragmentTransaction.show(existingFragment)
        } else {
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            }

            fragmentTransaction.add(frameId, fragment, fragment.name())
        }

        val previousFragment = supportFragmentManager.primaryNavigationFragment
        if (previousFragment != null && previousFragment != existingFragment) {
            fragmentTransaction.hide(previousFragment)
        }

        fragmentTransaction.setPrimaryNavigationFragment(existingFragment ?: fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    frameId: Int,
    addToBackStack: Boolean = false
) {
    var transactionFragment = fragment
    val existingFragment = supportFragmentManager.findFragmentByTag(fragment.name())
    val fragmentTransaction = supportFragmentManager.beginTransaction()

    existingFragment?.let {
        transactionFragment = existingFragment
    }

    if (!supportFragmentManager.isStateSaved) {
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(transactionFragment.javaClass.simpleName)
        }

        fragmentTransaction
            .add(frameId, transactionFragment, transactionFragment.name())
            .commit()
    }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    addToBackStack: Boolean = false
) {
    var transactionFragment = fragment
    val existingFragment = supportFragmentManager.findFragmentByTag(fragment.name())
    val fragmentTransaction = supportFragmentManager.beginTransaction()

    existingFragment?.let {
        transactionFragment = existingFragment
    }

    if (!supportFragmentManager.isStateSaved) {
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(transactionFragment.javaClass.simpleName)
        }

        fragmentTransaction
            .replace(frameId, transactionFragment, transactionFragment.name())
            .commit()
    }
}

fun AppCompatActivity.hideKeyboard() =
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(window.decorView.windowToken, 0)

fun AppCompatActivity.showKeyboard() =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
