package com.rod.uidemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.rod.uidemo.data.DataUnit

/**
 *
 * @author Rod
 * @date 2018/9/7
 */
abstract class BaseDataFragment<D : DataUnit, P : DataPresenter<D>> : Fragment() {

    protected lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = generatePresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.onPageCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mPresenter.onSaveInstanceStates(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.onPageDestroy()
    }

    protected abstract fun generatePresenter(): P
}