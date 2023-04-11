package com.vgaw.scaffolddemo.page.demo.compoment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vgaw.scaffold.page.ScaffoldFrag
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffold.view.TitleLayout
import com.vgaw.scaffolddemo.R

class ViewPagerFrag : ScaffoldFrag() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.viewpager_frag, container, false)
        StatusBarUtil.addStatusbarHeight(context, view)
        view.findViewById<TitleLayout>(R.id.viewpager_title_layout).setBackClickListener {DialogUtil.dismissDialog(getSelf())}
        return view
    }
}
