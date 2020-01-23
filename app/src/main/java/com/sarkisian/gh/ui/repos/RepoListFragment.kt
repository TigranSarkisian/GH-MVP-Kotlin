package com.sarkisian.gh.ui.repos


import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.sarkisian.gh.R
import com.sarkisian.gh.data.entity.Owner
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.ui.adapter.RepoAdapter
import com.sarkisian.gh.ui.base.BaseFragment
import com.sarkisian.gh.ui.repo.RepoActivity
import com.sarkisian.gh.util.extensions.*
import kotlinx.android.synthetic.main.fragment_repo_list.*
import javax.inject.Inject


@ActivityScoped
class RepoListFragment : BaseFragment(), RepoListContract.RepoListView,
        RepoAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var repoListPresenter: RepoListContract.RepoListPresenter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var repoAdapter: RepoAdapter

    companion object {
        fun newInstance(): RepoListFragment = RepoListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoListPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_repo_list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        srl_repo_list.setOnRefreshListener(this@RepoListFragment)

        rv_repo_list.apply {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            onScrollToBottom { repoListPresenter.loadNextPage() }
        }

        repoAdapter = RepoAdapter(this@RepoListFragment)
        rv_repo_list.adapter = repoAdapter
        repoListPresenter.loadRepos("google", true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_repo -> {
                val newRepo = Repo(
                        id = System.currentTimeMillis(),
                        name = "Repo ${System.currentTimeMillis()}",
                        language = "Kotlin",
                        htmlUrl = "http://www.nourl.com/",
                        description = "This is test repo created with id ${System.currentTimeMillis()}",
                        updatedAt = System.currentTimeMillis().formatTimeMillisToISO()
                )
                repoListPresenter.addRepo(newRepo)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.repo_list_fragment_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        repoListPresenter.detachView()
    }

    override fun onRefresh() {
        repoListPresenter.loadRepos("google", true)
    }

    override fun showRepos(repoList: MutableList<Repo>) {
        repoAdapter.setItems(repoList) {
            if (repoList.isNotEmpty()) rv_repo_list.post {
                rv_repo_list.ifNotScrollable { repoListPresenter.loadNextPage() }
            }
        }
    }

    override fun showNextPageRepos(repoList: MutableList<Repo>) {
        val positionStart = repoAdapter.items.size
        repoAdapter.insertItems(positionStart, repoList)
    }

    override fun showRepoDeleted(repo: Repo) {
        repoAdapter.removeItem(repo)
        if (repoAdapter.itemCount == 0) {
            showEmptyState(true)
        }
    }

    override fun showRepoAdded(repo: Repo) {
        val insertedPosition = repoAdapter.insertItem(repo)
        rv_repo_list.smoothScrollToPosition(insertedPosition)
        showEmptyState(false)
    }

    override fun showRepoUpdated(repo: Repo) {
        repoAdapter.updateItem(repo)
    }

    override fun showLoadingIndicator(value: Boolean) {
        when (value) {
            true -> if (!srl_repo_list.isRefreshing) setActionBarTitle(getString(R.string.status_updating))
            false -> {
                srl_repo_list.isRefreshing = false
                setActionBarTitle(getString(R.string.screen_list))
            }
        }
    }

    override fun showNextPageLoadingIndicator(value: Boolean) {
        when (value) {
            true -> {
            }
            false -> {
            }
        }
    }

    override fun showEmptyState(value: Boolean) {
        repo_list_empty_state.visible(value)
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
        repoListPresenter.deleteRepo(repo)
    }

}
