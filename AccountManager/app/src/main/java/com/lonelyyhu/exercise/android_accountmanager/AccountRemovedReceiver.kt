package com.lonelyyhu.exercise.android_accountmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AccountRemovedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.wtf("AccountRemovedReceiver", "onReceive =>")
    }
}
