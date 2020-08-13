package com.vgaw.scaffold.util.dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util
import com.vgaw.scaffold.view.rcv.EasyRcvAdapter
import com.vgaw.scaffold.view.rcv.EasyRcvHolder
import com.vgaw.scaffold.view.rcv.OnItemClickListener
import com.vgaw.scaffold.view.rcv.decoration.BaseItemDecoration
import com.vgaw.scaffold.view.rcv.decoration.GridDividerItemDecoration
import kotlinx.android.synthetic.main.menu_dialog_frag.view.*
import kotlinx.android.synthetic.main.menu_item.view.*

class MenuDialogFrag : ScaffoldDialogFrag() {
    private var mListener: OnItemClickListener<String>? = null

    companion object {
        fun newInstance(activity: Activity, menuList: ArrayList<String>): MenuDialogFrag {
            val args = Bundle()
            args.putStringArrayList("menu_list", menuList)
            val fragment = MenuDialogFrag()
            fragment.arguments = args
            return fragment
        }
    }

    fun setMenuItemClickListener(listener: OnItemClickListener<String>) {
        mListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.menu_dialog_frag, container, false)

        val layoutManager: LinearLayoutManager = GridLayoutManager(context, 1)
        view.menu_dialog_menu_list.layoutManager = layoutManager
        val gridDividerItemDecoration: BaseItemDecoration = GridDividerItemDecoration(context, 8)
        view.menu_dialog_menu_list.addItemDecoration(gridDividerItemDecoration)


        val menuList = arguments?.getStringArrayList("menu_list")
        view.menu_dialog_menu_list.adapter = object : EasyRcvAdapter<String>(mActivity, menuList) {
            override fun getHolder(parent: ViewGroup, type: Int) = object : EasyRcvHolder<String>(LayoutInflater.from(mActivity).inflate(R.layout.menu_item, parent, false)) {
                override fun onCreateView(): View {
                    return mView
                }

                override fun refreshView(position: Int, item: String?) {
                    mView.menu_item_content.setTitle(Util.nullToEmpty(item))
                    mView.menu_item_content.setOnItemClickListener(View.OnClickListener { mListener?.onItemClicked(item, position) })
                }
            }
        }
        return view
    }
}