package com.sarkisian.gh.ui.base


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingInjector: DispatchingAndroidInjector<Fragment>

    abstract val layoutResource: Int

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = dispatchingInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
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
