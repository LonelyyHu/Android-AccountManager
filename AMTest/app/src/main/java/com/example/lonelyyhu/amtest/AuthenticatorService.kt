package com.example.lonelyyhu.amtest

import android.app.Service
import android.content.Intent
import android.os.IBinder


class AuthenticatorService : Service() {

//    private val sAccountAuthenticator: AccountAuthenticator? = null

    override fun onBind(intent: Intent): IBinder? {
        var binder: IBinder? = null
//        if (intent.action.equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
//            binder = getAuthenticator().getIBinder()
//        }
        return binder
    }

//    private fun getAuthenticator(): AccountAuthenticator {
//        if (null == AuthenticatorService.sAccountAuthenticator) {
//            AuthenticatorService.sAccountAuthenticator = AccountAuthenticator(this)
//        }
//        return AuthenticatorService.sAccountAuthenticator
//    }
}
