package com.rod.uidemo.testasynctask

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.fragment_test_asynctask.*

/**
 *
 * @author Rod
 * @date 2019/11/14
 */
class TestAsyncTaskFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_asynctask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startBtn.setOnClickListener {
            MyTask(firstTaskInfo, "first Task").execute()
            MyTask(secondTaskInfo, "second Task").execute()
        }
    }

    private inner class MyTask(private val tv: TextView, private val name: String) : AsyncTask<Void, Int, Int>() {

        override fun onPreExecute() {
            tv.text = "${name} pre execute"
        }

        override fun doInBackground(vararg params: Void?): Int {
            for (i in 0..100) {
                publishProgress(i)
                Thread.sleep(100)
            }
            return 1000
        }

        override fun onProgressUpdate(vararg values: Int?) {
            tv.text = "$name Progress: ${values[0]}"
        }

        override fun onPostExecute(result: Int?) {
            tv.text = "$name complete: $result"
        }
    }
}