package com.rod.uidemo.hotsearch

import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.activity_hot_search.*

/**
 *
 * @author Rod
 * @date 2018/10/30
 */
class HotSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_hot_search)

        pager.adapter = MyPagerAdapter(this)
    }

    class MyPagerAdapter(private val context: Context) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val page = createPage(position)
            container.addView(page, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return page
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Title $position"
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            if (obj is View) {
                container.removeView(obj)
            }
        }

        private fun createPage(pos: Int): View {
            val recyclerView = RecyclerView(context)
            recyclerView.adapter = MyRvAdapter(context, pos)
            recyclerView.layoutManager = LinearLayoutManager(context)
            return recyclerView
        }
    }

    class MyRvAdapter(private val context: Context, private val pos: Int)
        : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val data = getData(pos)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, parent, false)
            return MHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MHolder) {
                holder.textView.text = data[position]
            }
        }

        inner class MHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(android.R.id.text1)
        }

        private fun getData(pos: Int) = Array(if (pos==1) 10 else 100) { "List Of $pos, index $it" }
    }
}