package com.vgaw.scaffolddemo.page.demo.compoment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.vgaw.scaffold.page.MockFrag
import com.vgaw.scaffold.page.ScaffoldFrag
import com.vgaw.scaffold.util.context.ContextManager
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.phone.DensityUtil
import com.vgaw.scaffolddemo.R
import com.vgaw.scaffolddemo.page.demo.compoment.bottomnav.BottomNavFrag
import com.vgaw.scaffolddemo.page.demo.compoment.other.OtherFrag
import com.vgaw.scaffolddemo.page.demo.compoment.tab.TabFragment
import com.vgaw.scaffolddemo.page.demo.compoment.viewpager.ViewPagerFrag
import java.util.*

class CompomentFrag : MockFrag() {
    private lateinit var compomentContainer : ViewGroup
    private val mItemList = ArrayList<ItemBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavFragment = BottomNavFrag.newInstance()
        mItemList.add(ItemBean(ItemBean.TYPE_LABEL, "bottom nav"))
        mItemList.add(ItemBean(ItemBean.TYPE_CONTENT, "基本使用", bottomNavFragment))

        /*mItemList.add(new ItemBean(ItemBean.TYPE_LABEL, "listview"))
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "基本使用"))
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "上拉下拉"))*/

        val tabFragment = TabFragment()
        mItemList.add(ItemBean(ItemBean.TYPE_LABEL, "tab"))
        mItemList.add(ItemBean(ItemBean.TYPE_CONTENT, "基本使用", tabFragment))

        val viewPagerFrag = ViewPagerFrag()
        mItemList.add(ItemBean(ItemBean.TYPE_LABEL, "viewpager"))
        mItemList.add(ItemBean(ItemBean.TYPE_CONTENT, "基本使用", viewPagerFrag))

        val otherFrag = OtherFrag()
        mItemList.add(ItemBean(ItemBean.TYPE_LABEL, "other"))
        mItemList.add(ItemBean(ItemBean.TYPE_CONTENT, "基本使用", otherFrag))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.compoment_frag, container, false)
        compomentContainer = view.findViewById(R.id.compoment_container)
        initView()
        return view
    }

    private fun initView() {
        for (item in mItemList) {
            if (item.type == ItemBean.TYPE_CONTENT) {
                if (item?.frag != null) {
                    addContentView(item.name, item.frag)
                }
            } else {
                addLabelView(item.name)
            }
        }
    }

    private fun addLabelView(name: String) {
        val tv = TextView(context)
        tv.setPadding(DensityUtil.dp2px(context, 10F), 0, 0, 0)
        tv.setBackgroundResource(android.R.color.darker_gray)
        tv.text = name
        tv.textSize = 16F
        tv.setTextColor(resources.getColor(R.color.black))
        tv.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        compomentContainer.addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dp2px(context, 36F)))
    }

    private fun addContentView(name: String, frag: ScaffoldFrag) {
        val tv = TextView(context)
        tv.setPadding(DensityUtil.dp2px(context, 10F), 0, 0, 0)
        tv.text = name
        tv.textSize = 16F
        tv.setTextColor(resources.getColor(R.color.black))
        tv.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        tv.setOnClickListener {
            if (frag != null) {
                val fragmentManager = ContextManager.getInstance().fragmentManager
                if (fragmentManager != null) {
                    DialogUtil.showDialog(frag, fragmentManager)
                }
            }
        }
        compomentContainer.addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dp2px(context, 64F)))
    }

    private class ItemBean(val type: Int = -1, val name: String, val frag: ScaffoldFrag? = null) {
        companion object {
            internal const val TYPE_LABEL = 0
            internal const val TYPE_CONTENT = 1
        }
    }
}
