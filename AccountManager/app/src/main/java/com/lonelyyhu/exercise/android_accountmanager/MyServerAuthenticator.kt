package com.lonelyyhu.exercise.android_accountmanager

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by hulonelyy on 2018/2/10.
 */
class MyServerAuthenticator : IServerAuthenticator {

    override fun signUp(email: String, username: String, password: String): String? {
        // TODO: register new user on the server and return its auth token
        return null
    }

    override fun signIn(email: String, password: String): String? {
        val df = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINESE)
        return email + "-" + df.format(Date())
    }

    companion object {

//        /**
//         * A dummy authentication store containing known user names and passwords.
//         * TODO: remove after connecting to a real authentication system.
//         */
//        private var mCredentialsRepo: Map<String, String>? = null
//
//        init {
//            val credentials = HashMap<String, String>()
//            credentials["demo@example.com"] = "demo"
//            credentials["foo@example.com"] = "foobar"
//            credentials["user@example.com"] = "pass"
//            mCredentialsRepo = Collections.unmodifiableMap(credentials)
//        }
    }

}