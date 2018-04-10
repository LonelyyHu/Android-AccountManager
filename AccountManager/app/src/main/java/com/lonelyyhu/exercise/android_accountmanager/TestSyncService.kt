package com.lonelyyhu.exercise.android_accountmanager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class TestSyncService: Service() {

    private val sLock = Any()
    private var sSyncAdapter: SyncAdapter? = null

    override fun onCreate() {

        Log.wtf("TestSyncService", "onCreate =>")

        synchronized(sLock) {
            if (null == sSyncAdapter) {
                sSyncAdapter = SyncAdapter(this, true)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return sSyncAdapter!!.getSyncAdapterBinder()
    }
}