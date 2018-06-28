package com.rod.flowlayoutmanager.flow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.flowlayoutmanager.R
import com.rod.flowlayoutmanager.common.DataFactory
import kotlinx.android.synthetic.main.activity_flowlayout.*

class FlowLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flowlayout)

        recyclerView.layoutManager = FlowLayoutManager()
        recyclerView.addItemDecoration(MyItemDecoration())
        recyclerView.adapter = TagAdapter(DataFactory.getStringList(40))
    }

    inner class TagAdapter(val mData: List<String>) : RecyclerView.Adapter<TagAdapter.TagHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
            return TagHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false))
        }

        override fun getItemCount() = mData.size

        override fun onBindViewHolder(holder: TagHolder, position: Int) {
            holder.mTagTv.text = mData[position]
        }

        inner class TagHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mTagTv = view.findViewById<TextView>(R.id.tagText)
        }
    }
}
