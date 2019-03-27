package com.rod.uidemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
        Log.d("Lifecycle", "FragmentHostActivity, onCreate")
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("Lifecycle", "FragmentHostActivity, onRestoreInstanceState")
    }

    override fun onStart() {
        super.onStart()

        Log.d("Lifecycle", "FragmentHostActivity, onStart")
    }

    override fun onRestart() {
        super.onRestart()

        Log.d("Lifecycle", "FragmentHostActivity, onRestart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("Lifecycle", "FragmentHostActivity, onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("Lifecycle", "FragmentHostActivity, onPause")
    }

    override fun onStop() {
        super.onStop()

        Log.d("Lifecycle", "FragmentHostActivity, onStop")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        Log.d("Lifecycle", "FragmentHostActivity, onSaveInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("Lifecycle", "FragmentHostActivity, onDestroy")
    }
}