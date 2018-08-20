package com.rod.uidemo.flow

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.uidemo.R
import org.jetbrains.anko.verticalLayout

/**
 *
 * @author Rod
 * @date 2018/8/20
 */
class FlowActivity : Activity() {
    companion object {
        const val SMALL = 0
        const val NORMAL = 1
        const val BIG = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            val recyclerView = RecyclerView(context)

        }
    }

    inner class FlowAdapter : RecyclerView.Adapter<FlowAdapter.Holder>() {

        var mData = ArrayList<FlowItem>()
            set(value) {
                field.clear()
                field.addAll(value)
                notifyDataSetChanged()
            }

        override fun getItemViewType(position: Int) = mData[position].style

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowAdapter.Holder {
            val resId = when (viewType) {
                SMALL -> R.layout.item_small
                NORMAL -> R.layout.item_tag
                else -> R.layout.item_big
            }

            return Holder(LayoutInflater.from(parent.context).inflate(resId, parent, false))
        }

        override fun getItemCount() = mData.size

        override fun onBindViewHolder(holder: FlowAdapter.Holder, position: Int) {
            holder.mTagText.text = mData[position].text
        }

        inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
            val mTagText = view.findViewById<TextView>(R.id.tagText)
        }
    }

    data class FlowItem(@ItemSize val style: Int, val text: String)

    @Target(AnnotationTarget.FIELD, AnnotationTarget.TYPE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SMALL, NORMAL, BIG)
    annotation class ItemSize
}