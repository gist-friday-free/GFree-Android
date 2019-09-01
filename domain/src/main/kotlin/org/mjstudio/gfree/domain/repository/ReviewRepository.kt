package org.mjstudio.gfree.domain.repository

import io.reactivex.Completable
import org.mjstudio.gfree.domain.dto.ReviewDTO

interface ReviewRepository {
    fun createReview(review: ReviewDTO): Completable
}