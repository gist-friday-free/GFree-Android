package org.mjstudio.gfree.domain.repository

import org.mjstudio.gfree.domain.dto.PlayStoreDTO

interface PlayStoreRepository {
    suspend fun getPlayStoreInfo(): PlayStoreDTO
}