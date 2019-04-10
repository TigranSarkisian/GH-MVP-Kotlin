package com.sarkisian.gh.ui.search

import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import com.sarkisian.gh.data.repository.GitHubRepository
import com.sarkisian.gh.util.TestUtil
import com.sarkisian.gh.util.TestUtil.Constant.TEST_QUERY
import com.sarkisian.gh.util.TestUtil.Constant.WAIT_TIME
import com.sarkisian.gh.util.error.ErrorHandler
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class RepoSearchPresenterTest {

    @Mock lateinit var repoSearchView: RepoSearchContract.RepoSearchView
    @Mock lateinit var gitHubRepository: GitHubRepository
    @Mock lateinit var errorHandler: ErrorHandler
    lateinit var repoSearchPresenter: RepoSearchPresenter
    lateinit var repoList: MutableList<Repo>
    lateinit var repo: Repo
    lateinit var throwable: Throwable
    lateinit var captor: KArgumentCaptor<(String) -> Unit>
    lateinit var searchRequest: SearchRequest

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        repoSearchPresenter = RepoSearchPresenter(gitHubRepository, errorHandler)
        repoSearchPresenter.attachView(repoSearchView)
        repoList = TestUtil.composeStubGitHubRepoList()
        repo = TestUtil.composeStubGitHubRepo()
        throwable = Throwable()
        captor = argumentCaptor()
        searchRequest = TestUtil.composeStubGitHubSearchRequest()
    }

    @Test
    fun presenterCreated() {
        Assert.assertNotNull(repoSearchPresenter)
    }

    @Test
    fun searchRepos_Success() {
        `when`(gitHubRepository.searchRepos(query = TEST_QUERY, page = 1))
                .thenReturn(Observable.just(searchRequest))

        repoSearchPresenter.searchRepos(query = TEST_QUERY)

        verify(gitHubRepository).searchRepos(query = TEST_QUERY, page = 1)

        verify(repoSearchView, timeout(WAIT_TIME)).showSearchResult(searchRequest.items)
        verify(repoSearchView).showEmptyState(false)
        verify(repoSearchView, never()).showMessage(anyString())
        verify(errorHandler, never()).readError(error = eq(throwable), messageListener = captor.capture())
    }

    @Test
    fun searchRepos_Fail() {
        `when`(gitHubRepository.searchRepos(query = TEST_QUERY, page = 1))
                .thenReturn(Observable.error(throwable))

        repoSearchPresenter.searchRepos(query = TEST_QUERY)

        verify(gitHubRepository).searchRepos(query = TEST_QUERY, page = 1)
        verify(repoSearchView, never()).showSearchResult(searchRequest.items)
        verify(errorHandler).readError(error = eq(throwable), messageListener = captor.capture())
    }

    @Test
    fun clearSearch() {
        repoSearchPresenter.clearSearch()

        verify(repoSearchView).showSearchCleared()
        verify(gitHubRepository, never()).searchRepos(query = TEST_QUERY, page = 1)
        verify(repoSearchView, never()).showSearchResult(searchRequest.items)
        verify(errorHandler, never()).readError(error = eq(throwable), messageListener = captor.capture())
    }
}