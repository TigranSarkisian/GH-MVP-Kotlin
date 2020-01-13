package com.sarkisian.gh.ui.favorites


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sarkisian.gh.R
import com.sarkisian.gh.data.entity.Owner
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.ui.adapter.RepoAdapter
import com.sarkisian.gh.ui.base.BaseFragment
import com.sarkisian.gh.ui.repo.RepoActivity
import com.sarkisian.gh.util.extensions.*
import kotlinx.android.synthetic.main.fragment_repo_favorites.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

@ActivityScoped
class RepoFavoritesFragment : BaseFragment(), RepoFavoritesContract.RepoFavoritesView,
    RepoAdapter.OnItemClickListener {

    @Inject
    lateinit var presenter: RepoFavoritesContract.RepoFavoritesPresenter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_repo_favorites)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        rv_repo_favorites.apply {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        adapter = RepoAdapter(this@RepoFavoritesFragment)
        rv_repo_favorites.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter.getFavoriteRepos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onFavoriteReposLoaded(repoList: MutableList<Repo>) = adapter.setItems(repoList)

    override fun onRepoDeletedFromFavorites(repo: Repo) {
        adapter.removeItem(repo)
        if (adapter.itemCount == 0) showEmptyState(true)
    }

    override fun onRepoAddedToFavorites(repo: Repo) {
        adapter.insertItem(repo)
        showEmptyState(false)
    }

    override fun onRepoUpdated(repo: Repo) = adapter.updateItem(repo)

    override fun showEmptyState(value: Boolean) = repo_favorites_empty_state.visible(value)

    override fun showMessage(message: String) = toast(message)

    override fun onItemClick(position: Int, repo: Repo) {
        val intent = Intent(activity, RepoActivity::class.java)
        intent.putExtra(Repo.REPO_NAME, repo.name)
        intent.putExtra(Owner.OWNER_LOGIN, repo.owner?.login)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, repo: Repo) = presenter.deleteRepoFromFavorites(repo)

    companion object {
        fun newInstance(): RepoFavoritesFragment = RepoFavoritesFragment()
    }

}
