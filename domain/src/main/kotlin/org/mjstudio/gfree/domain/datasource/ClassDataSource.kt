package org.mjstudio.gfree.domain.datasource

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import javax.inject.Inject

class ClassDataSource @Inject constructor(
        private val classDataRepository: ClassDataRepository
) : PageKeyedDataSource<String,ClassData>() {

    private val TAG = ClassDataSource::class.java.simpleName

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, ClassData>) {
        GlobalScope.launch {
            try {
                val response = classDataRepository.getClassDataListWithPage(Constant.CURRENT_YEAR, Constant.CURRENT_SEMESTER, 1)

                callback.onResult(response.classes.toEntity().toList(), response.previous, response.next)
            }catch(t : Throwable) {
                debugE(TAG,t)
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, ClassData>) {
        val pageString = params.key

        GlobalScope.launch {
            debugE(TAG,pageString)
            val nextPageInt = pageString.split('&').also{debugE(it)}.filter {
                it.contains("page=")
            }.also{debugE(it)}.first().split('?')[1].also{debugE(it)} .filter {
                (0..9).contains(it.toInt() - '0'.toInt())
            }.also{debugE(it)}.toInt()

            val response = classDataRepository.getClassDataListWithPage(Constant.CURRENT_YEAR,Constant.CURRENT_SEMESTER,nextPageInt)

            callback.onResult(response.classes.toEntity().toList(),response.next)
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, ClassData>) {
    }
}

