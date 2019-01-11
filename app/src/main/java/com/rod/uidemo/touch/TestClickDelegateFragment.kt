package com.rod.uidemo.touch

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout


/**
 *
 * @author Rod
 * @date 2018/11/26
 */
class TestClickDelegateFragment : Fragment() {
    companion object {
        const val TAG = "TestClickDelegateFragment"
    }
    private lateinit var mPagerMsg: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            verticalLayout {
                val clickView = textView("click here"){
                    backgroundColor = Color.parseColor("#ffffff")
                }.lparams(matchParent, 200)
                val msgText = textView("hehe")
                val clickDelegate = ClickDelegate(clickView, object : ClickDelegate.OnClickListener {
                    override fun onClick(view: View, dx: Int, dy: Int, ux: Int, uy: Int) {
                        msgText.text = "{dx=$dx, dy=$dy, ux=$ux, uy=$uy}"
                    }
                })

                viewPager() {
                    adapter = MyPagerAdapter()
                }.lparams(matchParent, 500)

                mPagerMsg = textView("viewPager")
            }
        }.view
    }


    inner class MyPagerAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val textView = TextView(container.context)
            textView.text = "Content of $position"
            val clickDelegate = ClickDelegate(textView, object : ClickDelegate.OnClickListener {
                override fun onClick(view: View, dx: Int, dy: Int, ux: Int, uy: Int) {
                    mPagerMsg.text = "{dx=$dx, dy=$dy, ux=$ux, uy=$uy}"
                }
            })

            container.addView(textView)
            return textView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeViewAt(position)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return 6
        }

    }
}