package org.mjstudio.ggonggang.repository

import org.junit.Before
import org.junit.Test
import org.mjstudio.gfree.domain.repository.ReviewRepository

class ReviewRepositoryTest {
    lateinit var repository: ReviewRepository

    @Before
    fun setup() {
        repository = DaggerAppComponent
    }

    @Test
    fun `TEST!`() {
    }
}