package org.mjstudio.gfree.data.repository

import org.mjstudio.gfree.data.api.PlayStoreAPI
import org.mjstudio.gfree.domain.dto.PlayStoreDTO
import org.mjstudio.gfree.domain.repository.PlayStoreRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayStoreRepositoryImpl @Inject constructor(private val playStoreAPI: PlayStoreAPI) : PlayStoreRepository {
    override suspend fun getPlayStoreInfo():PlayStoreDTO {
        return playStoreAPI.getPlayStoreInfo()
    }
}