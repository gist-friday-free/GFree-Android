package org.mjstudio.gfree.domain.repository


interface ServerRepository {
    suspend fun isServerRunning(): Boolean
}