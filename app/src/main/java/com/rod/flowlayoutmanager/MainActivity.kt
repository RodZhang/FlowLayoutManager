package com.rod.flowlayoutmanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = FlowLayoutManager()
        recyclerView.addItemDecoration(MyItemDecoration())
        recyclerView.adapter = TagAdapter(getData())
    }

    private fun getData(): List<String> {
        val prefixArr = arrayOf("aa", "bbbbbb", "ddddd")
        return (0 until 40).map { "${prefixArr[(Math.random() * prefixArr.size).toInt()]}-$it" }
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
