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
import com.sarkisian.gh.ui.adapter.RepoAdapter
import com.sarkisian.gh.ui.base.BaseFragment
import com.sarkisian.gh.ui.repo.RepoActivity
import com.sarkisian.gh.util.extensions.*
import kotlinx.android.synthetic.main.fragment_repo_list.*
import org.jetbrains.anko.support.v4.toast
import org.koin.android.scope.currentScope


class RepoListFragment : BaseFragment(), RepoListContract.RepoListView,
    RepoAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val presenter by currentScope.inject<RepoListContract.RepoListPresenter>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_repo_list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        srl_repo_list.setOnRefreshListener(this@RepoListFragment)

        rv_repo_list.apply {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        }

        adapter = RepoAdapter(this@RepoListFragment)
        rv_repo_list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter.loadRepos(GOOGLE_REPO)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_repo) presenter.addRepo(Repo.composeStubRepo())
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) =
        inflater.inflate(R.menu.repo_list_fragment_menu, menu)

    override fun onRefresh() = presenter.loadRepos(GOOGLE_REPO)

    override fun onReposLoaded(repoList: MutableList<Repo>) = adapter.setItems(repoList)

    override fun onRepoDeleted(repo: Repo) {
        adapter.removeItem(repo)
        if (adapter.itemCount == 0) {
            showEmptyState(true)
        }
    }

    override fun onRepoAdded(repo: Repo) {
        val insertedPosition = adapter.insertItem(repo)
        rv_repo_list.smoothScrollToPosition(insertedPosition)
        showEmptyState(false)
    }

    override fun onRepoUpdated(repo: Repo) = adapter.updateItem(repo)

    override fun showLoadingIndicator(value: Boolean) {
        when (value) {
            true -> {
                if (!srl_repo_list.isRefreshing) setActionBarTitle(getString(R.string.status_updating))
            }
            false -> {
                srl_repo_list.isRefreshing = false
                setActionBarTitle(getString(R.string.screen_list))
            }
        }
    }

    override fun showEmptyState(value: Boolean) = repo_list_empty_state.visible(value)

    override fun showMessage(message: String) = toast(message)

    override fun onItemClick(position: Int, repo: Repo) {
        val intent = Intent(activity, RepoActivity::class.java)
        intent.putExtra(Repo.REPO_NAME, repo.name)
        intent.putExtra(Owner.OWNER_LOGIN, repo.owner?.login)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, repo: Repo) = presenter.deleteRepo(repo)

    companion object {

        const val  GOOGLE_REPO = "google"

        fun newInstance(): RepoListFragment = RepoListFragment()

    }

}
