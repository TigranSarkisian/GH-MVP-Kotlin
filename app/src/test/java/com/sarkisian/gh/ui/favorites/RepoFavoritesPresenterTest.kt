package com.sarkisian.gh.ui.favorites

import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubRepository
import com.sarkisian.gh.util.TestUtil
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.rxbus.RxBus
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class RepoFavoritesPresenterTest {

    @Mock lateinit var repoFavoritesView: RepoFavoritesContract.RepoFavoritesView
    @Mock lateinit var gitHubRepository: GitHubRepository
    @Mock lateinit var errorHandler: ErrorHandler
    @Mock lateinit var rxBus: RxBus
    lateinit var repoFavoritesPresenter: RepoFavoritesPresenter
    lateinit var repoList: MutableList<Repo>
    lateinit var repo: Repo
    lateinit var throwable: Throwable
    lateinit var captor: KArgumentCaptor<(String) -> Unit>

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        `when`(rxBus.observable()).thenReturn(Observable.just(Any()))
        repoFavoritesPresenter = RepoFavoritesPresenter(gitHubRepository, errorHandler, rxBus)
        repoFavoritesPresenter.attachView(repoFavoritesView)
        repoList = TestUtil.composeStubGitHubRepoList()
        repo = TestUtil.composeStubGitHubRepo()
        throwable = Throwable()
        captor = argumentCaptor()
    }

    @Test
    fun presenterCreated() {
        Assert.assertNotNull(repoFavoritesPresenter)
    }

    @Test
    fun loadingFavoriteReposFromDb() {
        `when`(gitHubRepository.getFavoriteRepos(gitHubUser = TestUtil.Constant.USER_NAME))
                .thenReturn(Observable.just((repoList)))

        repoFavoritesPresenter.loadFavoriteRepos(username = TestUtil.Constant.USER_NAME)

        Mockito.verify(gitHubRepository).getFavoriteRepos(gitHubUser = TestUtil.Constant.USER_NAME)
        val inOrder = Mockito.inOrder(repoFavoritesView)
        inOrder.verify(repoFavoritesView).showLoadingIndicator(true)
        inOrder.verify(repoFavoritesView).showLoadingIndicator(false)
        verify(repoFavoritesView, timeout(TestUtil.Constant.WAIT_TIME)).showFavoriteRepos(repoList)
        verify(repoFavoritesView).showEmptyState(false)
        verify(repoFavoritesView, never()).showMessage(anyString())
        verify(errorHandler, never()).readError(error = eq(throwable), messageListener = captor.capture())
    }

    @Test
    fun removingRepoFromFavorites() {
        `when`(gitHubRepository.deleteRepoFromFavorites(repo))
                .thenReturn(Completable.complete())

        repoFavoritesPresenter.deleteRepoFromFavorites(repo)

        verify(gitHubRepository).deleteRepoFromFavorites(repo)
        verify(repoFavoritesView, timeout(TestUtil.Constant.WAIT_TIME)).showRepoDeletedFromFavorites(repo)
    }

}