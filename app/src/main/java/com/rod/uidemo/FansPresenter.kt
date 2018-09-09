package com.rod.uidemo

import android.support.annotation.NonNull
import com.rod.uidemo.data.DataUnit

/**
 *
 * @author Rod
 * @date 2018/9/9
 */
class FansPresenter(private val mView: FansConstract.View)
    : DataPresenter<FansPresenter.FansData>(), FansConstract.Presenter {

    @NonNull
    override fun generateDataUnit() = FansPresenter.FansData()

    @NonNull
    override fun generatePageId() = "fans_${System.currentTimeMillis()}"

    override fun onNewCreate() {
        mDataUnit.mFans.addAll(getData())
        mView.setData(mDataUnit.mFans)
    }

    override fun onRestoreState() {
        mView.setData(mDataUnit.mFans)
    }

    private fun getData(): List<String> {
        return (0 until 50).map { "Item $it" }
    }

    class FansData : DataUnit {
        val mFans = ArrayList<String>()

        override fun clear() {
            mFans.clear()
        }
    }
}