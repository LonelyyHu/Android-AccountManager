package com.lonelyyhu.exercise.android_accountmanager

import android.Manifest
import android.os.Bundle
import android.accounts.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.TextView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {

    var permissions = arrayOf<String>(
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCOUNT_MANAGER
    )
    val REQ_PERMISSION_CODE = 100


    private var mAccountManager: AccountManager? = null
    private var mAuthPreferences: AuthPreference? = null
    private var authToken: String? = null
    private var mAccount: Account? = null

    private var tvName: TextView? = null
    private var tvPassword: TextView? = null
    private var tvToken: TextView? = null

    companion object {
        private val REQ_SIGNUP = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvName = findViewById<TextView>(R.id.tvName)
        tvPassword = findViewById<TextView>(R.id.tvPassword)
        tvToken = findViewById<TextView>(R.id.tvToken)

        authToken = null
        mAuthPreferences = AuthPreference(this)
        mAccountManager = AccountManager.get(this)

        checkPermissions()
    }

    override fun onResume() {
        super.onResume()

        showInfo()
    }

    private fun showInfo() {
        mAccount = AccountUtils.getFirstAccount(this)

        if (mAccount == null) {
            tvName!!.text = ""
            tvPassword!!.text = ""
            tvToken!!.text = ""
        } else {
            tvName!!.text = mAccount!!.name
            tvPassword!!.text = mAccountManager!!.getPassword(mAccount)
            authToken = mAccountManager!!.peekAuthToken(mAccount, AccountUtils.AUTH_TOKEN_TYPE)
            tvToken!!.text = authToken
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_close_session -> {

                Log.wtf("MainActivity", "onOptionsItemSelected => action_close_session")

                // Clear session and ask for new auth token
                mAccountManager!!.invalidateAuthToken(AccountUtils.ACCOUNT_TYPE, authToken)
                mAuthPreferences!!.setAuthToken("")
//                mAuthPreferences!!.setUsername("")
//                mAccountManager!!.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, GetAuthTokenCallback(), null)

                showInfo()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun onClickAddAccount(view: View) {
        var account = Account("t-${Calendar.getInstance().timeInMillis}@exsample.com", AccountUtils.ACCOUNT_TYPE)
        mAccountManager!!.addAccountExplicitly(account, "password", null)

        mAccountManager!!.setUserData(account,"USER_EMAIL", "user@mail.com")

        showInfo()

    }

    fun onClickGetToken(view: View) {

        // Ask for an auth token
//        mAccountManager!!.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, GetAuthTokenCallback(), null)


        val bundle = Bundle()
        bundle.putString("KEY", "aaa12345")

        if (mAccount != null) {
            mAccountManager!!.getAuthToken(mAccount, AccountUtils.AUTH_TOKEN_TYPE, bundle, this, GetAuthTokenCallback(), null)
        } else {
            Toast.makeText(this, "Account is null", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun onClickRemoveAccount(view: View) {

        if (mAccount != null) {
            mAccountManager?.removeAccount(mAccount, this@MainActivity, {
                showInfo()
            }, null)

            Log.wtf("MainActivity", "onClickRemoveAccount => Account ${mAccount?.name} removed")

        } else {
            Log.wtf("MainActivity", "onClickRemoveAccount => No Account been removed !!")
            showInfo()

        }

    }

    fun onClickListAccounts(view: View) {
        val accountManager = AccountManager.get(this)
        val accounts = accountManager.accounts

        Log.wtf("MainActivity", "onClickListAccounts => all accounts size:${accounts.size}")

        for (account in accounts) {
            Log.wtf("MainActivity", "account name: ${account.name}, account type: ${account.type}")
        }
    }

    fun onClickSetData(view: View) {

        val account = AccountUtils.getFirstAccount(this)

        val accountManager = AccountManager.get(this)

        val data = Calendar.getInstance().timeInMillis.toString()

        accountManager.setUserData(account, "accessTime", data)

        Log.wtf("MainActivity", "onClickSetData => $data")
    }

    fun onClickGetData(view: View) {
        val account = AccountUtils.getFirstAccount(this)

        val accountManager = AccountManager.get(this)
        val value = accountManager.getUserData(account, "accessTime")
        Log.wtf("MainActivity", "onClickGetData => $value")
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
//                    checkPermissions()
                }
                return
            }
        }
    }

    private inner class GetAuthTokenCallback : AccountManagerCallback<Bundle> {

        override fun run(result: AccountManagerFuture<Bundle>) {

            Log.wtf("GetAuthTokenCallback", "GetAuthTokenCallback run!!!")

            val bundle: Bundle

            try {
                bundle = result.result

                val intent = bundle.get(AccountManager.KEY_INTENT) as Intent?
                if (null != intent) {

                    Log.wtf("GetAuthTokenCallback", "intent not null")

                    startActivityForResult(intent, REQ_SIGNUP)
                } else {
                    authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN)
                    val accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME)

                    Log.wtf("GetAuthTokenCallback", "accountName => $accountName")

                    // Save session username & auth token
                    mAuthPreferences!!.setAuthToken(authToken!!)
                    mAuthPreferences!!.setUsername(accountName)

                    tvName!!.text = accountName
                    tvPassword!!.text = authToken

                    val acc = AccountUtils.getAccount(this@MainActivity, accountName)
                    acc?.let {
                        tvToken!!.text = mAccountManager?.peekAuthToken(acc, AccountUtils.AUTH_TOKEN_TYPE) ?: ""
                    }


                    // If the logged account didn't exist, we need to create it on the device
                    var account = AccountUtils.getAccount(this@MainActivity, accountName!!)
                    if (null == account) {
                        account = Account(accountName, AccountUtils.ACCOUNT_TYPE)
                        mAccountManager!!.addAccountExplicitly(account, bundle.getString(LoginActivity.PARAM_USER_PASSWORD), null)
                        mAccountManager!!.setAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, authToken)
//                        mAccountManager!!.setUserData()
                    }
                }
            } catch (e: OperationCanceledException) {
                // If signup was cancelled, force activity termination

                Log.wtf("GetAuthTokenCallback", "catch OperationCanceledException!!")

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }



}
