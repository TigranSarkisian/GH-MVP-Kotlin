package com.sarkisian.gh.ui.search


import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.SearchView
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.ui.adapter.RepoAdapter
import com.sarkisian.gh.ui.base.BaseFragment
import com.sarkisian.gh.util.extensions.inflate
import com.sarkisian.gh.util.extensions.snack
import com.sarkisian.gh.util.extensions.toast
import com.sarkisian.gh.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject


@ActivityScoped
class RepoSearchFragment : BaseFragment(), RepoSearchContract.RepoSearchView,
    RepoAdapter.OnItemClickListener {

    @Inject
    lateinit var repoSearchPresenter: RepoSearchContract.RepoSearchPresenter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var repoAdapter: RepoAdapter

    companion object {
        fun newInstance(): RepoSearchFragment = RepoSearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoSearchPresenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(com.sarkisian.gh.R.layout.fragment_search)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_repo_search.apply {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        repoAdapter = RepoAdapter(this@RepoSearchFragment)
        rv_repo_search.adapter = repoAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        repoSearchPresenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(com.sarkisian.gh.R.menu.search_fragment_menu, menu)
        val searchItem = menu?.findItem(com.sarkisian.gh.R.id.search_repos)
        val searchView = searchItem?.actionView as SearchView

        repoSearchPresenter.searchRepos(
            RxSearchView.queryTextChanges(searchView)
        )
    }

    override fun onSearchResultRetrieved(repoList: MutableList<Repo>) {
        repoAdapter.setItems(repoList) {}
    }

    override fun showLoadingIndicator(value: Boolean) {
        // not used
    }

    override fun showEmptyState(value: Boolean) {
        repo_search_empty_state.visible(value)
    }

    override fun showMessage(message: String) {
        snack(message)
    }

    override fun onItemClick(position: Int, repo: Repo) {
        toast(repo.name)
    }

    override fun onItemLongClick(position: Int, repo: Repo) {
        toast(repo.name)
    }

}
