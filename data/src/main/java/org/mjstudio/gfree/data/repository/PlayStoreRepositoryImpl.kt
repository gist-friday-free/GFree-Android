package org.mjstudio.gfree.data.repository

import io.reactivex.Single
import org.mjstudio.gfree.data.api.PlayStoreAPI
import org.mjstudio.gfree.domain.dto.PlayStoreDTO
import org.mjstudio.gfree.domain.repository.PlayStoreRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayStoreRepositoryImpl @Inject constructor(private val playStoreAPI: PlayStoreAPI) : PlayStoreRepository {
    override fun getPlayStoreInfo(): Single<PlayStoreDTO> {
        return playStoreAPI.getPlayStoreInfo()
    }
}