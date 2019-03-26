package com.sarkisian.gh.ui.repo


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sarkisian.gh.R
import com.sarkisian.gh.data.entity.Owner
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.BaseActivity
import com.sarkisian.gh.util.extensions.*
import kotlinx.android.synthetic.main.activity_repo.*
import javax.inject.Inject

class RepoActivity : BaseActivity(), RepoContract.RepoView, View.OnClickListener {

    override val layoutResource: Int
        get() = R.layout.activity_repo

    @Inject lateinit var repoPresenter: RepoContract.RepoPresenter
    private var gitHubRepo: Repo? = null
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoPresenter.attachView(this)

        fab_edit.setOnClickListener(this)

        val repoName = getStringExtra(Repo.REPO_NAME)
        val repoOwner = getStringExtra(Owner.OWNER_LOGIN)

        repoPresenter.loadRepo(repoOwner, repoName)
    }

    override fun onDestroy() {
        super.onDestroy()
        repoPresenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.repo_activity_menu, menu)
        this.menu = menu
        setUpMenu(this.menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.delete_repo -> gitHubRepo?.let { repoPresenter.deleteRepo(it) }
            R.id.favorite_repo -> gitHubRepo?.let {
                if (!it.favorite) {
                    it.favorite = true
                    item.setIcon(R.drawable.ic_favorite)
                    repoPresenter.addRepoToFavorites(it)
                } else {
                    it.favorite = false
                    item.setIcon(R.drawable.ic_unfavorite)
                    repoPresenter.deleteRepoFromFavorites(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab_edit -> gitHubRepo?.let {
                if (!edt_repo_name.isVisible()) {
                    tv_repo_name.visible(false)
                    edt_repo_name.visible(true)
                    edt_repo_name.setText(tv_repo_name.text.toString())
                } else {
                    it.name = edt_repo_name.text.toString()
                    repoPresenter.updateRepo(it)
                }
            }
        }
    }

    override fun showLoadingIndicator(value: Boolean) {
        when (value) {
            true -> setActionBarTitle(getString(R.string.status_updating))
            false -> setActionBarTitle(gitHubRepo?.name!!)
        }
    }

    override fun showRepo(repo: Repo) {
        gitHubRepo = repo

        setUpMenu(menu)

        tv_repo_name.setTextOrHide(gitHubRepo?.name)
        tv_repo_id.setTextWithFormatArgs(R.string.repo_id, gitHubRepo?.id)
        tv_repo_url.setTextWithFormatArgs(R.string.repo_url, gitHubRepo?.htmlUrl)
        tv_repo_language.setTextWithFormatArgs(R.string.repo_language, gitHubRepo?.language)
        tv_repo_description.setTextWithFormatArgs(R.string.repo_description, gitHubRepo?.description)
        tv_repo_network_count.setTextWithFormatArgs(R.string.repo_network_count, gitHubRepo?.networkCount)
        tv_repo_subscribers_count.setTextWithFormatArgs(R.string.repo_subscribers_count, gitHubRepo?.subscribersCount)

        /*GlideApp.with(this)
                .load("")
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_launcher_background)
                .into(iv_repo_logo)
         */
    }

    override fun showRepoDeleted(repo: Repo) {
        finish()
    }

    override fun showRepoUpdated(repo: Repo) {
        edt_repo_name.visible(false)
        tv_repo_name.visible(true)
        tv_repo_name.text = repo.name
        setActionBarTitle(repo.name!!)
        hideKeyboard()
    }

    override fun showMessage(message: String) {
        cl_repo_container.snack(message)
    }

    override fun showEmptyState(value: Boolean) {
    }

    private fun setUpMenu(menu: Menu?) {
        when (gitHubRepo?.favorite) {
            true -> menu?.findItem(R.id.favorite_repo)?.setIcon(R.drawable.ic_favorite)
            false -> menu?.findItem(R.id.favorite_repo)?.setIcon(R.drawable.ic_unfavorite)
        }
    }

}
