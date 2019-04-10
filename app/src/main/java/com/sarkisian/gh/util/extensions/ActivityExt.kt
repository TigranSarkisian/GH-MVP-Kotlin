package com.sarkisian.gh.util.extensions

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast


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

        fragmentTransaction.setPrimaryNavigationFragment(
            when (existingFragment) {
                null -> fragment
                else -> existingFragment
            }
        )
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
            fragmentTransaction
                .addToBackStack(transactionFragment.javaClass.simpleName)
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
        if (addToBackStack){
            fragmentTransaction
                .addToBackStack(transactionFragment.javaClass.simpleName)
        }
        fragmentTransaction
            .replace(frameId, transactionFragment, transactionFragment.name())
            .commit()
    }
}

fun AppCompatActivity.toast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun AppCompatActivity.showKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun AppCompatActivity.snack(msg: String) {
    findViewById<View>(android.R.id.content)?.let {
        Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show()
    }
}

fun AppCompatActivity.getStringExtra(key: String): String {
    return intent.extras?.let {
        it.getString(key, emptyString())
    } ?: kotlin.run {
        emptyString()
    }
}

