package com.vgaw.scaffolddemo.page.demo.compoment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vgaw.scaffold.page.ScaffoldFrag
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffold.view.CheckedImageButton
import com.vgaw.scaffold.view.TitleLayout
import com.vgaw.scaffolddemo.R

class OtherFrag : ScaffoldFrag() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.other_frag, container, false)
        StatusBarUtil.addStatusbarHeight(context, view)

        // toolbar
        view.findViewById<TitleLayout>(R.id.other_title_layout).setBackClickListener {DialogUtil.dismissDialog(getSelf())}

        // checkable imagebutton
        val otherCheckedib = view.findViewById<CheckedImageButton>(R.id.other_checkedib)
        otherCheckedib.setNormalImageId(android.R.drawable.checkbox_off_background)
        otherCheckedib.setCheckedImageId(android.R.drawable.checkbox_on_background)
        otherCheckedib.setOnClickListener {otherCheckedib.setChecked(!otherCheckedib.isChecked())}

        val otherCheckedib1 = view.findViewById<CheckedImageButton>(R.id.other_checkedib1)
        otherCheckedib1.setNormalBkResId(android.R.drawable.checkbox_off_background)
        otherCheckedib1.setCheckedBkResId(android.R.drawable.checkbox_on_background)
        otherCheckedib1.setOnClickListener {otherCheckedib1.setChecked(!otherCheckedib1.isChecked())}
        return view
    }
}
