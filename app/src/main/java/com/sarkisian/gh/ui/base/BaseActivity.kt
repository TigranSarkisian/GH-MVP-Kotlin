package com.sarkisian.gh.ui.base


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.include_toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)

        tool_bar?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    fun setActionBarTitle(title: String) {
        tv_toolbar_title?.text = title
    }

    fun setActionBarSubTitle(subtitle: String) {
        tv_toolbar_subtitle?.visibility = View.VISIBLE
        tv_toolbar_subtitle?.text = subtitle
    }

    fun showActionBarHomeButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

}
