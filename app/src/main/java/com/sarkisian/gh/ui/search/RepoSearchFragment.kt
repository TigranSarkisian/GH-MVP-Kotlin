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
import com.sarkisian.gh.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject


@ActivityScoped
class RepoSearchFragment : BaseFragment(), RepoSearchContract.RepoSearchView,
    RepoAdapter.OnItemClickListener {

    @Inject
    lateinit var presenter: RepoSearchContract.RepoSearchPresenter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(com.sarkisian.gh.R.layout.fragment_search)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        rv_repo_search.apply {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        adapter = RepoAdapter(this@RepoSearchFragment)
        rv_repo_search.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(com.sarkisian.gh.R.menu.search_fragment_menu, menu)
        val searchItem = menu?.findItem(com.sarkisian.gh.R.id.search_repos)
        val searchView = searchItem?.actionView as SearchView
        presenter.searchRepos(RxSearchView.queryTextChanges(searchView))
    }

    override fun onSearchResultRetrieved(repoList: MutableList<Repo>) = adapter.setItems(repoList)

    override fun showEmptyState(value: Boolean) = repo_search_empty_state.visible(value)

    override fun showMessage(message: String) = toast(message)

    override fun onItemClick(position: Int, repo: Repo) = toast(repo.name.toString())

    override fun onItemLongClick(position: Int, repo: Repo) = toast(repo.name.toString())

    companion object {
        fun newInstance(): RepoSearchFragment = RepoSearchFragment()
    }

}
