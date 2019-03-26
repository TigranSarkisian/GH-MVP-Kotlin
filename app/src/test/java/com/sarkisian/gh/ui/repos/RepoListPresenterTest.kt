package com.sarkisian.gh.ui.repos

import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubRepository
import com.sarkisian.gh.util.TestUtil
import com.sarkisian.gh.util.TestUtil.Constant.USER_NAME
import com.sarkisian.gh.util.TestUtil.Constant.WAIT_TIME
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.rxbus.RxBus
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class RepoListPresenterTest {

    @Mock lateinit var repoListView: RepoListContract.RepoListView
    @Mock lateinit var gitHubRepository: GitHubRepository
    @Mock lateinit var errorHandler: ErrorHandler
    @Mock lateinit var rxBus: RxBus
    lateinit var repoListPresenter: RepoListPresenter
    lateinit var repoList: MutableList<Repo>
    lateinit var repo: Repo
    lateinit var throwable: Throwable
    lateinit var captor: KArgumentCaptor<(String) -> Unit>

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        `when`(rxBus.observable()).thenReturn(Observable.just(Any()))
        repoListPresenter = RepoListPresenter(gitHubRepository, errorHandler, rxBus)
        repoListPresenter.attachView(repoListView)
        repoList = TestUtil.composeStubGitHubRepoList()
        repo = TestUtil.composeStubGitHubRepo()
        throwable = Throwable()
        captor = argumentCaptor()
    }

    @Test
    fun presenterCreated() {
        assertNotNull(repoListPresenter)
    }

    @Test
    fun loadingRepos_forceUpdateFalse() {
        `when`(gitHubRepository.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just((repoList)))

        repoListPresenter.loadRepos(username = USER_NAME)

        verify(gitHubRepository).getRepos(gitHubUser = USER_NAME)
        verify(repoListView, timeout(WAIT_TIME)).showRepos(repoList)
        verify(repoListView, never()).showMessage(anyString())
        verify(errorHandler, never()).readError(error = eq(throwable), messageListener = captor.capture())
        verify(repoListView).showEmptyState(anyBoolean())
    }

    @Test
    fun loadingRepos_forceUpdateTrue_Success() {
        `when`(gitHubRepository.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(repoList))

        repoListPresenter.loadRepos(username = USER_NAME, forceUpdate = true)

        verify(gitHubRepository).getRepos(gitHubUser = USER_NAME)

        val inOrder = inOrder(repoListView)
        inOrder.verify(repoListView).showLoadingIndicator(true)
        inOrder.verify(repoListView).showLoadingIndicator(false)
        verify(repoListView, never()).showMessage(anyString())
        verify(errorHandler, never()).readError(error = eq(throwable), messageListener = captor.capture())
        verify(repoListView, timeout(WAIT_TIME)).showRepos(repoList)
        verify(repoListView).showEmptyState(false)
    }

    @Test
    fun loadingRepos_forceUpdateTrue_Fail() {
        `when`(gitHubRepository.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.error(throwable))

        repoListPresenter.loadRepos(username = USER_NAME, forceUpdate = true)

        verify(gitHubRepository).getRepos(gitHubUser = USER_NAME)
        val inOrder = inOrder(repoListView)
        inOrder.verify(repoListView).showLoadingIndicator(true)
        inOrder.verify(repoListView).showLoadingIndicator(false)
        verify(repoListView, never()).showRepos(repoList)
        verify(errorHandler).readError(error = eq(throwable), messageListener = captor.capture())
    }

    @Test
    fun addingRepo() {
        `when`(gitHubRepository.addRepo(repo))
                .thenReturn(Completable.complete())

        repoListPresenter.addRepo(repo)

        verify(gitHubRepository).addRepo(repo)
        verify(repoListView, timeout(WAIT_TIME)).showRepoAdded(repo)
    }

    @Test
    fun updateRepo() {
        `when`(gitHubRepository.updateRepo(repo))
                .thenReturn(Completable.complete())

        repoListPresenter.updateRepo(repo)

        verify(gitHubRepository).updateRepo(repo)
        verify(repoListView, timeout(WAIT_TIME)).showRepoUpdated(repo)
    }

    @Test
    fun deletingRepo() {
        `when`(gitHubRepository.deleteRepo(repo))
                .thenReturn(Completable.complete())

        repoListPresenter.deleteRepo(repo)

        verify(gitHubRepository).deleteRepo(repo)
        verify(repoListView, timeout(WAIT_TIME)).showRepoDeleted(repo)
    }
}