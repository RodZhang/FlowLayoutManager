package com.rod.uidemo

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import com.rod.uidemo.flow.FlowLayoutActivity
import com.rod.uidemo.flowlayout.FlowLayoutActivity2
import com.rod.uidemo.hotsearch.HotSearchActivity
import com.rod.uidemo.limitcount.TestViewFragment
import com.rod.uidemo.mokelocation.MockLocationFragment
import com.rod.uidemo.permission.TestPermission
import com.rod.uidemo.refresh.ViewPagerFragment
import com.rod.uidemo.server.IPCClientFragment
import com.rod.uidemo.sticky.StickyActivity
import com.rod.uidemo.test.RefreshFragment
import com.rod.uidemo.test.TestLayoutAnimFragment
import com.rod.uidemo.testtouch.TestTouchFragment
import com.rod.uidemo.touch.TestClickDelegateFragment
import com.rod.uidemo.touch.TestTouchDispatch
import org.jetbrains.anko.button
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "MainActivity, onCreate")
        scrollView {
            verticalLayout {
                orientation = LinearLayout.VERTICAL

                button("test touch trace").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, TestTouchDispatch::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, TestTouchDispatch.TAG))
                }

                button("test permission").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, TestPermission::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, TestPermission.TAG))
                }

                button("test view").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, TestViewFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, TestViewFragment.TAG))
                }

                button("test touch").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, TestTouchFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, TestTouchFragment.TAG))
                }

                button("mock location").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, MockLocationFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, MockLocationFragment.TAG))
                }

                button("ipc btn").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, IPCClientFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, IPCClientFragment.TAG))
                }

                button("status btn").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, TestBtnStatusFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, TestBtnStatusFragment.TAG))
                }

                button("Horizontal Refresh").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, ViewPagerFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, ViewPagerFragment.TAG))
                }

                button("Hot Search") {
                    onClick { simpleStartActivity(HotSearchActivity::class.java) }
                }

                button("flowlayout") {
                    onClick { simpleStartActivity(FlowLayoutActivity::class.java) }
                }

                button("sticky") {
                    onClick { simpleStartActivity(StickyActivity::class.java) }
                }

                button("flow").onClick {
                    simpleStartActivity(FlowLayoutActivity2::class.java)
                }

                button("test dataRepository").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, FansFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, FansFragment.TAG))
                }

                button("PullToRefresh").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, RefreshFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, RefreshFragment.TAG))
                }

                button("test layoutanim").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, TestLayoutAnimFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, TestLayoutAnimFragment.TAG))
                }

                button("test ClickDelegate").onClick {
                    startActivity(Intent(this@MainActivity, FragmentHostActivity::class.java)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_NAME, TestClickDelegateFragment::class.java.name)
                            .putExtra(FragmentHostActivity.ARGS_FRAGMENT_TAG, TestClickDelegateFragment.TAG))
                }
            }
        }
    }

    private fun simpleStartActivity(activityClass: Class<out Activity>) {
        startActivity(Intent(this, activityClass))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("Lifecycle", "MainActivity, onRestoreInstanceState")
    }

    override fun onStart() {
        super.onStart()

        Log.d("Lifecycle", "MainActivity, onStart")
    }

    override fun onRestart() {
        super.onRestart()

        Log.d("Lifecycle", "MainActivity, onRestart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("Lifecycle", "MainActivity, onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("Lifecycle", "MainActivity, onPause")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        Log.d("Lifecycle", "MainActivity, onSaveInstanceState")
    }

    override fun onStop() {
        super.onStop()

        Log.d("Lifecycle", "MainActivity, onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "MainActivity, onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        Log.d("Lifecycle", "MainActivity, onConfigurationChanged")
    }
}
