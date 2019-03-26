package com.sarkisian.gh.data.repository

import com.sarkisian.gh.data.api.ApiFactory.GIT_HUB_USER
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import com.sarkisian.gh.util.TestUtil
import com.sarkisian.gh.util.TestUtil.Constant.TEST_QUERY
import com.sarkisian.gh.util.TestUtil.Constant.USER_NAME
import com.sarkisian.gh.util.rxbus.RxBus
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class GitHubRepositoryTest {

    @Mock lateinit var remoteDataSource: GitHubRemoteDataSource
    @Mock lateinit var localDataSource: GitHubLocalDataSource
    @Mock lateinit var rxBus: RxBus
    lateinit var gitHubRepository: GitHubRepository
    lateinit var repoList: MutableList<Repo>
    lateinit var repo: Repo
    lateinit var searchRequest: SearchRequest

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        gitHubRepository = GitHubRepository(localDataSource, remoteDataSource, rxBus)
        repoList = TestUtil.composeStubGitHubRepoList()
        repo = TestUtil.composeStubGitHubRepo()
        searchRequest = TestUtil.composeStubGitHubSearchRequest()
    }

    @Test
    fun getRepos_NoDataInDb_LoadingFromApiFail() {
        `when`(localDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(mutableListOf()))
        `when`(localDataSource.getFavoriteRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(mutableListOf()))
        `when`(remoteDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.error(Throwable(eq(anyString()))))

        gitHubRepository.refreshRepos()
        gitHubRepository.getRepos(USER_NAME)
                .test()
                .await()
                .assertError(Throwable::class.java)
                .assertValueCount(1)

        verify(remoteDataSource).getRepos(USER_NAME)
        verify(localDataSource).getRepos(USER_NAME)
        verify(localDataSource, never()).getFavoriteRepos(USER_NAME)
    }

    @Test
    fun getRepos_NoDataInDb_LoadingFromApiSuccess() { // empty api
        `when`(localDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(mutableListOf()))
        `when`(localDataSource.getFavoriteRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(mutableListOf()))
        `when`(remoteDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(mutableListOf()))

        gitHubRepository.refreshRepos()
        val testObserver: TestObserver<MutableList<Repo>> = TestObserver()
        gitHubRepository.getRepos(USER_NAME).subscribe(testObserver)

        testObserver
                .await()
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(2)

        verify(remoteDataSource).getRepos(USER_NAME)
        verify(localDataSource).addRepos(mutableListOf())
        verify(localDataSource).getFavoriteRepos(USER_NAME)
        verify(localDataSource, times(2)).getRepos(USER_NAME)
    }

    @Test
    fun getRepos_HasDataInDb_LoadingFromApiSuccess() {
        `when`(localDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(repoList))
        `when`(localDataSource.getFavoriteRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(repoList))
        `when`(remoteDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(repoList))

        gitHubRepository.refreshRepos()
        gitHubRepository.getRepos(USER_NAME)
                .test()
                .await()
                .assertValueCount(2)
                .assertNoErrors()
                .assertComplete()

        verify(remoteDataSource).getRepos(USER_NAME)
        verify(localDataSource).addRepos(repoList)
        verify(localDataSource, times(2)).getRepos(USER_NAME)
        verify(localDataSource).getFavoriteRepos(USER_NAME)
    }

    @Test
    fun getRepos_HasDataInDb_LoadingFromApiFail() {
        `when`(localDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(repoList))
        `when`(localDataSource.getFavoriteRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(mutableListOf()))
        `when`(remoteDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.error(Throwable(eq(anyString()))))

        gitHubRepository.refreshRepos()
        val testObserver: TestObserver<List<Repo>> = TestObserver()
        gitHubRepository.getRepos(USER_NAME).subscribe(testObserver)

        testObserver
                .await()
                .assertError(Throwable::class.java)
                .assertValueCount(1)
                .assertValue(repoList)

        verify(remoteDataSource).getRepos(USER_NAME)
        verify(localDataSource).getRepos(USER_NAME)
        verify(localDataSource, never()).getFavoriteRepos(USER_NAME)
    }

    @Test
    fun getRepos_OnlyFromDb() {
        `when`(localDataSource.getRepos(gitHubUser = USER_NAME))
                .thenReturn(Observable.just(repoList))

        gitHubRepository.getRepos(USER_NAME)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)

        verify(localDataSource).getRepos(gitHubUser = USER_NAME)
    }

    @Test
    fun getRepo_HasDataInDb_LoadingFromApiFail() {
        `when`(localDataSource.getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME))
                .thenReturn(Observable.just(repo))
        `when`(remoteDataSource.getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME))
                .thenReturn(Observable.error(Throwable(eq(anyString()))))

        gitHubRepository.getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME)
                .test()
                .await()
                .assertError(Throwable::class.java)
                .assertValueCount(1)
                .assertValue(repo)

        verify(remoteDataSource).getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME)
        verify(localDataSource).getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME)
    }

    @Test
    fun getRepo_HasDataInDb_LoadingFromApiSuccess() {
        `when`(localDataSource.getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME))
                .thenReturn(Observable.just(repo))
        `when`(remoteDataSource.getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME))
                .thenReturn(Observable.just(repo))

        gitHubRepository.getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(2)

        verify(remoteDataSource).getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME)
        verify(localDataSource).addRepo(repo)
        verify(localDataSource, times(3))
                .getRepo(gitHubUser = USER_NAME, repoName = TestUtil.Constant.REPO_NAME)
    }

    @Test
    fun addRepo() {
        `when`(localDataSource.addRepo(repo))
                .thenReturn(Completable.complete())

        gitHubRepository.addRepo(repo)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()

        verify(localDataSource).addRepo(repo)
    }

    @Test
    fun addRepos() {
        `when`(localDataSource.addRepos(repoList))
                .thenReturn(Completable.complete())

        gitHubRepository.addRepos(repoList)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()

        verify(localDataSource).addRepos(repoList)
    }

    @Test
    fun deleteRepo() {
        `when`(localDataSource.deleteRepo(repo))
                .thenReturn(Completable.complete())

        gitHubRepository.deleteRepo(repo)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()

        verify(localDataSource).deleteRepo(repo)
    }

    @Test
    fun editRepo() {
        `when`(localDataSource.updateRepo(repo))
                .thenReturn(Completable.complete())

        gitHubRepository.updateRepo(repo)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()

        verify(localDataSource).updateRepo(repo)
    }

    @Test
    fun addRepoToFavorites() {
        `when`(localDataSource.addRepoToFavorites(repo))
                .thenReturn(Completable.complete())

        gitHubRepository.addRepoToFavorites(repo)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()

        verify(localDataSource).addRepoToFavorites(repo)
    }

    @Test
    fun removeRepoFromFavorites() {
        `when`(localDataSource.deleteRepoFromFavorites(repo))
                .thenReturn(Completable.complete())

        gitHubRepository.deleteRepoFromFavorites(repo)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()

        verify(localDataSource).deleteRepoFromFavorites(repo)
    }

    @Test
    fun getFavoriteRepos() {
        `when`(localDataSource.getFavoriteRepos(GIT_HUB_USER))
                .thenReturn(Observable.just(repoList))

        gitHubRepository.getFavoriteRepos(GIT_HUB_USER)
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()

        verify(localDataSource).getFavoriteRepos(GIT_HUB_USER)
    }

    @Test
    fun searchRepos_Success() {
        `when`(remoteDataSource.searchRepos(query = TEST_QUERY, page = 1))
                .thenReturn(Observable.just(searchRequest))

        gitHubRepository.searchRepos(query = TEST_QUERY, page = 1)
                .test()
                .await()
                .assertValueCount(1)
                .assertNoErrors()
                .assertComplete()

        verify(remoteDataSource).searchRepos(query = TEST_QUERY, page = 1)
    }

    @Test
    fun searchRepos_Fail() {
        `when`(remoteDataSource.searchRepos(query = TEST_QUERY, page = 1))
                .thenReturn(Observable.error(Throwable(eq(anyString()))))

        gitHubRepository.searchRepos(query = TEST_QUERY, page = 1)
                .test()
                .await()
                .assertError(Throwable::class.java)

        verify(remoteDataSource).searchRepos(query = TEST_QUERY, page = 1)
    }
}