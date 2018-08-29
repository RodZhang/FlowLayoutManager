package com.rod.uidemo.flowlayout

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import org.jetbrains.anko.*
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
        val button = TextView(this@FlowLayoutActivity2)
        button.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        button.text = "D"
        button.setPadding(40, 0, 40, 0)
        button.gravity = Gravity.CENTER
        button.textColor = Color.parseColor("#FFFFFF")
        button.backgroundColor = Color.parseColor("#FF0000")
//        flowLayout.setMeasurer(NormalMeasurer())
        button.onClick {
            flowLayout.setNeedFold(false)
            flowLayout.requestLayout()
        }
        flowLayout.setMaxLineCount(3)
        flowLayout.setNeedFold(true)
        flowLayout.setFoldLineCount(2)
        flowLayout.setSpecialView(button)
        flowLayout.setPadding(20, 20, 20, 20)
        verticalLayout {
            val edit = editText()
            linearLayout {
                button("limit") {
                    layoutParams = LayoutParams(0, WRAP_CONTENT, 1F)
                    onClick {
//                        flowLayout.setMeasurer(specialMeasurer)
                    }
                }

                button("unlimit") {
                    layoutParams = LayoutParams(0, WRAP_CONTENT, 1F)
                    onClick {
                        flowLayout.setMeasurer(NormalMeasurer())
                    }
                }
            }
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
            }
            linearLayout {
                button("ascend") {
                    layoutParams = LayoutParams(0, WRAP_CONTENT, 1F)
                    onClick {
                        flowLayout.removeAllViews()
                        flowLayout.setNeedFold(true)
                        fillView(flowLayout, false)
                    }
                }

                button("reverse") {
                    layoutParams = LayoutParams(0, WRAP_CONTENT, 1F)
                    onClick {
                        flowLayout.removeAllViews()
                        flowLayout.setNeedFold(true)
                        fillView(flowLayout, true)
                    }
                }
            }

            val scrollView = scrollView {
                layoutParams = LayoutParams(MATCH_PARENT, 0, 1F)
            }
            flowLayout.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            flowLayout.backgroundColor = Color.parseColor("#40000000")
            scrollView.addView(flowLayout)
        }
    }

    private fun fillView(flowLayout: FlowLayout, reverse: Boolean) {
        (0 until 30).map { it -> (0..it).joinToString { "#" } }
//                .shuffled()
                .forEach { addItem(flowLayout, it, reverse) }
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