package org.mjstudio.gfree.data.repository

import org.mjstudio.gfree.data.api.ServerAPI
import org.mjstudio.gfree.domain.repository.ServerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerRepositoryImpl @Inject constructor(private val serverAPI: ServerAPI) : ServerRepository {
    override suspend fun isServerRunning(): Boolean {
        return serverAPI.isServerRunning()
    }
}