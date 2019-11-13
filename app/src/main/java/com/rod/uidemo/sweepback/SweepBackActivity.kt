package com.rod.uidemo.sweepback

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.activity_sweepback.*
import org.jetbrains.anko.textColor

/**
 *
 * @author Rod
 * @date 2019/5/11
 */
class SweepBackActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sweepback)
        pager.adapter = MyAdapter()
    }

    class MyAdapter: PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = TextView(container.context)
            view.text = "item_$position"
            container.addView(view)
            view.textColor = Color.BLACK
            view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            view.gravity = Gravity.CENTER
            return view
        }

        override fun isViewFromObject(view: View, `object`: Any) = view === `object`

        override fun getCount() = 20

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}