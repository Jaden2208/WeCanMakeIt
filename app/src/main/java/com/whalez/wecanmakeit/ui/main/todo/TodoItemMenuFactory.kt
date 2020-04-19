package com.whalez.wecanmakeit.ui.main.todo

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import androidx.lifecycle.LifecycleOwner
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import com.skydoves.powermenu.kotlin.createPowerMenu
import com.whalez.wecanmakeit.R

class TodoItemMenuFactory: PowerMenu.Factory() {

    companion object { // 메뉴 옵션
        const val EDIT = 0
        const val DELETE = 1
    }

    override fun create(context: Context, lifecycle: LifecycleOwner): PowerMenu {
        return createPowerMenu(context) {
            addItem(PowerMenuItem("수정", false))
            addItem(PowerMenuItem("삭제", false))
            /* PowerMenu methods link
             * https://github.com/skydoves/PowerMenu#powermenu-methods
             */
            setWidth(200)
            setAutoDismiss(true)
            setLifecycleOwner(lifecycle)
            setAnimation(MenuAnimation.DROP_DOWN)
            setMenuRadius(10f)
            setMenuShadow(10f)
            setTextColorResource(R.color.colorBlack)
            setTextSize(13)
            setTextGravity(Gravity.CENTER)
            setTextTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL))
            setSelectedTextColor(Color.WHITE)
            setMenuColor(Color.WHITE)
            setSelectedMenuColorResource(R.color.colorPrimary)
            setPreferenceName("TodoItemMenu")
        }
    }
}