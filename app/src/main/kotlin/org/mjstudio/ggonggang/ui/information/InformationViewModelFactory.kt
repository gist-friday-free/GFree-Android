package org.mjstudio.ggonggang.ui.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.ReviewRepository
import org.mjstudio.gfree.domain.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class InformationViewModelFactory(
        private val classDataRepository: ClassDataRepository,
        private val reviewRepository: ReviewRepository,
        private val userRepository: UserRepository,
        private val authRepository: FirebaseAuthRepository,
        private val classData : ClassData
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InformationViewModel(
                classDataRepository,
                reviewRepository,
                userRepository,
                authRepository,
                classData
        ) as T
    }
}