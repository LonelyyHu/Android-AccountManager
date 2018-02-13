package com.lonelyyhu.exercise.android_accountmanager

import android.app.Service
import android.os.IBinder
import android.content.Intent



/**
 * Created by hulonelyy on 2018/2/10.
 */
class AuthenticatorService : Service() {

    private val authenticator: AccountAuthenticator?
        get() {
            if (null == AuthenticatorService.sAccountAuthenticator) {
                AuthenticatorService.sAccountAuthenticator = AccountAuthenticator(this)
            }
            return AuthenticatorService.sAccountAuthenticator
        }

    companion object {
        private var sAccountAuthenticator: AccountAuthenticator? = null
    }

    override fun onBind(intent: Intent): IBinder? {
        var binder: IBinder? = null
        if (intent.action == android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT) {
            binder = authenticator!!.iBinder
        }
        return binder
    }



}