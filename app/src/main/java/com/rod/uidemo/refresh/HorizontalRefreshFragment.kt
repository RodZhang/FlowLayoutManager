package com.rod.uidemo.refresh

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.uidemo.R
import com.scwang.smartrefresh.layout.util.DensityUtil
import kotlinx.android.synthetic.main.fragment_horizontal_refresh.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.ems
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textView

/**
 *
 * @author Rod
 * @date 2019/1/11
 */
class HorizontalRefreshFragment : Fragment() {

    companion object {
        const val TAG = "HorizontalRefreshFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_horizontal_refresh, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = MAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    class MAdapter : RecyclerView.Adapter<MAdapter.ItemHolder>() {

        private val mData = IntArray(8)

        init {
            (0 until 8).forEach { mData[it] = if (it % 2 == 0) Color.RED else Color.GREEN }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = TextView(parent.context)
            view.layoutParams = RecyclerView.LayoutParams(DensityUtil.dp2px(200F), matchParent)
            view.gravity = Gravity.CENTER
            view.textSize = 24F

            return ItemHolder(view)
        }

        override fun getItemCount(): Int {
            return mData.size
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.itemView.setBackgroundColor(mData[position])
            (holder.itemView as TextView).text = "Item $position"
        }

        class ItemHolder(view: TextView) : RecyclerView.ViewHolder(view) {

        }
    }
}