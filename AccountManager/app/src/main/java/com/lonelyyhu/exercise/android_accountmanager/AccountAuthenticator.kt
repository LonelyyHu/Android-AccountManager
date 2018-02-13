package com.lonelyyhu.exercise.android_accountmanager

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import android.accounts.AbstractAccountAuthenticator
import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast


/**
 * Created by hulonelyy on 2018/2/10.
 */
class AccountAuthenticator(private val mContext: Context) : AbstractAccountAuthenticator(mContext) {

    companion object {
        const val ERROR_CODE_ONE_ACCOUNT_ALLOWED = 99
    }

    private val handler = Handler()


    @Throws(NetworkErrorException::class)
    override fun addAccount(response: AccountAuthenticatorResponse, accountType: String, authTokenType: String?, requiredFeatures: Array<String>?, options: Bundle?): Bundle? {

        Log.wtf("AccountAuthenticator", "addAccount running!!")

        val reply = Bundle()

        val am = AccountManager.get(mContext)
        val accountsSize = am.getAccountsByType(AccountUtils.ACCOUNT_TYPE)?.size ?: 0

        if (accountsSize > 0) {
            reply.putInt(AccountManager.KEY_ERROR_CODE, ERROR_CODE_ONE_ACCOUNT_ALLOWED)
            reply.putString(AccountManager.KEY_ERROR_MESSAGE, "only one account allowed")

            handler.post(Runnable {

                Toast.makeText(mContext, "only one account allowed", Toast.LENGTH_LONG).show()

            })

            return reply
        }


        val intent = Intent(mContext, LoginActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType)
        intent.putExtra(LoginActivity.ARG_AUTH_TOKEN_TYPE, authTokenType)
        intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true)

        // return our AccountAuthenticatorActivity
        reply.putParcelable(AccountManager.KEY_INTENT, intent)

        return reply
    }

    @Throws(NetworkErrorException::class)
    override fun confirmCredentials(arg0: AccountAuthenticatorResponse, arg1: Account, arg2: Bundle): Bundle? {

        Log.wtf("AccountAuthenticator", "confirmCredentials running!!")

        return null
    }

    override fun editProperties(arg0: AccountAuthenticatorResponse, arg1: String): Bundle? {

        Log.wtf("AccountAuthenticator", "editProperties running!!")

        return null
    }

    @Throws(NetworkErrorException::class)
    override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account?, authTokenType: String, options: Bundle): Bundle {

        Log.wtf("AccountAuthenticator", "getAuthToken running!!")

        options.let {
            val op = options.getString("KEY")
            Log.wtf("AccountAuthenticator", "options => $op")
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        val am = AccountManager.get(mContext)

        var authToken: String? = am.peekAuthToken(account, authTokenType)

        // Lets give another try to authenticate the user
        if (null != authToken) {
            if (authToken.isEmpty()) {
                val password = am.getPassword(account)
                if (password != null) {
                    authToken = AccountUtils.mServerAuthenticator.signIn(account!!.name, password)
                }
            }
        }

        // If we get an authToken - we return it
        if (null != authToken) {
            if (!authToken.isEmpty()) {
                val result = Bundle()
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account!!.name)
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
                result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
                return result
            }
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        val intent = Intent(mContext, LoginActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, account!!.type)
        intent.putExtra(LoginActivity.ARG_AUTH_TOKEN_TYPE, authTokenType)

        // This is for the case multiple accounts are stored on the device
        // and the AccountPicker dialog chooses an account without auth token.
        // We can pass out the account name chosen to the user of write it
        // again in the Login activity intent returned.
        if (null != account) {
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name)
        }

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

    override fun getAuthTokenLabel(arg0: String): String? {

        Log.wtf("AccountAuthenticator", "getAuthTokenLabel running!!")

        return null
    }

    @Throws(NetworkErrorException::class)
    override fun hasFeatures(arg0: AccountAuthenticatorResponse, arg1: Account, arg2: Array<String>): Bundle? {

        Log.wtf("AccountAuthenticator", "hasFeatures running!!!")

        return null
    }

    @Throws(NetworkErrorException::class)
    override fun updateCredentials(arg0: AccountAuthenticatorResponse, arg1: Account, arg2: String, arg3: Bundle): Bundle? {

        Log.wtf("AccountAuthenticator", "updateCredentials running!!")

        return null
    }

}