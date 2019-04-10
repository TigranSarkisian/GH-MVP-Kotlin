package com.sarkisian.gh.util


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import java.util.*

object TestUtil {

    object Constant {
        val WAIT_TIME: Long = 2000
        val USER_NAME = "google"
        val TEST_QUERY = "google"
        val REPO_NAME = "abpackage"
    }

    @JvmStatic
    fun composeStubGitHubSearchRequest(): SearchRequest =
            SearchRequest(1, true, composeStubGitHubRepoList())

    @JvmStatic
    fun composeStubGitHubRepo(): Repo =
            Repo(1L, "Test name " + 1, "Test description " + 1)

    @JvmStatic
    fun composeStubGitHubRepoList(): MutableList<Repo> {
        return (1..30).mapTo(ArrayList()) {
            Repo(it.toLong(), "Test name " + it, "Test description " + it)
        }
    }
}
