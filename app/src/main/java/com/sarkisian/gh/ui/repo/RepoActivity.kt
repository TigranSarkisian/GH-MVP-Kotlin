package com.sarkisian.gh.ui.repo


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.sarkisian.gh.R
import com.sarkisian.gh.data.entity.Owner
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.BaseActivity
import com.sarkisian.gh.util.extensions.*
import kotlinx.android.synthetic.main.activity_repo.*
import org.jetbrains.anko.toast
import org.koin.android.scope.currentScope

class RepoActivity : BaseActivity(), RepoContract.RepoView {

    private val presenter by currentScope.inject<RepoContract.RepoPresenter>()
    private var repo: Repo? = null
    private var menu: Menu? = null

    override val layoutResource: Int
        get() = R.layout.activity_repo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)

        val repoName = intent.extras?.getString(Repo.REPO_NAME)
        val repoOwner = intent.extras?.getString(Owner.OWNER_LOGIN)
        presenter.loadRepo(repoOwner, repoName)

        fab_edit.setOnClickListener {
            when (it.id) {
                R.id.fab_edit -> repo?.let {
                    if (!edt_repo_name.isVisible()) {
                        tv_repo_name.visible(false)
                        edt_repo_name.visible(true)
                        edt_repo_name.setText(tv_repo_name.text.toString())
                    } else {
                        it.name = edt_repo_name.text.toString()
                        presenter.updateRepo(it)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
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
            R.id.delete_repo -> {
                repo?.let { presenter.deleteRepo(it) }
            }
            R.id.favorite_repo -> {
                repo?.let {
                    it.favorite = !it.favorite
                    item.setIcon(if (!it.favorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite)
                    if (!it.favorite) presenter.addRepoToFavorites(it) else presenter.deleteRepoFromFavorites(it)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRepoLoaded(repo: Repo) {
        this.repo = repo
        setUpMenu(menu)

        tv_repo_name.setTextOrHide(this.repo?.name)
        tv_repo_id.setTextWithFormatArgs(R.string.repo_id, this.repo?.id)
        tv_repo_url.setTextWithFormatArgs(R.string.repo_url, this.repo?.htmlUrl)
        tv_repo_language.setTextWithFormatArgs(R.string.repo_language, this.repo?.language)
        tv_repo_description.setTextWithFormatArgs(R.string.repo_description, this.repo?.description)
        tv_repo_network_count.setTextWithFormatArgs(R.string.repo_network_count, this.repo?.networkCount)
        tv_repo_subscribers_count.setTextWithFormatArgs(R.string.repo_subscribers_count, this.repo?.subscribersCount)

        /*GlideApp.with(this)
                .load("")
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_launcher_background)
                .into(iv_repo_logo)
         */
    }

    override fun onRepoDeleted(repo: Repo) = finish()

    override fun onRepoUpdated(repo: Repo) {
        edt_repo_name.visible(false)
        tv_repo_name.visible(true)
        tv_repo_name.text = repo.name
        setActionBarTitle(repo.name!!)
        hideKeyboard()
    }

    override fun showLoadingIndicator(value: Boolean) {
        when (value) {
            true -> setActionBarTitle(getString(R.string.status_updating))
            false -> setActionBarTitle(repo?.name!!)
        }
    }

    override fun showMessage(message: String) = toast(message)

    private fun setUpMenu(menu: Menu?) {
        when (repo?.favorite) {
            true -> menu?.findItem(R.id.favorite_repo)?.setIcon(R.drawable.ic_favorite)
            false -> menu?.findItem(R.id.favorite_repo)?.setIcon(R.drawable.ic_unfavorite)
        }
    }

}
