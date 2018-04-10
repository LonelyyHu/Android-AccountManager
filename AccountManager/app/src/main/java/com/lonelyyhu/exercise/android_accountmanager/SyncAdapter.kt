package com.lonelyyhu.exercise.android_accountmanager

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.util.Log

class SyncAdapter(context: Context, autoInit: Boolean) : AbstractThreadedSyncAdapter(context, autoInit) {


    override fun onPerformSync(account: Account?, extras: Bundle?, authority: String?, provider: ContentProviderClient?, syncResult: SyncResult?) {
        Log.wtf("SyncAdapter", "onPerformSync =>")

    }


}