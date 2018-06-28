package com.rod.uidemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.rod.uidemo.flow.FlowLayoutActivity
import com.rod.uidemo.sticky.StickyActivity
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

                button("flowlayout") {
                    onClick { simpleStartActivity(FlowLayoutActivity::class.java) }
                }

                button("sticky") {
                    onClick { simpleStartActivity(StickyActivity::class.java) }
                }
            }
        }
    }

    private fun simpleStartActivity(activityClass: Class<out Activity>) {
        startActivity(Intent(this, activityClass))
    }
}
