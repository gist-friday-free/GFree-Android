//
// Created by 문명주 on 24/05/2019.
//

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

    JNIEXPORT jstring JNICALL
    Java_org_mjstudio_gfree_domain_constant_NativeConstant_getAPIUrl(JNIEnv* JEnv, jobject thiz) {
        return JEnv->NewStringUTF("http://172.30.1.51:8000");
    }

#ifdef __cplusplus
}
#endif

