package com.lonelyyhu.exercise.android_accountmanager

import android.os.Bundle
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.accounts.Account
import android.content.Intent
import android.os.AsyncTask
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.annotation.TargetApi
import android.text.TextUtils
import android.widget.TextView
import android.view.inputmethod.EditorInfo
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText


class LoginActivity : AccountAuthenticatorActivity() {

    private var mAccountManager: AccountManager? = null

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null

    // Values for email and password at the time of the login attempt.
    private var mEmail: String? = null
    private var mPassword: String? = null

    // UI references.
    private var mEmailView: EditText? = null
    private var mPasswordView: EditText? = null
    private var mLoginFormView: View? = null
    private var mLoginStatusView: View? = null
    private var mLoginStatusMessageView: TextView? = null

    companion object {

        val ARG_ACCOUNT_TYPE = "accountType"
        val ARG_AUTH_TOKEN_TYPE = "authTokenType"
        val ARG_IS_ADDING_NEW_ACCOUNT = "isAddingNewAccount"
        val PARAM_USER_PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAccountManager = AccountManager.get(this)

        // Set up the login form.
        mEmail = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        mEmailView = findViewById(R.id.email) as EditText
        mEmailView!!.setText(mEmail)

        mPasswordView = findViewById(R.id.password) as EditText
        mPasswordView!!
                .setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(textView: TextView, id: Int,
                                                keyEvent: KeyEvent): Boolean {
//                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                            attemptLogin()
//                            return true
//                        }
                        return false
                    }
                })

        mLoginFormView = findViewById(R.id.login_form)
        mLoginStatusView = findViewById(R.id.login_status)
        mLoginStatusMessageView = findViewById(R.id.login_status_message) as TextView

        val button = findViewById<Button>(R.id.sign_in_button)

        button.setOnClickListener { attemptLogin() }

        if (null != mEmail) {
            if (!mEmail!!.isEmpty()) {
                mPasswordView!!.requestFocus()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_login, menu)
        return true
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        mEmailView!!.error = null
        mPasswordView!!.error = null

        // Store values at the time of the login attempt.
        mEmail = mEmailView!!.text.toString()
        mPassword = mPasswordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView!!.error = getString(R.string.error_field_required)
            focusView = mPasswordView
            cancel = true
        } else if (mPassword!!.length < 4) {
            mPasswordView!!.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView!!.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!mEmail!!.contains("@")) {
            mEmailView!!.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView!!.setText(R.string.login_progress_signing_in)
            showProgress(true)
            mAuthTask = UserLoginTask()
            mAuthTask!!.execute(null as Void?)
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(
                    android.R.integer.config_shortAnimTime)

            mLoginStatusView!!.setVisibility(View.VISIBLE)
            mLoginStatusView!!.animate().setDuration(shortAnimTime.toLong())
                    .alpha(if (show) 1F else 0F)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            mLoginStatusView!!.setVisibility(if (show)
                                View.VISIBLE
                            else
                                View.GONE)
                        }
                    })

            mLoginFormView!!.setVisibility(View.VISIBLE)
            mLoginFormView!!.animate().setDuration(shortAnimTime.toLong())
                    .alpha(if (show) 0F else 1F)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            mLoginFormView!!.setVisibility(if (show)
                                View.GONE
                            else
                                View.VISIBLE)
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView!!.setVisibility(if (show) View.VISIBLE else View.GONE)
            mLoginFormView!!.setVisibility(if (show) View.GONE else View.VISIBLE)
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask : AsyncTask<Void, Void, Intent>() {

        override fun doInBackground(vararg params: Void): Intent {

            // TODO: attempt authentication against a network service.
            val authToken = AccountUtils.mServerAuthenticator.signIn(mEmail!!, mPassword!!)

            val res = Intent()

            res.putExtra(AccountManager.KEY_ACCOUNT_NAME, mEmail)
            res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountUtils.ACCOUNT_TYPE)
            res.putExtra(AccountManager.KEY_AUTHTOKEN, authToken)
            res.putExtra(PARAM_USER_PASSWORD, mPassword)

            return res
        }

        override fun onPostExecute(intent: Intent) {
            mAuthTask = null
            showProgress(false)

            if (null == intent.getStringExtra(AccountManager.KEY_AUTHTOKEN)) {
                mPasswordView!!.error = getString(R.string.error_incorrect_password)
                mPasswordView!!.requestFocus()
            } else {
                finishLogin(intent)
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }

        private fun finishLogin(intent: Intent) {
            val accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            val accountPassword = intent.getStringExtra(PARAM_USER_PASSWORD)
            val account = Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))
            val authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN)

            if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
                // Creating the account on the device and setting the auth token we got
                // (Not setting the auth token will cause another call to the server to authenticate the user)
                mAccountManager!!.addAccountExplicitly(account, accountPassword, null)
                mAccountManager!!.setAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, authToken)
            } else {
                mAccountManager!!.setPassword(account, accountPassword)
            }

            setAccountAuthenticatorResult(intent.extras)
            setResult(AccountAuthenticatorActivity.RESULT_OK, intent)

            finish()
        }
    }

    override fun onBackPressed() {
        setResult(AccountAuthenticatorActivity.RESULT_CANCELED)
        super.onBackPressed()
    }



}
