package com.sarkisian.gh.ui.search


import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.ImageView
import com.sarkisian.gh.R
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.adapter.RepoAdapter
import com.sarkisian.gh.ui.base.BaseFragment
import com.sarkisian.gh.util.extensions.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.get


class RepoSearchFragment : BaseFragment(), RepoSearchContract.RepoSearchView,
    RepoAdapter.OnItemClickListener {

    private var repoSearchPresenter: RepoSearchContract.RepoSearchPresenter = get()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var repoAdapter: RepoAdapter

    companion object {
        fun newInstance(): RepoSearchFragment = RepoSearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoSearchPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_search)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_repo_search.apply {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            onScrollToBottom { repoSearchPresenter.loadNextPage() }
        }

        repoAdapter = RepoAdapter(this@RepoSearchFragment)
        rv_repo_search.adapter = repoAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        repoSearchPresenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment_menu, menu)
        val searchItem = menu?.findItem(R.id.search_repos)
        val searchView = searchItem?.actionView as SearchView
        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotBlank()!!) {
                    repoSearchPresenter.processInput(newText)
                } else {
                    repoSearchPresenter.clearSearch()
                }
                return false
            }
        })

        closeButton.setOnClickListener {
            repoSearchPresenter.clearSearch()
            searchView.setQuery(null, false)
        }
    }

    override fun showSearchResult(repoList: MutableList<Repo>) {
        repoAdapter.setItems(repoList) {}
    }

    override fun showNextPageRepos(repoList: MutableList<Repo>) {
        val positionStart = repoAdapter.items.size
        repoAdapter.insertItems(positionStart, repoList)
    }

    override fun showSearchCleared() {
        repoAdapter.setItems(mutableListOf()) {}
        repo_search_empty_state.visible(true)
    }

    override fun showNextPageLoadingIndicator(value: Boolean) {
        when (value) {
            true -> {
            }
            false -> {
            }
        }
    }

    override fun showLoadingIndicator(value: Boolean) {
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
