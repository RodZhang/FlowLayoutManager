package com.rod.uidemo.permission

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.Context
import android.os.Binder
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.util.Log

/**
 *
 * @author Rod
 * @date 2019/6/3
 */
class AppOpsPermission(private val context: Context) {

    companion object {
        private const val TAG = "AppOpsPermission"
    }

    private val mPackageName: String = context.applicationContext.packageName
    private var mAppOpsManager: AppOpsManager? = null

    fun doWithPermission(permissions: Array<String>, callback: PermissionCallback) {
        val permissionResults = permissions.map { Permission(it, isAllowed(it), false) }
        callback.invoke(permissionResults)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun isAllowed(opStr: String?): Boolean {
        if (TextUtils.isEmpty(opStr)) {
            return true
        }
        if (mAppOpsManager == null) {
            mAppOpsManager = getAppOpsManager()
        }

        val aapOpsManager = mAppOpsManager ?: return true

        var isAllowed = false
        try {
            val result = aapOpsManager.checkOpNoThrow(opStr, Binder.getCallingUid(), mPackageName)
            Log.d(TAG, String.format("checkPermission:%s, result=%d", opStr, result))
            if (result == AppOpsManager.MODE_ALLOWED) {
                isAllowed = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "", e)
        }

        return isAllowed
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getAppOpsManager(): AppOpsManager? {
        if (mAppOpsManager == null) {
            mAppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        }
        return mAppOpsManager
    }
}
typealias PermissionCallback = (permissions: List<Permission>) -> Unit