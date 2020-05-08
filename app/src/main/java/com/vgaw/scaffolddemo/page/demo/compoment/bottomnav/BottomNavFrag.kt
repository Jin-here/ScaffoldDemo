package com.vgaw.scaffolddemo.page.demo.compoment.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.vgaw.scaffold.page.ScaffoldFrag
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffold.view.TitleLayout
import com.vgaw.scaffold.view.bottomnavigation.BottomNavigationLayout
import com.vgaw.scaffolddemo.R
import com.vgaw.scaffolddemo.page.demo.compoment.Tab0Frag
import com.vgaw.scaffolddemo.page.demo.compoment.Tab1Frag
import com.vgaw.scaffolddemo.page.demo.compoment.Tab2Frag
import kotlinx.android.synthetic.main.bottom_nav_frag.view.*

class BottomNavFrag : ScaffoldFrag() {
    private lateinit var mFragmentArray: Array<ScaffoldFrag>
    private lateinit var bottomNavBottomNavLayout: BottomNavigationLayout
    private lateinit var bottomNavTitleLayout: TitleLayout
    private lateinit var bottomNavShowBubble: MaterialButton
    private lateinit var bottomNavHideBubble: MaterialButton
    
    companion object {
        fun newInstance(): BottomNavFrag {
            val args = Bundle()
            val fragment = BottomNavFrag()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_nav_frag, container, false)
        StatusBarUtil.addStatusbarHeight(context, view)
        bottomNavBottomNavLayout = view.bottomNavBottomNavLayout
        bottomNavTitleLayout = view.bottomNavTitleLayout
        bottomNavShowBubble = view.bottomNavShowBubble
        bottomNavHideBubble = view.bottomNavHideBubble

        initView()
        return view
    }

    private fun initData() {
        mFragmentArray = arrayOf(Tab0Frag(), Tab1Frag(), Tab2Frag())
    }

    private fun initView() {
        bottomNavBottomNavLayout.setOnItemCheckedListener {index -> showFragment(index)}
        bottomNavBottomNavLayout.check(0)

        bottomNavTitleLayout.setBackClickListener {DialogUtil.dismissDialog(getSelf())}
        bottomNavShowBubble.setOnClickListener {bottomNavBottomNavLayout.setMsgTip(0, 100)}
        bottomNavHideBubble.setOnClickListener {bottomNavBottomNavLayout.setMsgTip(0, 0)}
    }

    private fun showFragment(index: Int) {
        val tag = DialogUtil.buildTag(mFragmentArray[index])

        val fragmentManager = childFragmentManager
        var fragment: ScaffoldFrag? = fragmentManager.findFragmentByTag(tag) as? ScaffoldFrag

        val fragmentTransaction = fragmentManager.beginTransaction()
        // hide other
        val fragmentList = fragmentManager.fragments
        if (fragmentList.size > 0) {
            for (item in fragmentList) {
                if (item.isAdded) {
                    fragmentTransaction.hide(item)
                }
            }
        }
        // showDialog index fragment
        if (fragment == null) {
            fragment = mFragmentArray[index]
            fragmentTransaction.add(R.id.bottomNavContainer, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }

        fragmentTransaction.commit()

        StatusBarUtil.setColor(activity, resources.getColor(fragment.getStatusBarColor()))
    }
}
