package com.sarkisian.gh.util.rxbus

import com.sarkisian.gh.data.entity.Repo


sealed class RxEvent {
    class UpdateRepoList(val gitHubUser: String) : RxEvent()
    class DeleteRepo(val repo: Repo) : RxEvent()
    class UpdateRepo(val repo: Repo) : RxEvent()
    class FavoriteRepo(val repo: Repo) : RxEvent()
}