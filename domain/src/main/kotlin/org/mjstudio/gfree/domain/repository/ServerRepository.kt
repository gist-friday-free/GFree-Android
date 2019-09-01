package org.mjstudio.gfree.domain.repository

import io.reactivex.Single

interface ServerRepository {
    fun isServerRunning(): Single<Boolean>
}