package com.lonelyyhu.exercise.android_accountmanager

/**
 * Created by hulonelyy on 2018/2/10.
 */
interface IServerAuthenticator {


    /**
     * Tells the server to create the new user and return its auth token.
     * @param email
     * @param username
     * @param password
     * @return Access token
     */
    fun signUp(email: String, username: String, password: String): String?

    /**
     * Logs the user in and returns its auth token.
     * @param email
     * @param password
     * @return Access token
     */
    fun signIn(email: String, password: String): String?


}