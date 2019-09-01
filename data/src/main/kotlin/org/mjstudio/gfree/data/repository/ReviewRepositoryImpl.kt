package org.mjstudio.gfree.data.repository

import io.reactivex.Completable
import org.mjstudio.gfree.data.api.ReviewAPI
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.repository.ReviewRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryImpl @Inject constructor(private val reviewAPI: ReviewAPI) : ReviewRepository {

    override fun createReview(review: ReviewDTO): Completable {
        return Completable.create { emitter ->

            reviewAPI.createReview(review)
                    .addSchedulers()
                    .subscribe({
                        emitter.onComplete()
                    }, {
                        emitter.onError(it)
                    })
        }
    }
}