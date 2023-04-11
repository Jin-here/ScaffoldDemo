package com.vgaw.scaffold.util.dialog;

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util
import com.vgaw.scaffold.util.phone.DensityUtil
import com.vgaw.scaffold.view.rcv.EasyRcvAdapter
import com.vgaw.scaffold.view.rcv.EasyRcvHolder
import com.vgaw.scaffold.view.rcv.OnItemClickListener
import com.vgaw.scaffold.view.rcv.decoration.LinearDividerItemDecoration

class BottomMenuDialog: ScaffoldDialogFrag() {
    private lateinit var mBottomMenuDialogList: RecyclerView

    private var mTitle: String? = null
    private var mDataList: ArrayList<String>? = null

    private var mAdapter: EasyRcvAdapter<String>? = null

    companion object {
        fun newInstance(menuList: ArrayList<String>): BottomMenuDialog {
            return newInstance(null, menuList)
        }

        fun newInstance(title: String? = null, menuList: ArrayList<String>): BottomMenuDialog {
            val args = Bundle()
            title?.let { args.putString("title", it) }
            args.putStringArrayList("menu_list", menuList)
            val fragment = BottomMenuDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private var mItemClickListener: OnItemClickListener<String>? = null

    fun setItemClickListener(itemClickListener: OnItemClickListener<String>) {
        mItemClickListener = itemClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            mTitle = getString("title")
            mDataList = getStringArrayList("menu_list")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_menu_dialog, container, false)
        mBottomMenuDialogList = view.findViewById(R.id.bottom_menu_dialog_list)

        val bottomMenuDialogTitle = view.findViewById<TextView>(R.id.bottom_menu_dialog_title)
        val bottomMenuDialogDivider = view.findViewById<View>(R.id.bottom_menu_dialog_divider)
        val bottomMenuDialogClose = view.findViewById<View>(R.id.bottom_menu_dialog_close)

        if (TextUtils.isEmpty(mTitle)) {
            bottomMenuDialogTitle.visibility = View.GONE
            bottomMenuDialogDivider.visibility = View.GONE
            bottomMenuDialogClose.visibility = View.GONE
        } else {
            bottomMenuDialogTitle.text = Util.nullToEmpty(mTitle)
            view.findViewById<View>(R.id.bottom_menu_dialog_close).setOnClickListener {
                dismissDialog()
            }
            bottomMenuDialogTitle.visibility = View.VISIBLE
            bottomMenuDialogDivider.visibility = View.VISIBLE
            bottomMenuDialogClose.visibility = View.VISIBLE
        }
        initView(view)
        return view
    }

    open fun onItemClick(item: String, position: Int) {
        mItemClickListener?.apply { onItemClicked(item!!, position) }
        dismissDialog()
    }

    private fun initView(view: View) {
        view.setOnClickListener{ dismissDialog() }

        mAdapter = object : EasyRcvAdapter<String>(context, mDataList) {
            override fun getHolder(parent: ViewGroup, type: Int): EasyRcvHolder<String> {
                return object : EasyRcvHolder<String>(LayoutInflater.from(context).inflate(R.layout.bottom_menu_dialog_item, parent, false)){
                    private lateinit var mBottomMenuDialogItemContent: TextView

                    override fun onCreateView(): View {
                        mBottomMenuDialogItemContent = mView.findViewById(R.id.bottom_menu_dialog_item_content)
                        return mView
                    }

                    override fun refreshView(position: Int, item: String?) {
                        mBottomMenuDialogItemContent.text = Util.nullToEmpty(item)

                        mView.setOnClickListener {
                            onItemClick(item!!, position)
                        }
                    }
                }
            }
        }
        mBottomMenuDialogList.layoutManager = LinearLayoutManager(context)
        mBottomMenuDialogList.addItemDecoration(LinearDividerItemDecoration(requireContext(), marginLeft = DensityUtil.dp2px(context, 20F)))
        mBottomMenuDialogList.adapter = mAdapter
    }

    fun showDialog(fragmentManager: FragmentManager) {
        DialogUtil.showDialog(this, fragmentManager, false)
    }

    fun dismissDialog() {
        DialogUtil.dismissDialog(this)
    }
}
