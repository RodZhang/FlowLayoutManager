package com.rod.uidemo.mokelocation

import android.annotation.TargetApi
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.*
import android.provider.Settings
import android.provider.Settings.Secure
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * @author Rod
 * @date 2019/3/21
 */
class MockLocationFragment : Fragment() {

    companion object {
        const val TAG = "MockLocationFragment"
    }

    private lateinit var mInfoText: TextView
    private var mLat = 39.92998577808024
    private var mLng = 116.39564503787867

    private lateinit var mLocationManager : LocationManager
    private val mWorkHandler = lazy {
        val handlerThread = HandlerThread("location", Process.THREAD_PRIORITY_BACKGROUND)
        handlerThread.start()
        Handler(handlerThread.looper)
    }
    private val mRunnable = Runnable {
        setLocation()
        postWork()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            verticalLayout {
                button("start mock").onClick {
                    startMock()
                    mInfoText.text = "start mock"
                }
                mInfoText = textView()
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun startMock() {
        enableTestProvider()
        postWork()
    }

    private fun postWork() {
        mWorkHandler.value.postDelayed(mRunnable, 500)
    }

    private fun enableTestProvider() {
        val locationManager = mLocationManager
        val res = activity?.contentResolver
        //获取gps的状态，false为关闭，true为开启。
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "", "requiresSatellite" == "",
                "requiresCell" == "", "hasMonetaryCost" == "",
                "supportsAltitude" == "", "supportsSpeed" == "",
                "supportsBearing" == "supportsBearing",
                Criteria.POWER_LOW,
                Criteria.ACCURACY_FINE);
        locationManager.addTestProvider(LocationManager.NETWORK_PROVIDER,
                "requiresNetwork" == "", "requiresSatellite" == "",
                "requiresCell" == "", "hasMonetaryCost" == "",
                "supportsAltitude" == "", "supportsSpeed" == "",
                "supportsBearing" == "supportsBearing",
                Criteria.POWER_LOW,
                Criteria.ACCURACY_FINE);
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        locationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setLocation() {
        val loc = Location(LocationManager.GPS_PROVIDER)
        loc.setAccuracy(Criteria.ACCURACY_FINE.toFloat());
        loc.setTime(System.currentTimeMillis());//设置当前时间
        loc.setLatitude(mLat);           //设置纬度
        loc.setLongitude(mLng);           //设置经度
        loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, loc);
        val netLoc = Location(LocationManager.NETWORK_PROVIDER)
        netLoc.setAccuracy(Criteria.ACCURACY_FINE.toFloat());
        netLoc.setTime(System.currentTimeMillis());//设置当前时间
        netLoc.setLatitude(mLat);           //设置纬度
        netLoc.setLongitude(mLng);           //设置经度
        netLoc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        mLocationManager.setTestProviderLocation(LocationManager.NETWORK_PROVIDER, netLoc);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disableTestProvider()
        mWorkHandler.value.removeCallbacks(mRunnable)
    }

    private fun disableTestProvider() {
        mLocationManager.clearTestProviderEnabled(LocationManager.GPS_PROVIDER)
        mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        mLocationManager.clearTestProviderEnabled(LocationManager.NETWORK_PROVIDER)
        mLocationManager.removeTestProvider(LocationManager.NETWORK_PROVIDER);
    }
}
