package com.sarkisian.gh.data.api

import com.sarkisian.gh.data.api.ApiFactory.BASE_URL
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import com.sarkisian.gh.util.TestUtil.Constant.REPO_NAME
import com.sarkisian.gh.util.TestUtil.Constant.USER_NAME
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class GitHubAPITest {

    lateinit var gitHubAPI: GitHubAPI

    @Before
    fun setUp() {
        gitHubAPI = ApiFactory.getGitHubAPI(BASE_URL)
    }

    @Test
    fun baseUrl_Correct() {
        assertEquals(BASE_URL, "https://api.github.com/")
    }

    @Test
    fun getRepoList_Success() {
        val testObserver = TestObserver<List<Repo>>()
        gitHubAPI.getRepos(USER_NAME).subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it.isNotEmpty() }

        val actualRepos = testObserver.values()[0] as ArrayList<Repo>
        assertEquals(30, actualRepos.size)
        assertEquals(REPO_NAME, actualRepos[0].name)
    }

    @Test
    fun getRepoList_Fail() {
        val testObserver = TestObserver<List<Repo>>()
        gitHubAPI.getRepos("not_existing_user").subscribe(testObserver)
        assertFalse(testObserver.errors().isEmpty())
    }

    @Test
    fun getRepo_Success() {
        val testObserver = TestObserver<Repo>()
        gitHubAPI.getRepo(USER_NAME, REPO_NAME).subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val actual = testObserver.values()[0] as Repo
        assertEquals(REPO_NAME, actual.name)
    }

    @Test
    fun getRepo_Fail() {
        val testObserver = TestObserver<Repo>()
        gitHubAPI.getRepo("not_existing_user", "not_existing_repo").subscribe(testObserver)
        assertFalse(testObserver.errors().isEmpty())
    }

    @Test
    fun searchRepo_Success() {
        val testObserver = TestObserver<SearchRequest>()
        gitHubAPI.searchRepos(query = "google").subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val actual = testObserver.values()[0] as SearchRequest
        assertNotNull(actual.items)
    }

    @Test
    fun searchRepo_Fail() {
        val testObserver = TestObserver<SearchRequest>()
        gitHubAPI.searchRepos(query = "").subscribe(testObserver)
        assertFalse(testObserver.errors().isEmpty())
    }
}