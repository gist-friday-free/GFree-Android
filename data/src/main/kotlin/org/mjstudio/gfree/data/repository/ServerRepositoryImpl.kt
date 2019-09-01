package org.mjstudio.gfree.data.repository

import io.reactivex.Single
import org.mjstudio.gfree.data.api.ServerAPI
import org.mjstudio.gfree.domain.repository.ServerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerRepositoryImpl @Inject constructor(private val serverAPI: ServerAPI) : ServerRepository {
    override fun isServerRunning(): Single<Boolean> {
        return serverAPI.isServerRunning()
    }
}