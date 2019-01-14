package com.rod.uidemo.refresh

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.fragment_viewpager.*

/**
 *
 * @author Rod
 * @date 2019/1/14
 */
class ViewPagerFragment : Fragment() {

    companion object {
        const val TAG = "ViewPagerFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager.offscreenPageLimit = 2
        pager.adapter = MyFragmentAdapter(childFragmentManager)
    }

    class MyFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return HorizontalRefreshFragment()
        }

        override fun getCount(): Int {
            return 9
        }

    }

}