package org.mjstudio.gfree.data.repository

import org.mjstudio.gfree.data.api.ReviewAPI
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.repository.ReviewRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryImpl @Inject constructor(private val reviewAPI: ReviewAPI) : ReviewRepository {

    override suspend fun createReview(review: ReviewDTO) {
        reviewAPI.createReview(review)
    }
}