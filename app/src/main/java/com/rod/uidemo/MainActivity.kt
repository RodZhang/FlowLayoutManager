package com.rod.uidemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.rod.uidemo.flow.FlowLayoutActivity
import com.rod.uidemo.flowlayout.FlowLayoutActivity2
import com.rod.uidemo.hotsearch.HotSearchActivity
import com.rod.uidemo.refresh.HorizontalRefreshFragment
import com.rod.uidemo.refresh.ViewPagerFragment
import com.rod.uidemo.sticky.StickyActivity
import com.rod.uidemo.test.RefreshFragment
import com.rod.uidemo.test.TestLayoutAnimFragment
import com.rod.uidemo.touch.TestClickDelegateFragment
import org.jetbrains.anko.button
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollView {
            verticalLayout {
                orientation = LinearLayout.VERTICAL

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
}
