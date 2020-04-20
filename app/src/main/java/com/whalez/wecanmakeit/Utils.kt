package com.whalez.wecanmakeit

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.whalez.wecanmakeit.ui.login.LoginActivity
import com.whalez.wecanmakeit.ui.main.MainActivity

var kakaoLogin = 0

// 할 일
const val ADD_TODO_REQUEST = 0
const val EDIT_TODO_REQUEST = 1

const val EXTRA_ID = "EXTRA_ID"
const val EXTRA_TIMESTAMP = "EXTRA_TIMESTAMP"
const val EXTRA_CONTENT = "EXTRA_CONTENT"

// 그룹
const val CREATE_NEW_GROUP = 0

const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
const val EXTRA_GROUP_NAME = "EXTRA_GROUP_NAME"
const val EXTRA_GROUP_TYPE = "EXTRA_GROUP_TYPE"
const val EXTRA_GROUP_USER = "EXTRA_GROUP_USER"


fun goToLoginPage(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
    (context as MainActivity).finish()
}

fun shortToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun simpleBuilder(context: Context): AlertDialog.Builder {
    return AlertDialog.Builder(
        ContextThemeWrapper(
            context,
            R.style.MyAlertDialogStyle
        )
    )
}