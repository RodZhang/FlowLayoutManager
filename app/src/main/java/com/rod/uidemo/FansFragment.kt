package com.rod.uidemo

import android.os.Bundle
import android.support.annotation.NonNull
import android.view.View
import com.rod.uidemo.data.DataUnit

/**
 *
 * @author Rod
 * @date 2018/9/7
 */
class FansFragment : BaseDataFragment<FansFragment.FansData>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onNewCreate() {
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