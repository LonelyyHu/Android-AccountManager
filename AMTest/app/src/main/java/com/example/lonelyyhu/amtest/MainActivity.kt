package com.example.lonelyyhu.amtest

import android.Manifest
import android.accounts.AccountManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class MainActivity : AppCompatActivity() {

    var permissions = arrayOf<String>(
            Manifest.permission.GET_ACCOUNTS
    )

    companion object {
        const val ACCOUNT_TYPE = "com.lonelyyhu.exercise.android_accountmanager"
//        const val ACCOUNT_TYPE = "com.facebook.messenger"

        const val REQ_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listAccounts(null)

        checkPermissions()

    }

    private fun listAccounts(accType: String?) {

        Log.wtf("MainActivity", "listAccounts!!")

        val am = getSystemService(ACCOUNT_SERVICE) as AccountManager

        val accounts = if (accType == null) {
            am.accounts
        } else {
            am.getAccountsByType(accType)
        }

        for (acc in accounts) {

            Log.wtf("MainActivity", "acc name:${acc.name}, type:${acc.type}")

        }
    }

    fun getAccount(view: View) {

        listAccounts(ACCOUNT_TYPE)


        val am = getSystemService(ACCOUNT_SERVICE) as AccountManager

        val accounts = am.getAccountsByType(ACCOUNT_TYPE)

        for (acc in accounts) {

            Log.wtf("MainActivity", "getPassword for accName => ${acc.name}")

            val mail = am.getUserData(acc, "USER_EMAIL")

            Log.wtf("MainActivity", "USER_EMAIL => $mail")
//            am.getPassword(acc)
//            am.peekAuthToken(acc, ACCOUNT_TYPE)
        }

    }

    fun getAllAccount(view: View) {
        listAccounts(null)
    }

    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded = arrayListOf<String>()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), REQ_PERMISSION_CODE)
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQ_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    checkPermissions()
                }
                return
            }
        }
    }

}
