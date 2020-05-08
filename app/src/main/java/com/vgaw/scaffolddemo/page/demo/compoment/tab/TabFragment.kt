package com.vgaw.scaffolddemo.page.demo.compoment.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.vgaw.scaffold.page.ScaffoldFrag
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffold.view.TitleLayout
import com.vgaw.scaffold.view.tab.DefaultTabAdapter
import com.vgaw.scaffold.view.tab.DefaultTabStyleBuilder
import com.vgaw.scaffold.view.tab.SlidingTabLayout
import com.vgaw.scaffold.view.tab.indicator.RoundRectShortIndicator
import com.vgaw.scaffold.view.vp.EasyFragmentPagerAdapter
import com.vgaw.scaffolddemo.R
import com.vgaw.scaffolddemo.page.demo.compoment.Tab0Frag
import com.vgaw.scaffolddemo.page.demo.compoment.Tab1Frag
import com.vgaw.scaffolddemo.page.demo.compoment.Tab2Frag
import kotlinx.android.synthetic.main.tab_frag.view.*

class TabFragment : ScaffoldFrag() {
    private lateinit var tabVp: ViewPager
    private lateinit var tabTab: SlidingTabLayout
    private lateinit var tabTitleLayout: TitleLayout
    private lateinit var tabShowBubble: MaterialButton

    private lateinit var mFragArray: Array<ScaffoldFrag>
    private lateinit var mFragTitleArray: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_frag, container, false)
        StatusBarUtil.addStatusbarHeight(context, view)
        tabVp = view.tabVp
        tabTab = view.tabTab
        tabTitleLayout = view.tabTitleLayout
        tabShowBubble = view.tabShowBubble

        initData()
        initView()
        return view
    }

    private fun initData() {
        mFragArray = arrayOf(Tab0Frag(), Tab1Frag(), Tab2Frag())
        mFragTitleArray = arrayOf("tab0", "tab1", "tab2")
    }

    private fun initView() {
        tabVp.adapter = EasyFragmentPagerAdapter(childFragmentManager, mFragArray)
        tabTab.setAdapter(DefaultTabAdapter(activity, mFragTitleArray, DefaultTabStyleBuilder()
                .txtColor(R.color.black, R.color.black2)
                .txtSize(16, 22).build()))
        tabTab.setSelectedIndicator(RoundRectShortIndicator(context))
        tabTab.setViewPager(tabVp, 0)

        tabTitleLayout.setBackClickListener { DialogUtil.dismissDialog(getSelf())}
        tabShowBubble.setOnClickListener {tabTab.setMsgTip(0, 100)}
        tabShowBubble.setOnClickListener {tabTab.setMsgTip(0, 0)}
    }
}
