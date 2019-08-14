package com.rod.uidemo.sticky

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.rod.uidemo.common.DataFactory
import org.jetbrains.anko.*

/**
 * No pains, no gains.
 *
 * Created by Rod on 2018/6/28.
 */
class StickyActivity : AppCompatActivity() {

    private lateinit var mListView: ListView
    private lateinit var mStickyContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        frameLayout {
            mListView = listView {
                adapter = TestAdapter(this)
            }

            mStickyContainer = verticalLayout {
                lparams(matchParent, wrapContent)
            }
        }
    }

    private class TestAdapter(private val mListView: ListView) : BaseAdapter() {
        private val mData = DataFactory.getStringList(50)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var contentView = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
            contentView.findViewById<TextView>(android.R.id.text1)
                    .text = mData[position]

            return contentView
        }

        override fun getItem(position: Int): Any {
            return mData[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return mData.size
        }

        private fun updateListViewDividerHeight(height: Int) {
            try {
                val field = mListView.javaClass.getDeclaredField("mDividerHeight")
                field.setInt(mListView, height)
            } catch (e: Exception) {

            }
        }
    }
}