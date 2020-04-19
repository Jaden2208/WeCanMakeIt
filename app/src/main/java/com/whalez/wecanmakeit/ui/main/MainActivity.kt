package com.whalez.wecanmakeit.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.graphics.ColorUtils
import com.kakao.auth.Session
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.goToLoginPage
import com.whalez.wecanmakeit.ui.main.group.GroupFragment
import com.whalez.wecanmakeit.ui.main.home.HomeFragment
import com.whalez.wecanmakeit.ui.main.setting.SettingFragment
import com.whalez.wecanmakeit.ui.main.todo.TodoFragment
import github.com.st235.lib_expandablebottombar.ExpandableBottomBarMenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkUserLoginStatus(this@MainActivity)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment,
            HomeFragment()
        ).commit()
        expandable_bottom_bar.onItemSelectedListener = { v, menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.home -> HomeFragment()
                R.id.todo -> TodoFragment()
                R.id.group -> GroupFragment()
                R.id.setting -> SettingFragment()
                else -> null
            }
            if (fragment != null) {
                showAnimation(v, menuItem)
                supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
            }
        }
    }

    private fun checkUserLoginStatus(context: Context) {
        val userIsLoggedIn = Session.getCurrentSession().checkAndImplicitOpen()
        if (!userIsLoggedIn) goToLoginPage(context)
        else {
            return
        }
    }

    private fun showAnimation(v: View, i: ExpandableBottomBarMenuItem){
        val anim = ViewAnimationUtils.createCircularReveal(fragment,
            expandable_bottom_bar.x.toInt() + v.x.toInt() + v.width / 2,
            expandable_bottom_bar.y.toInt() + v.y.toInt() + v.height / 2, 0F,
            findViewById<View>(android.R.id.content).height.toFloat())
        fragment.setBackgroundColor(ColorUtils.setAlphaComponent(i.activeColor, 60))
        anim.duration = 420
        anim.start()
    }
}
