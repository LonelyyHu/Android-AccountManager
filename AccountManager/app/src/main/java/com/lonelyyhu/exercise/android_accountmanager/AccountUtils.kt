package com.lonelyyhu.exercise.android_accountmanager

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.util.Log


/**
 * Created by hulonelyy on 2018/2/10.
 * for accounts in AccountManager operation
 */
class AccountUtils {


    companion object {

        var mServerAuthenticator: IServerAuthenticator = MyServerAuthenticator()


        val ACCOUNT_TYPE = "com.lonelyyhu.exercise.android_accountmanager"
        val AUTH_TOKEN_TYPE = "com.lonelyyhu.exercise.android_accountmanager.auth"

        @JvmStatic
        fun getAccount(context: Context, accountName: String): Account? {
            val accountManager = AccountManager.get(context)
            val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)

            Log.wtf("AccountUtils", "accounts size => ${accounts.size}")

            for (account in accounts) {
                Log.wtf("AccountUtils", "getAccount => ${account.name}")
                if (account.name.equals(accountName, ignoreCase = true)) {
                    return account
                }
            }
            return null
        }

        @JvmStatic
        fun getFirstAccount(context: Context): Account? {
            val accountManager = AccountManager.get(context)
            val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)

            Log.wtf("AccountUtils", "accounts size => ${accounts.size}")

            for (account in accounts) {
                Log.wtf("AccountUtils", "getFirstAccount => ${account.name}")
                return account
            }
            return null
        }

    }




}