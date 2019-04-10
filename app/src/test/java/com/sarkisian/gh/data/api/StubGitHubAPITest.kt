package com.sarkisian.gh.data.api

import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import com.sarkisian.gh.util.GsonUtil
import com.sarkisian.gh.util.TestUtil.Constant.REPO_NAME
import com.sarkisian.gh.util.TestUtil.Constant.USER_NAME
import com.squareup.okhttp.mockwebserver.Dispatcher
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.squareup.okhttp.mockwebserver.RecordedRequest
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class StubGitHubAPITest {

    lateinit var mockWebServer: MockWebServer
    lateinit var gitHubAPI: GitHubAPI
    lateinit var gsonUtil: GsonUtil

    @Before
    fun setUp() {
        gsonUtil = GsonUtil()
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path.contains("/users/$USER_NAME/repos") -> MockResponse()
                            .setResponseCode(200)
                            .setBody(gsonUtil.readString("json/repos"))

                    request.path.contains("/repos/$USER_NAME/$REPO_NAME") -> MockResponse()
                            .setResponseCode(200)
                            .setBody(gsonUtil.readString("json/repo"))

                    request.path.contains("/search/repositories") -> MockResponse()
                            .setResponseCode(200)
                            .setBody(gsonUtil.readString("json/search"))

                    else -> MockResponse()
                            .setResponseCode(404)
                }
            }
        }
        mockWebServer.setDispatcher(dispatcher)
        val baseUrl = mockWebServer.url("/")
        gitHubAPI = ApiFactory.getGitHubAPI(baseUrl.toString())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getRepoList_Success() {
        val testObserver = TestObserver<List<Repo>>()
        gitHubAPI.getRepos(USER_NAME).subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue({ it.isNotEmpty() })

        val actual = testObserver.values()[0] as List<Repo>
        assertEquals(30, actual.size)
        assertEquals(REPO_NAME, actual[0].name)
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
        Assert.assertNotNull(actual.items)
    }

    @Test
    fun searchRepo_Fail() {
    }
}