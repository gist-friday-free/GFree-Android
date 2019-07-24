package org.mjstudio.ggonggang.util

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.mjstudio.ggonggang.R

object PermissionUtil {

    private const val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PERMISSION_ARRAY: MutableCollection<String> = mutableListOf(
            PERMISSION_WRITE_EXTERNAL_STORAGE
    )

    /**
     * 권한들이 허용되었는지 검사를 요청하는 함수
     *
     * @param activity Dexter 라이브러리가 사용할 Activity 객체
     * @param listener 권한들에 대한 검사가 완료되었을 때 콜백을 처리할 리스너
     */
    fun requestPermissions(activity: Activity, listener: PermissionListener) {
        val callbackListener: MultiplePermissionsListener = object : BaseMultiplePermissionsListener() {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                // 모든 권한이 허가되었다면,
                if (report.areAllPermissionsGranted()) {
                    listener.onPermissionGranted()
                } else if (report.isAnyPermissionPermanentlyDenied) {
                    val message = activity.getString(R.string.permission_permanently_deny)
                    listener.onAnyPermissionsPermanentlyDeined(message)
                } else {
                    val message = activity.getString(R.string.permission_deny_message)
                    listener.onPermissionShouldBeGranted(activity, message)
                }
            }
        }

        Dexter.withActivity(activity).withPermissions(PERMISSION_ARRAY)
                .withListener(callbackListener)
                .check()
    }
}

interface PermissionListener {
    /**
     * 모든 권한이 허용되었다.
     */
    fun onPermissionGranted()

    /**
     *
     */
    fun onPermissionShouldBeGranted(activity: Activity, message: String)

    /**
     *
     */
    fun onAnyPermissionsPermanentlyDeined(message: String)
}


///**
// * Dexter를 이용한 권한 요청 및 콜백에 관련된 유틸
// *
// * dependencies{
// *
// *      implementation 'com.karumi:dexter:5.0.0'
// *
// * }
// *
// *
// * @author MJStudio
// * @see android.Manifest.permission
// */
//object PermissionUtil {
//
//
//    /**
//     * 권한들이 허용되었는지 검사를 요청하는 함수
//     *
//     * @param activity Dexter 라이브러리가 사용할 Activity 객체
//     * @param listener 권한들에 대한 검사가 완료되었을 때 콜백을 처리할 리스너
//     * @param permissions 요청하는 권한 목록 [android.Manifest.permission]
//     */
//    fun requestPermissions(activity: Activity, listener: PermissionListener, permissions : MutableCollection<String>) {
//
//        val callbackListener: MultiplePermissionsListener = object : BaseMultiplePermissionsListener() {
//
//            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//
//                val deniedPermissions = report.deniedPermissionResponses.map { it.permissionName }
//                val permanentlyDeniedPermissions = report.deniedPermissionResponses.filter { it.isPermanentlyDenied }.map{it.permissionName}
//
//                // 모든 권한이 허가되었다면,
//                if (report.areAllPermissionsGranted()) {
//                    listener.onPermissionGranted()
//                }
//                // 권한 중에 영구적으로 거부된 권한이 있다면
//                else if (report.isAnyPermissionPermanentlyDenied) {
//                    listener.onAnyPermissionsPermanentlyDeined(deniedPermissions,permanentlyDeniedPermissions)
//                }
//                // 권한 중에 거부된 권한이 있다면
//                else {
//                    listener.onPermissionShouldBeGranted(deniedPermissions)
//                }
//            }
//
//        }
//
//        /**
//         * Dexter로 activity를 이용한 권한 요청
//         */
//        Dexter.withActivity(activity).withPermissions(permissions)
//                .withListener(callbackListener)
//                .check()
//    }
//}
//
//interface PermissionListener {
//    /**
//     * 모든 권한이 허용되었다.
//     */
//    fun onPermissionGranted()
//
//    /**
//     * 일부 권한이 거부되었다.
//     *
//     * @param deniedPermissions 거부된 권한 목록
//     */
//    fun onPermissionShouldBeGranted(deniedPermissions : List<String>)
//
//    /**
//     * 일부 권한이 영구적으로 거부되었다.
//     *
//     * @param deniedPermissions 거부된 권한 목록
//     * @param permanentDeniedPermissions 영구적으로 거부된 권한 목록
//     */
//    fun onAnyPermissionsPermanentlyDeined(deniedPermissions : List<String>,permanentDeniedPermissions : List<String>)
//}