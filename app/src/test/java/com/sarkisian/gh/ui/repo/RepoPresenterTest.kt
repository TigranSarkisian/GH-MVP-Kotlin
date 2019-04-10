package com.sarkisian.gh.ui.repo

import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubRepository
import com.sarkisian.gh.util.TestUtil
import com.sarkisian.gh.util.TestUtil.Constant.REPO_NAME
import com.sarkisian.gh.util.TestUtil.Constant.USER_NAME
import com.sarkisian.gh.util.TestUtil.Constant.WAIT_TIME
import com.sarkisian.gh.util.error.ErrorHandler
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

class RepoPresenterTest {

    @Mock lateinit var repoView: RepoContract.RepoView
    @Mock lateinit var gitHubRepository: GitHubRepository
    @Mock lateinit var errorHandler: ErrorHandler
    lateinit var repoPresenter: RepoPresenter
    lateinit var repoList: MutableList<Repo>
    lateinit var repo: Repo
    lateinit var throwable: Throwable
    lateinit var captor: KArgumentCaptor<(String) -> Unit>

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        repoPresenter = RepoPresenter(gitHubRepository, errorHandler)
        repoPresenter.attachView(repoView)
        repoList = TestUtil.composeStubGitHubRepoList()
        repo = TestUtil.composeStubGitHubRepo()
        throwable = Throwable()
        captor = argumentCaptor()
    }

    @Test
    fun presenterCreated() {
        assertNotNull(repoPresenter)
    }

    @Test
    fun loadingRepo_Success() {
        `when`(gitHubRepository.getRepo(gitHubUser = USER_NAME, repoName = REPO_NAME))
                .thenReturn(Observable.just((repo)))

        repoPresenter.loadRepo(gitHubUser = USER_NAME, repoName = REPO_NAME)

        verify(gitHubRepository).getRepo(gitHubUser = USER_NAME, repoName = REPO_NAME)
        val inOrder = inOrder(repoView)
        inOrder.verify(repoView).showLoadingIndicator(true)
        inOrder.verify(repoView).showLoadingIndicator(false)
        verify(repoView, timeout(WAIT_TIME)).showRepo(repo)
        verify(repoView, never()).showEmptyState(anyBoolean())
        verify(errorHandler, never()).readError(error = eq(throwable), messageListener = captor.capture())
    }

    @Test
    fun loadingRepo_Fail() {
        `when`(gitHubRepository.getRepo(gitHubUser = USER_NAME, repoName = REPO_NAME))
                .thenReturn(Observable.error(throwable))

        repoPresenter.loadRepo(gitHubUser = USER_NAME, repoName = REPO_NAME)

        verify(gitHubRepository).getRepo(gitHubUser = USER_NAME, repoName = REPO_NAME)
        val inOrder = inOrder(repoView)
        inOrder.verify(repoView).showLoadingIndicator(true)
        inOrder.verify(repoView).showLoadingIndicator(false)
        verify(repoView, never()).showRepo(repo)
        verify(repoView, never()).showEmptyState(anyBoolean())
        verify(errorHandler).readError(error = eq(throwable), messageListener = captor.capture())
    }

    @Test
    fun deletingRepo() {
        `when`(gitHubRepository.deleteRepo(repo))
                .thenReturn(Completable.complete())

        repoPresenter.deleteRepo(repo)

        verify(gitHubRepository).deleteRepo(repo)
        verify(repoView, timeout(WAIT_TIME)).showRepoDeleted(repo)
    }

    @Test
    fun editingRepo() {
        `when`(gitHubRepository.updateRepo(repo))
                .thenReturn(Completable.complete())

        repoPresenter.updateRepo(repo)

        verify(gitHubRepository).updateRepo(repo)
        verify(repoView, timeout(WAIT_TIME)).showRepoUpdated(repo)
    }

    @Test
    fun addingRepoToFavorites() {
        `when`(gitHubRepository.addRepoToFavorites(repo))
                .thenReturn(Completable.complete())

        repoPresenter.addRepoToFavorites(repo)

        verify(gitHubRepository).addRepoToFavorites(repo)
    }

    @Test
    fun removingRepoFromFavorites() {
        `when`(gitHubRepository.deleteRepoFromFavorites(repo))
                .thenReturn(Completable.complete())

        repoPresenter.deleteRepoFromFavorites(repo)

        verify(gitHubRepository).deleteRepoFromFavorites(repo)
    }
}