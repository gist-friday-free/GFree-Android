package org.mjstudio.ggonggang.ui

import android.os.Bundle
import androidx.navigation.findNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.mjstudio.gfree.domain.common.GeneralMsg
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.R.layout
import org.mjstudio.ggonggang.common.showSnackbar

class MainActivity : DaggerAppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private var snackbarShow = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
    }


    /**
     * 앱을 종료하려 할 때, 스낵바가 올라오며, 한번 더 Back 버튼을 눌러야 종료가 되게 한다.
     */
    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)


        if(navController.currentDestination?.id == R.id.mainFragment) {
            if (!snackbarShow) {
                main_root_container.showSnackbar(GeneralMsg.EXIT.msg, null, {
                    snackbarShow = true
                }, {
                    snackbarShow = false
                })
            } else {
                super.onBackPressed()
            }
        }else {

            super.onBackPressed()
        }
    }
}
