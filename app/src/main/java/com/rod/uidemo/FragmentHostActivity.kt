package com.rod.uidemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.rod.uidemo.ext.getIntentString

/**
 *
 * @author Rod
 * @date 2018/9/9
 */
class FragmentHostActivity : AppCompatActivity() {

    companion object {
        const val ARGS_FRAGMENT_NAME = "fragment_name"
        const val ARGS_FRAGMENT_TAG = "fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_host)

        val fragmentTag = getIntentString(ARGS_FRAGMENT_TAG)
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(fragmentTag)

        if (fragment == null) {
            val fragmentName = getIntentString(ARGS_FRAGMENT_NAME)
            fragment = Class.forName(fragmentName).newInstance() as Fragment
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, fragmentTag)
                    .commit()
        }

    }
}