package org.mjstudio.ggonggang.ui.splash

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.common.rxSingleTimer
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.application.GFreeApplication
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.databinding.FragmentSplashBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import org.mjstudio.ggonggang.ui.auth.AuthViewModel
import org.mjstudio.ggonggang.ui.splash.UpdateNeedInfo.FORCE_UPDATE
import org.mjstudio.ggonggang.ui.splash.UpdateNeedInfo.HAVE_UPDATE
import org.mjstudio.ggonggang.ui.splash.UpdateNeedInfo.RECENT
import org.mjstudio.ggonggang.util.PermissionListener
import org.mjstudio.ggonggang.util.PermissionUtil
import org.mjstudio.ggonggang.util.PlayStoreUtil
import java.util.concurrent.TimeUnit.*
import javax.inject.Inject

class SplashFragment : DaggerFragment() {

    private val TAG = SplashFragment::class.java.simpleName

    private var mBinding: FragmentSplashBinding by AutoClearedValue()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var mViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProviders.of(this, viewModelFactory)[AuthViewModel::class.java]

        mBinding = FragmentSplashBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkVersion()
    }

    private fun checkVersion() {
        checkPermissionsAndTryNavigate()
        return

        val packageName = context!!.packageName
        val localVersionName = context!!.packageManager.getPackageInfo(packageName, 0).versionName

        val d = GFreeApplication.instance.playStoreInfoSingle
                .addSchedulers()
                .timeout(1000, MILLISECONDS)
                .subscribe({ storeInfo ->
                    GFreeApplication.instance.playStoreVersionName = storeInfo.versionName

                    when (UpdateNeedInfo.getUpdateNeedInfo(localVersionName, storeInfo.versionName)) {
                        RECENT -> {
                            checkPermissionsAndTryNavigate()
                        }
                        HAVE_UPDATE -> {
                            askUpdate()
                        }
                        FORCE_UPDATE -> {
                            forceUpdate()
                        }
                    }
                }, {
                    debugE(TAG, "CANNOT CHECK VERSION")
                    checkPermissionsAndTryNavigate()
                })
    }

    private fun askUpdate() {
        AlertDialog.Builder(context!!)
                .setTitle(R.string.update_available)
                .setMessage(R.string.update_exist)
                .setPositiveButton(R.string.go_store) { _, _ ->
                    PlayStoreUtil.openPlayStore(context!!)
                    finishActivity()
                }
                .setNegativeButton(R.string.ignore) { _, _ ->
                    checkPermissionsAndTryNavigate()
                }
                .create()
                .show()
    }

    private fun forceUpdate() {
        AlertDialog
                .Builder(context!!)
                .setTitle(R.string.update_available)
                .setMessage(R.string.update_force_need)
                .setPositiveButton(R.string.go_store) { _, _ ->
                    PlayStoreUtil.openPlayStore(context!!)
                    finishActivity() }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    finishActivity() }
                .create()
                .show()
    }

    private val listener = object : PermissionListener {
        override fun onPermissionGranted() {
            navigateNextFragment()
        }


        override fun onPermissionShouldBeGranted(activity: Activity, message: String) {
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.permission_title)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        finishActivity()
                    }.create().show()
        }

        override fun onAnyPermissionsPermanentlyDeined(message: String) {
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.permission_title)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        finishActivity()
                    }.create().show()
        }
    }

    private fun checkPermissionsAndTryNavigate() {
        PermissionUtil.requestPermissions(activity!!, listener)
    }
    private fun navigateNextFragment() {
        rxSingleTimer(500) {
            findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }

    private fun finishActivity() {
        activity?.finish()
    }
}

enum class UpdateNeedInfo {
    RECENT,
    HAVE_UPDATE,
    FORCE_UPDATE
    ;

    companion object {
        fun getUpdateNeedInfo(localVersionName: String, storeVersionName: String): UpdateNeedInfo {

            val localMiddleNumber = localVersionName.take(3).toFloat()
            val storeMiddleNumber = storeVersionName.take(3).toFloat()

            if (localVersionName == storeVersionName)
                return RECENT
            else if (localMiddleNumber == storeMiddleNumber) {
                return HAVE_UPDATE
            } else
                return FORCE_UPDATE
        }
    }
}