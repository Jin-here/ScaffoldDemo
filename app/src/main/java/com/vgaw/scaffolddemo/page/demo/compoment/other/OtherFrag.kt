package com.vgaw.scaffolddemo.page.demo.compoment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vgaw.scaffold.page.ScaffoldFrag
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffolddemo.R
import kotlinx.android.synthetic.main.other_frag.*
import kotlinx.android.synthetic.main.other_frag.view.*

class OtherFrag : ScaffoldFrag() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.other_frag, container, false)
        StatusBarUtil.addStatusbarHeight(context, view)

        // toolbar
        view.otherTitleLayout.setBackClickListener {DialogUtil.dismissDialog(getSelf())}

        // checkable imagebutton
        view.otherCheckedib.setNormalImageId(android.R.drawable.checkbox_off_background)
        view.otherCheckedib.setCheckedImageId(android.R.drawable.checkbox_on_background)
        view.otherCheckedib.setOnClickListener {otherCheckedib.setChecked(!otherCheckedib.isChecked())}

        view.otherCheckedib1.setNormalBkResId(android.R.drawable.checkbox_off_background)
        view.otherCheckedib1.setCheckedBkResId(android.R.drawable.checkbox_on_background)
        view.otherCheckedib1.setOnClickListener {otherCheckedib1.setChecked(!otherCheckedib1.isChecked())}
        return view
    }
}
