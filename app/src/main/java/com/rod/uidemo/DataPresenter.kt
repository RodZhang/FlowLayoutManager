package com.rod.uidemo

import android.os.Bundle
import com.rod.uidemo.data.DataPage
import com.rod.uidemo.data.DataRepositoryImpl
import com.rod.uidemo.data.DataUnit

/**
 *
 * @author Rod
 * @date 2018/9/9
 */
abstract class DataPresenter<D : DataUnit>() : DataPage<D> {

    companion object {
        private const val TAG = "DataPresenter"
        private const val KEY_PAGE_ID = "PAGE_ID"
    }

    private var mSavedState = false
    private lateinit var mPageId: String
    protected lateinit var mDataUnit: D

    fun onPageCreate(savedState: Bundle?) {
        if (savedState == null) {
            mPageId = generatePageId()
            mDataUnit = generateDataUnit()
            DataRepositoryImpl.instance().put(mPageId, mDataUnit)
            onNewCreate()
            UL.d(TAG, "onNewCreate")
        } else {
            mPageId = savedState.getString(KEY_PAGE_ID)
            with(DataRepositoryImpl.instance()) {
                var data = get<D>(mPageId)
                if (data == null) {
                    data = generateDataUnit()
                    put(mPageId, data)
                }
                mDataUnit = data
            }
            onRestoreState()
            UL.d(TAG, "onRestoreState")
        }
    }

    fun onSaveInstanceStates(state: Bundle) {
        state.putString(KEY_PAGE_ID, mPageId)
        mSavedState = true
        UL.d(TAG, "onSaveInstanceStates")
    }

    fun onPageDestroy() {
        UL.d(TAG, "onPageDestroy, mSavedState=$mSavedState")
        if (!mSavedState) {
            UL.d(TAG, "will remove key=$mPageId")
            DataRepositoryImpl.instance().remove(mPageId)
        }
    }

    protected abstract fun onNewCreate()

    protected abstract fun onRestoreState()
}