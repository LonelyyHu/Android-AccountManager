package com.lonelyyhu.exercise.android_accountmanager

import android.R.id.edit
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences



/**
 * Created by hulonelyy on 2018/2/10.
 */
class AuthPreference(context: Context) {

    private val PREFS_NAME = "auth"
    private val KEY_ACCOUNT_NAME = "account_name"
    private val KEY_AUTH_TOKEN = "auth_token"

    private var preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getAccountName(): String? {
        return preferences.getString(KEY_ACCOUNT_NAME, null)
    }

    fun getAuthToken(): String? {
        return preferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun setUsername(accountName: String) {
        val editor = preferences.edit()
        editor.putString(KEY_ACCOUNT_NAME, accountName)
        editor.commit()
    }

    fun setAuthToken(authToken: String) {
        val editor = preferences.edit()
        editor.putString(KEY_AUTH_TOKEN, authToken)
        editor.commit()
    }

}