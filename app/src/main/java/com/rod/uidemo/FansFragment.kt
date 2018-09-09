package com.rod.uidemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_fans.*

/**
 *
 * @author Rod
 * @date 2018/9/7
 */
class FansFragment : BaseDataFragment<FansPresenter.FansData, FansPresenter>(), FansConstract.View {

    companion object {
        const val TAG = "FansFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fans, container, false)
    }

    private val mAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.adapter = mAdapter
    }

    override fun generatePresenter() = FansPresenter(this)

    override fun setData(data: List<String>) {
        mAdapter.clear()
        mAdapter.addAll(data)
    }
}