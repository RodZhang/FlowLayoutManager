package com.rod.uidemo

import android.os.Bundle
import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rod.uidemo.data.DataUnit
import kotlinx.android.synthetic.main.fragment_fans.*

/**
 *
 * @author Rod
 * @date 2018/9/7
 */
class FansFragment : BaseDataFragment<FansFragment.FansData>() {
    companion object {
        const val TAG = "FansFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fans, container, false)
    }

    override fun onNewCreate() {
        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1)
        adapter.addAll(getData())
        listView.adapter = adapter
    }

    private fun getData(): List<String> {
        return (0 until 50).map { "Item $it" }
    }

    override fun onRestoreState() {
    }

    @NonNull
    override fun generateDataUnit() = FansData()

    @NonNull
    override fun generatePageId() = "fans_${System.currentTimeMillis()}"

    class FansData : DataUnit {
    }
}