package org.mjstudio.gfree.domain.repository

import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.PlayStoreDTO

interface PlayStoreRepository {
    fun getPlayStoreInfo(): Single<PlayStoreDTO>
}