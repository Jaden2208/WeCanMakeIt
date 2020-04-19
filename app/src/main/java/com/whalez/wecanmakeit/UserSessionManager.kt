package com.whalez.wecanmakeit

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class UserSessionManager(context: Context) {

    private val TAG = "kkk.UserSessionManager"

    private var sharedPreferences: SharedPreferences
    private var editor: Editor


    companion object {
        private const val LOGIN = "IS_LOGIN"
        const val KAKAO_ID = "KAKAO_ID"
        const val PRIVATE_MODE = 0
    }

    init {
        sharedPreferences =
            context.getSharedPreferences(KAKAO_ID, PRIVATE_MODE)
        editor = sharedPreferences.edit()
        editor.apply()
    }

    fun createSession(id: String?) {
        editor.putBoolean(LOGIN, true)
        editor.putString(KAKAO_ID, id)
        editor.apply()
    }

    private val isLogin: Boolean
        get() = sharedPreferences.getBoolean(LOGIN, false)

//    fun checkLogin() {
//        if (!isLogin) {
//            val intent = Intent(context, LoginActivity::class.java)
//            context.startActivity(intent)
//            (context as MainActivity).finish()
//        }
//    }

    val userDetail: HashMap<String, String?>
        get() {
            val user = HashMap<String, String?>()
            user[KAKAO_ID] = sharedPreferences.getString(KAKAO_ID, null)
            return user
        }

    val currentID: String?
        get() = sharedPreferences.getString(KAKAO_ID, null)

    fun changeValue(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    fun removeUserDataFromSharedReference() {
        editor.clear()
        editor.commit()
//        val i = Intent(context, LoginActivity::class.java)
//        context.startActivity(i)
//        (context as MainActivity).finish()
    }


}