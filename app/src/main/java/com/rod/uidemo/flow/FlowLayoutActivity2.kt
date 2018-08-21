package com.rod.uidemo.flow

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.internals.AnkoInternals.addView
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author Rod
 * @date 2018/8/21
 */
class FlowLayoutActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flowLayout = FlowLayout(this)
        val button = Button(this@FlowLayoutActivity2)
        button.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        button.text = "D"
        flowLayout.setSpecialView(button)
//        flowLayout.setMaxLineCount(3)
//        flowLayout.setFoldLineCount(2)
//        flowLayout.setNeedFold(true)
        flowLayout.setPadding(20, 20, 20, 20)
        button.onClick {
            flowLayout.setNeedFold(false)
            flowLayout.removeView(button)
            flowLayout.requestLayout()
        }
        verticalLayout {
            val edit = editText()

            linearLayout {
                button("Add") {
                    layoutParams = LayoutParams(0, WRAP_CONTENT, 1F)
                    onClick {
                        if (TextUtils.isEmpty(edit.text)) {
                            return@onClick
                        }
                        addItem(flowLayout, edit.text.toString())
                        edit.text.clear()
                    }
                }

                button("Clear") {
                    layoutParams = LayoutParams(0, WRAP_CONTENT, 1F)
                    onClick {
                        flowLayout.removeAllViews()
                        flowLayout.setNeedFold(true)
                    }
                }

                button("test") {
                    layoutParams = LayoutParams(0, WRAP_CONTENT, 1F)
                    onClick {
                        (0 until 16).forEach {
                            val str = (0..it).map { "#" }.joinToString()
                            addItem(flowLayout, str)
                        }
                    }
                }
            }

            flowLayout.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            flowLayout.backgroundColor = Color.parseColor("#40000000")
            addView(flowLayout)
        }
    }

    private fun addItem(flowLayout: FlowLayout, item: String, reverse: Boolean = true) {
        val tv = TextView(flowLayout.context)
        tv.backgroundColor = Color.parseColor("#0000ff")
        tv.textColor = Color.parseColor("#FFFFFF")
        tv.singleLine = true
        tv.ellipsize = TextUtils.TruncateAt.END
        tv.text = item
        if (reverse) {
            flowLayout.addView(tv, 0)
        } else {
            flowLayout.addView(tv)
        }
    }
}