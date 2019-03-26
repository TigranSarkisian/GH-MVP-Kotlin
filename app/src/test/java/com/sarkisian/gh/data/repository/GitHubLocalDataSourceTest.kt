package com.sarkisian.gh.data.repository

import com.sarkisian.gh.data.db.RealmFactory
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.util.TestUtil
import com.sarkisian.gh.util.TestUtil.Constant.USER_NAME
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.api.mockito.PowerMockito.verifyStatic
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(RealmFactory::class)
class GitHubLocalDataSourceTest {

    lateinit var localDataSource: GitHubLocalDataSource
    lateinit var repoList: MutableList<Repo>
    lateinit var repo: Repo

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockStatic(RealmFactory::class.java)
        localDataSource = GitHubLocalDataSource()
        repoList = TestUtil.composeStubGitHubRepoList()
        repo = TestUtil.composeStubGitHubRepo()
    }

    @Test
    fun getRepo() {
        `when`(RealmFactory.getRepo(TestUtil.Constant.REPO_NAME)).thenReturn(repo)

        localDataSource.getRepo(USER_NAME, TestUtil.Constant.REPO_NAME)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(repo)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.getRepo(TestUtil.Constant.REPO_NAME)
    }

    @Test
    fun getRepos() {
        `when`(RealmFactory.getRepos()).thenReturn(repoList)

        localDataSource.getRepos(USER_NAME)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(repoList)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.getRepos()
    }

    @Test
    fun addRepo() {
        localDataSource.addRepo(repo)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.insertOrUpdateRepos(repo)
    }

    @Test
    fun addRepos() {
        localDataSource.addRepos(repoList)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.insertOrUpdateRepos(repoList)
    }

    @Test
    fun deleteRepo() {
        localDataSource.deleteRepo(repo)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.deleteRepo(repo)
    }

    @Test
    fun deleteRepos() {
        localDataSource.deleteRepo(repo)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.deleteRepo(repo)
    }

    @Test
    fun editRepo() {
        localDataSource.updateRepo(repo)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.insertOrUpdateRepos(repo)
    }

    @Test
    fun addRepoToFavorites() {
        repo.favorite = true
        localDataSource.addRepoToFavorites(repo)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.insertOrUpdateRepos(repo)
    }

    @Test
    fun removeRepoFromFavorites() {
        repo.favorite = false
        localDataSource.deleteRepoFromFavorites(repo)


        verifyStatic(RealmFactory::class.java)
        RealmFactory.insertOrUpdateRepos(repo)
    }

    @Test
    fun getFavoriteRepos() {
        `when`(RealmFactory.getFavoriteRepos()).thenReturn(repoList)

        localDataSource.getFavoriteRepos(USER_NAME)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(repoList)

        verifyStatic(RealmFactory::class.java)
        RealmFactory.getFavoriteRepos()
    }
}