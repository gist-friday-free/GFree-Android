package org.mjstudio.gfree.domain.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.mjstudio.gfree.domain.entity.ClassData
import javax.inject.Inject

class ClassDataSourceFactory @Inject constructor(
        private val dataSource : ClassDataSource
) : DataSource.Factory<String, ClassData>() {

    private val classDataLiveData = MutableLiveData<ClassDataSource>()

    override fun create(): DataSource<String, ClassData> {
        classDataLiveData.postValue(dataSource)
        return dataSource
    }
}