package com.rod.uidemo

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2020/4/9
 */
class TestLifeCycleFragment: Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("Lifecycle", "TestLifeCycleFragment onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "TestLifeCycleFragment onCreate")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d("Lifecycle", "TestLifeCycleFragment onCreateView")
        return inflater.inflate(R.layout.fragment_ipc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("Lifecycle", "TestLifeCycleFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Lifecycle", "TestLifeCycleFragment onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d("Lifecycle", "TestLifeCycleFragment onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("Lifecycle", "TestLifeCycleFragment onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("Lifecycle", "TestLifeCycleFragment onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("Lifecycle", "TestLifeCycleFragment onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("Lifecycle", "TestLifeCycleFragment onDestroy")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("Lifecycle", "TestLifeCycleFragment onDestroyView")
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.d("Lifecycle", "TestLifeCycleFragment onDetach")
        super.onDetach()
    }
}