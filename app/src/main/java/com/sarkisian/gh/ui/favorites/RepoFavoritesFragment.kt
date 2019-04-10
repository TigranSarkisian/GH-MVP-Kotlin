package com.sarkisian.gh.ui.favorites


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sarkisian.gh.R
import com.sarkisian.gh.data.api.ApiFactory.GIT_HUB_USER
import com.sarkisian.gh.data.entity.Owner
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.adapter.RepoAdapter
import com.sarkisian.gh.ui.base.BaseFragment
import com.sarkisian.gh.ui.repo.RepoActivity
import com.sarkisian.gh.util.extensions.*
import kotlinx.android.synthetic.main.fragment_repo_favorites.*
import org.koin.android.ext.android.inject

class RepoFavoritesFragment : BaseFragment(), RepoFavoritesContract.RepoFavoritesView,
    RepoAdapter.OnItemClickListener {

    private val repoFavoritesPresenter by inject<RepoFavoritesContract.RepoFavoritesPresenter>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var repoAdapter: RepoAdapter

    companion object {
        fun newInstance(): RepoFavoritesFragment = RepoFavoritesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoFavoritesPresenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_repo_favorites)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_repo_favorites.apply {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        repoAdapter = RepoAdapter(this@RepoFavoritesFragment)
        rv_repo_favorites.adapter = repoAdapter
    }

    override fun onResume() {
        super.onResume()
        repoFavoritesPresenter.loadFavoriteRepos(GIT_HUB_USER)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        repoFavoritesPresenter.detachView()
    }

    override fun showFavoriteRepos(repoList: MutableList<Repo>) {
        repoAdapter.setItems(repoList) {}
    }

    override fun showRepoDeletedFromFavorites(repo: Repo) {
        repoAdapter.removeItem(repo)
        if (repoAdapter.itemCount == 0) {
            showEmptyState(true)
        }
    }

    override fun showRepoAddedToFavorites(repo: Repo) {
        repoAdapter.insertItem(repo)
        showEmptyState(false)
    }

    override fun showRepoUpdated(repo: Repo) {
        repoAdapter.updateItem(repo)
    }

    override fun showLoadingIndicator(value: Boolean) {
        // not used here
    }

    override fun showEmptyState(value: Boolean) {
        repo_favorites_empty_state.visible(value)
    }

    override fun showMessage(message: String) {
        snack(message)
    }

    override fun onItemClick(position: Int, repo: Repo) {
        val intent = Intent(activity, RepoActivity::class.java)
        intent.putExtra(Repo.REPO_NAME, repo.name)
        intent.putExtra(Owner.OWNER_LOGIN, repo.owner?.login)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, repo: Repo) {
        repoFavoritesPresenter.deleteRepoFromFavorites(repo)
    }

}
