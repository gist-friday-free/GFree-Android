package org.mjstudio.gfree.domain.repository

import org.mjstudio.gfree.domain.dto.ReviewDTO

interface ReviewRepository {
    suspend fun createReview(review: ReviewDTO)
}