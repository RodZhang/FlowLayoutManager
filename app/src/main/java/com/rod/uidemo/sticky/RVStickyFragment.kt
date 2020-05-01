package com.rod.uidemo.sticky

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.uidemo.R
import com.rod.uidemo.sticky.entity.Group
import com.rod.uidemo.sticky.entity.People
import kotlinx.android.synthetic.main.fragment_rv_sticky.*

/**
 *
 * @author Rod
 * @date 2020/5/1
 */
class RVStickyFragment : Fragment() {

    companion object {
        const val TAG = "RVStickyFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rv_sticky, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactsAdapter = ContactsAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(StickyItemDecoration(object : StickyItemDecoration.StickyCallback {
            override fun isSticky(adapterPos: Int): Boolean {
                return contactsAdapter.isGroup(adapterPos)
            }

        }))
        recyclerView.adapter = contactsAdapter
    }

    class ContactsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        companion object {
            const val TYPE_GROUP = 1
            const val TYPE_PEOPLE = 2
        }

        private val data = ArrayList<Any>().apply {
            contacts.map { it as Group }
                    .forEach {
                        add(it)
                        addAll(it.peopleList)
                    }
        }

        fun isGroup(pos: Int): Boolean {
            if (pos < 0 || pos >= data.size) {
                return false
            }
            return data[pos] is Group
        }

        override fun getItemViewType(position: Int): Int {
            return if (data[position] is Group) TYPE_GROUP else TYPE_PEOPLE
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == TYPE_GROUP) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
                GroupHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sticky_child, parent, false)
                PeopleHolder(view)
            }
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is GroupHolder) {
                bindGroup(holder, data[position] as Group)
            } else {
                bindPeople(holder as PeopleHolder, data[position] as People)
            }
        }

        private fun bindGroup(holder: GroupHolder, group: Group) {
            holder.groupTitle.text = group.groupName
        }

        private fun bindPeople(holder: PeopleHolder, people: People) {
            holder.name.text = people.name
        }

    }

    class GroupHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val groupTitle: TextView = view.findViewById(R.id.groupTitle)
    }
    class PeopleHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
    }
}

val contacts = ArrayList<Any>().apply {
    add(Group("C", ArrayList<People>().apply {
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
        add(People("陈xx"))
    }))
    add(Group("D", ArrayList<People>().apply {
        add(People("杜xx"))
        add(People("杜xx"))
        add(People("杜xx"))
        add(People("杜xx"))
        add(People("杜xx"))
        add(People("杜xx"))
        add(People("杜xx"))
        add(People("杜xx"))
    }))
    add(Group("F", ArrayList<People>().apply {
        add(People("付xx"))
        add(People("付xx"))
        add(People("付xx"))
        add(People("付xx"))
    }))
    add(Group("G", ArrayList<People>().apply {
        add(People("郭xx"))
        add(People("郭xx"))
        add(People("郭xx"))
        add(People("郭xx"))
        add(People("郭xx"))
        add(People("郭xx"))
        add(People("郭xx"))
    }))
    add(Group("W", ArrayList<People>().apply {
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
        add(People("王xx"))
    }))
    add(Group("X", ArrayList<People>().apply {
        add(People("肖xx"))
        add(People("肖xx"))
        add(People("肖xx"))
        add(People("肖xx"))
        add(People("肖xx"))
        add(People("肖xx"))
        add(People("肖xx"))
        add(People("肖xx"))
        add(People("肖xx"))
    }))
    add(Group("Z", ArrayList<People>().apply {
        add(People("张xx"))
        add(People("张xx"))
        add(People("张xx"))
        add(People("张xx"))
        add(People("张xx"))
        add(People("张xx"))
        add(People("张xx"))
        add(People("张xx"))
        add(People("张xx"))
    }))
}