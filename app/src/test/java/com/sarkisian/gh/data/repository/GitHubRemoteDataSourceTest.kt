package com.sarkisian.gh.data.repository

import com.sarkisian.gh.data.api.GitHubAPI
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import com.sarkisian.gh.util.TestUtil
import com.sarkisian.gh.util.TestUtil.Constant.TEST_QUERY
import com.sarkisian.gh.util.TestUtil.Constant.USER_NAME
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class GitHubRemoteDataSourceTest {

    @Mock lateinit var gitHubAPI: GitHubAPI
    lateinit var remoteDataSource: GitHubRemoteDataSource
    lateinit var repoList: MutableList<Repo>
    lateinit var repo: Repo
    lateinit var searchRequest: SearchRequest

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remoteDataSource = GitHubRemoteDataSource(gitHubAPI)
        repoList = TestUtil.composeStubGitHubRepoList()
        repo = TestUtil.composeStubGitHubRepo()
        searchRequest = TestUtil.composeStubGitHubSearchRequest()
    }

    @Test
    fun getRepos_Success() {
        `when`(gitHubAPI.getRepos(USER_NAME))
                .thenReturn(Observable.just(repoList))

        remoteDataSource.getRepos(USER_NAME)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(repoList)

        verify(gitHubAPI).getRepos(USER_NAME)
    }

    @Test
    fun getRepos_Fail() {
        `when`(gitHubAPI.getRepos(USER_NAME))
                .thenReturn(Observable.error(Throwable()))

        remoteDataSource.getRepos(USER_NAME)
                .test()
                .assertError(Throwable::class.java)

        verify(gitHubAPI).getRepos(USER_NAME)
    }

    @Test
    fun getRepo_Success() {
        `when`(gitHubAPI.getRepo(USER_NAME, TestUtil.Constant.REPO_NAME))
                .thenReturn(Observable.just(repo))

        remoteDataSource.getRepo(USER_NAME, TestUtil.Constant.REPO_NAME)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(repo)

        verify(gitHubAPI).getRepo(USER_NAME, TestUtil.Constant.REPO_NAME)
    }

    @Test
    fun getRepo_Fail() {
        `when`(gitHubAPI.getRepo(USER_NAME, TestUtil.Constant.REPO_NAME))
                .thenReturn(Observable.error(Throwable()))

        remoteDataSource.getRepo(USER_NAME, TestUtil.Constant.REPO_NAME)
                .test()
                .assertError(Throwable::class.java)

        verify(gitHubAPI).getRepo(USER_NAME, TestUtil.Constant.REPO_NAME)
    }

    @Test
    fun searchRepos_Success() {
        `when`(gitHubAPI.searchRepos(query = TEST_QUERY))
                .thenReturn(Observable.just(searchRequest))

        remoteDataSource.searchRepos(TEST_QUERY, 1)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(searchRequest)

        verify(gitHubAPI).searchRepos(query = TEST_QUERY)
    }

    @Test
    fun searchRepos_Fail() {
        `when`(gitHubAPI.searchRepos(query = TEST_QUERY))
                .thenReturn(Observable.error(Throwable()))

        remoteDataSource.searchRepos(TEST_QUERY, 1)
                .test()
                .assertError(Throwable::class.java)

        verify(gitHubAPI).searchRepos(query = TEST_QUERY)
    }

}