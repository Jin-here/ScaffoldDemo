package com.vgaw.scaffolddemo.page

import android.Manifest
import android.os.Build
import android.os.Bundle
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.page.ScaffoldFrag
import com.vgaw.scaffold.page.common.PermissionAc
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffold.view.ScaffoldToast
import com.vgaw.scaffold.view.bottomnavigation.BottomNavigationLayout
import com.vgaw.scaffolddemo.R
import com.vgaw.scaffolddemo.page.demo.compoment.CompomentFrag
import com.vgaw.scaffolddemo.page.demo.example.ExampleFrag
import com.vgaw.scaffolddemo.page.demo.internalpage.InternalPageFrag

class MainAc : ScaffoldAc() {
    companion object {
        private const val BACK_TO_EXIT_DURATION = 1400L
    }

    private lateinit var mMainBottomNavLayout: BottomNavigationLayout
    
    private var mLstBackTime: Long = 0

    private lateinit var mFragmentArray: Array<ScaffoldFrag>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_ac)
        mMainBottomNavLayout = findViewById(R.id.main_bottom_navLayout)


        initData()
        initView()
        requestPermission()
    }

    override fun onBackPressed() {
        val backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount()
        if (backStackEntryCount > 0) {
            super.onBackPressed()
            return
        }

        val crtTime = System.currentTimeMillis()
        if (crtTime - mLstBackTime > BACK_TO_EXIT_DURATION) {
            mLstBackTime = crtTime
            ScaffoldToast.show(R.string.back_again_to_exit)
        } else {
            super.onBackPressed()
        }
    }

    private fun initData() {
        mFragmentArray = arrayOf(CompomentFrag(), InternalPageFrag(), ExampleFrag())
    }

    private fun initView() {
        mMainBottomNavLayout.setOnItemCheckedListener(this::showFragment)
        mMainBottomNavLayout.check(0)
    }

    private fun showFragment(index: Int) {
        val tag = DialogUtil.buildTag(mFragmentArray[index])

        val fragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag(tag) as? ScaffoldFrag

        val fragmentTransaction = fragmentManager.beginTransaction()
        // hide other
        val fragmentList = fragmentManager.fragments
        if (fragmentList != null && fragmentList.size > 0) {
            for (item in fragmentList) {
                if (item.isAdded) {
                    fragmentTransaction.hide(item)
                }
            }
        }
        // showDialog index fragment
        if (fragment == null) {
            fragment = mFragmentArray[index]
            fragmentTransaction.add(R.id.main_container, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }

        fragmentTransaction.commit()

        StatusBarUtil.setColor(this, resources.getColor(fragment.getStatusBarColor()))
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                PermissionAc.Companion.startActivityForResult(getSelf(), 0, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            } else {}
        }
    }
}
