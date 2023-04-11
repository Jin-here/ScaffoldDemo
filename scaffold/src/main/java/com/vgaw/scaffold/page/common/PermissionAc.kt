package com.vgaw.scaffold.page.common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.vgaw.scaffold.R
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.page.ScaffoldDialog
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.phone.SystemPageUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import timber.log.Timber

/**
 * todo
 * 1. 多个权限同一个描述；
 * 2. 不分手机功能异常；
 * 3. 先申请权限再弹出描述；
 *
 * 1. 读写权限自动适配
 */
class PermissionAc : ScaffoldAc() {
    companion object {
        private const val REQ_CODE_PERMISSION = 0

        private val sPermissionDesMap = HashMap<String, String>().apply {
            this[Manifest.permission.READ_EXTERNAL_STORAGE] = "读取文件"
            this[Manifest.permission.WRITE_EXTERNAL_STORAGE] = "存储文件"
            this[Manifest.permission.RECORD_AUDIO] = "麦克风"
            this[Manifest.permission.READ_PHONE_STATE] = "设备信息"
            this[Manifest.permission.CAMERA] = "相机"
            this[Manifest.permission.ACCESS_COARSE_LOCATION] = "粗略位置信息"
            this[Manifest.permission.ACCESS_FINE_LOCATION] = "精确位置信息"
            this[Manifest.permission.CALL_PHONE] = "拨打电话"
            this[Manifest.permission.USE_SIP] = "使用SIP"
            this[Manifest.permission.WRITE_CONTACTS] = "修改通讯录"
            this[Manifest.permission.ACCESS_BACKGROUND_LOCATION] = "后台获取位置【请点击始终允许】"
        }

        fun startActivityForResult(activity: Activity, reqCode: Int, perssionArray: Array<String>): Boolean {
            if (!hasPermission(activity, perssionArray)) {
                startActivityForResult(activity, reqCode, perssionArray, null)
                return false
            }
            return true
        }

        fun startActivityForResult(activity: Activity, reqCode: Int, perssionArray: Array<String>, des: String?): Boolean {
            if (!hasPermission(activity, perssionArray)) {
                val intent = Intent(activity, PermissionAc::class.java)
                intent.putExtra("permission_array", perssionArray)
                if (des != null) {
                    intent.putExtra("des", des)
                }
                activity.startActivityForResult(intent, reqCode)
                activity.overridePendingTransition(0, 0)
                return false
            }
            return true
        }

        fun hasPermission(activity: Activity, permissionArray: Array<String>): Boolean {
            for (item in permissionArray) {
                if (ActivityCompat.checkSelfPermission(activity, item) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
    }

    private var mPermissionDeniedMap: HashMap<String, Boolean>? = null

    private lateinit var mPermissionArray: Array<String>
    private var mDes: String? = null

    private var mCheck: Boolean = false

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_ac)
        StatusBarUtil.setColor(this, Color.TRANSPARENT)

        val intent = intent
        mPermissionArray = intent.getStringArrayExtra("permission_array")!!
        mDes = intent.getStringExtra("des")
        requestPermission()
    }

    override fun onResume() {
        super.onResume()
        if (mCheck) {
            mCheck = false
            if (requestPermission()) {
                onSuc()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mCheck = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestPermission()) {
            onSuc()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        onFail()
    }

    private fun onSuc() {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onFail() {
        val intent = intent
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun requestPermission(): Boolean {
        val permissionList = ArrayList<String>()
        var hasAlwaysDenied = false
        for (item in mPermissionArray) {
            if (proPermission(getSelf(), item, permissionList)) {
                hasAlwaysDenied = true
            }
        }
        if (permissionList.size > 0) {
            val sb = StringBuilder()
            permissionList.forEach { sb.append(sPermissionDesMap[it]).append("，") }

            val des = mDes ?: String.format("为了正常使用%s，请授予以下权限", getString(R.string.app_name))
            val baseDialog = ScaffoldDialog.newInstance(String.format("%s：\n%s", des, sb.substring(0, sb.length - 1)), "取消", /*if hasAlwaysDenied "前往设置" else */"开始授权")

            baseDialog.setOnLeftBtnClickListener(View.OnClickListener {
                DialogUtil.dismissDialog(supportFragmentManager, "permission_dialog")

                onFail()
            })
            val finalHasAlwaysDenied = hasAlwaysDenied
            baseDialog.setOnRightBtnClickListener(View.OnClickListener {
                DialogUtil.dismissDialog(supportFragmentManager, "permission_dialog")

                if (finalHasAlwaysDenied) {
                    SystemPageUtil.jump2PermissoinPage(getSelf())
                } else {
                    val tempList = mutableListOf<String>()
                    for (i in 0 until permissionList.size) {
                        tempList.add(permissionList[i])
                        tempList.add(sPermissionDesMap.get(permissionList[i])!!)
                    }
                    ActivityCompat.requestPermissions(getSelf(),
                        tempList.toTypedArray(), REQ_CODE_PERMISSION)
                }
            })
            DialogUtil.showDialog(baseDialog, supportFragmentManager, "permission_dialog")
            return false
        }
        return true
    }

    /**
     * 对于拒绝的权限，是否被永久拒绝（不再提示）
     * @param activity
     * @param permission
     * @param permissionList
     * @param sb
     * @return
     */
    private fun proPermission(activity: Activity, permission: String, permissionList: ArrayList<String>): Boolean {
        val check = ActivityCompat.checkSelfPermission(activity, permission)
        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
            Timber.d("grant: %s", check)
        }
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission)

            if (mPermissionDeniedMap == null) {
                mPermissionDeniedMap = HashMap()
            }
            val deniedBefore = mPermissionDeniedMap!!.get(permission)
            val deniedNow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            if (deniedBefore == null) {
                mPermissionDeniedMap!!.put(permission, deniedNow)
            } else {
                if (!deniedBefore && !deniedNow) {
                    return true
                }
            }
        }
        return false
    }
}
