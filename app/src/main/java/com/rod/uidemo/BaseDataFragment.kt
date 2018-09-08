package com.rod.uidemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.rod.uidemo.data.DataPage
import com.rod.uidemo.data.DataRepositoryImpl
import com.rod.uidemo.data.DataUnit

/**
 *
 * @author Rod
 * @date 2018/9/7
 */
abstract class BaseDataFragment<D : DataUnit> : Fragment(), DataPage<D> {

    companion object {
        private const val KEY_PAGE_ID = "PAGE_ID"
    }

    protected lateinit var mDataUnit: D
    private var mPageId = "";
    private var mSavedState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            mPageId = generatePageId()
            mDataUnit = generateDataUnit()
            DataRepositoryImpl.instance().put(mPageId, mDataUnit)
            onNewCreate()
        } else {
            mPageId = savedInstanceState.getString(KEY_PAGE_ID)
            with(DataRepositoryImpl.instance()) {
                var data = get<D>(mPageId)
                if (data == null) {
                    data = generateDataUnit()
                    put(mPageId, data)
                }
                mDataUnit = data
            }
            onRestoreState()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_PAGE_ID, mPageId)
        mSavedState = true
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!mSavedState) {
            DataRepositoryImpl.instance().remove(mPageId)
        }
    }

    protected abstract fun onNewCreate()

    protected abstract fun onRestoreState()
}