package org.mjstudio.gfree.domain.constant

object NativeConstant {
    val API_URL: String by lazy { getAPIUrl() }

    init {
        System.loadLibrary("constants")
    }

    private external fun getAPIUrl(): String
}