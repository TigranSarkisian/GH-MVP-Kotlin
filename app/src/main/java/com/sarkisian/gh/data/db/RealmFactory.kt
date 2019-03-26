package com.sarkisian.gh.data.db


import android.content.Context
import com.sarkisian.gh.BuildConfig
import com.sarkisian.gh.data.entity.Repo
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.rx.RealmObservableFactory

object RealmFactory {

    private const val REALM_DB_NAME = "gh.realm"
    private const val SCHEMA_VERSION = 1

    @JvmStatic
    fun init(context: Context) {
        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .name(REALM_DB_NAME)
            .schemaVersion(SCHEMA_VERSION.toLong())
            .deleteRealmIfMigrationNeeded()
            .rxFactory(RealmObservableFactory())
            .build()

        Realm.setDefaultConfiguration(config)

        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ERROR)
        }
    }

    @JvmStatic
    fun <T> insertOrUpdateRepos(data: T) {
        when (data) {
            is Repo -> Realm.getDefaultInstance().use { realm ->
                realm.executeTransaction { r -> r.insertOrUpdate(data as Repo) }
            }
            is List<*> -> Realm.getDefaultInstance().use { realm ->
                realm.executeTransaction { r -> r.insertOrUpdate(data.filterIsInstance<Repo>()) }
            }
        }
    }

    @JvmStatic
    fun deleteRepo(repo: Repo) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { r ->
                val result = r.where(Repo::class.java)
                    .equalTo(Repo.REPO_ID, repo.id)
                    .findAll()

                result.deleteAllFromRealm()
            }
        }
    }

    @JvmStatic
    fun getRepos(): MutableList<Repo> {
        var repoList: MutableList<Repo> = mutableListOf()

        Realm.getDefaultInstance().use { realm ->
            val realmRepoList = realm
                .where(Repo::class.java)
                .findAll()

            repoList = realm.copyFromRealm(realmRepoList)

            repoList.sortByDescending { repo: Repo? -> repo?.id }
        }

        return repoList
    }

    @JvmStatic
    fun getFavoriteRepos(): MutableList<Repo> {
        var repoList: MutableList<Repo> = mutableListOf()

        Realm.getDefaultInstance().use { realm ->
            val realmRepoList = realm
                .where(Repo::class.java)
                .equalTo(Repo.REPO_IS_FAVORITE, true)
                .findAll()

            repoList = realm.copyFromRealm(realmRepoList)

            repoList.sortByDescending { repo: Repo? -> repo?.id }
        }

        return repoList
    }

    @JvmStatic
    fun getRepo(repoName: String): Repo? {
        var repo: Repo? = null

        Realm.getDefaultInstance().use { realm ->
            val realmRepo = realm.where(Repo::class.java)
                .equalTo(Repo.REPO_NAME, repoName)
                .findFirst()

            if (realmRepo != null) {
                repo = realm.copyFromRealm(realmRepo)
            }
        }

        return repo
    }
}
