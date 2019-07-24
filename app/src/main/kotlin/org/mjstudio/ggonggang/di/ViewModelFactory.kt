package org.mjstudio.ggonggang.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val creators: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            creators[modelClass]?.get() as T
}

@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
@Retention(RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)