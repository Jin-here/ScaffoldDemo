package com.vgaw.scaffold.page.common.chooseimg

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.vgaw.scaffold.R
import com.vgaw.scaffold.ScaffoldSetting
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.util.FileUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.choose_img_ac.*
import kotlinx.coroutines.launch
import java.io.File

class ChooseImgAc : ScaffoldAc() {
    companion object {
        private const val IMG_SUFFIX = ".jpeg"
        private const val REQUEST_CODE_CHOOSE_IMG_FROM_FILE = 70
        private const val REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA = 71
        private const val REQUEST_CODE_IMG_CLIP = 72
        private const val REQUEST_CODE_CAMERA_PERMISSION = 81

        /**
         * intent返回值：
         * data:文件路径，字符串类型
         *
         * 默认对图片进行裁剪
         *
         * @param activity
         * @param requestCode
         */
        fun startActivityForResult(activity: Activity, requestCode: Int, cropConfig: CropConfig? = null) {
            val intent = Intent(activity, ChooseImgAc::class.java)
            if (CropConfig.valid(cropConfig)) {
                intent.putExtra("crop_config", cropConfig)
            }
            activity.startActivityForResult(intent, requestCode)
            activity.overridePendingTransition(0, 0)
        }

        fun startActivityForResult(fragment: Fragment, requestCode: Int, cropConfig: CropConfig? = null) {
            val intent = Intent(fragment.context, ChooseImgAc::class.java)
            if (CropConfig.valid(cropConfig)) {
                intent.putExtra("crop_config", cropConfig)
            }
            fragment.startActivityForResult(intent, requestCode)
            val activity = fragment.activity
            if (activity != null && !activity.isDestroyed) {
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    private var mCameraUri: Uri? = null
    private var mCameraFile: File? = null

    private var mCropConfig: CropConfig? = null

    private var mCropFileUri: Uri? = null
    private var mPermissionMap: MutableMap<Uri, Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_img_ac)
        StatusBarUtil.setColor(this, Color.TRANSPARENT)

        val intent = getIntent()
        mCropConfig = intent.getParcelableExtra("crop_config")

        chooseImgBg.setOnClickListener { finish() }
        chooseImgFromAlbum.setOnClickListener { callFile() }
        chooseImgFromCamera.setOnClickListener { callCamera() }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CHOOSE_IMG_FROM_FILE -> {
                    if (data != null) {
                        if (mCropConfig != null) {
                            callCrop(data.getData(), false)
                        } else {
                            onGetResult(FileUtil.from(getSelf(), data.getData()).absolutePath)
                        }
                    }
                }
                REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA -> {
                    if (mCropConfig != null) {
                        callCrop(mCameraUri, true)
                    } else {
                        if (mCameraFile != null) {
                            onGetResult(mCameraFile!!.getAbsolutePath()!!)
                        }
                    }
                }
                REQUEST_CODE_IMG_CLIP -> {
                    if (mCropFileUri != null) {
                        onGetResult(FileUtil.from(getSelf(), mCropFileUri).absolutePath)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            revokePermission()
        }
    }

    private fun onGetResult(data: String) {
        var resultPath: String? = data
        // 压缩图片
        val file = File(data)
        val fileSize = file.length()
        if (fileSize > ScaffoldSetting.MAX_IMG_UPLOAD_SIZE) {
            lifecycleScope.launch {
                val compressedFile = Compressor.compress(this@ChooseImgAc, file) {
                    size(ScaffoldSetting.MAX_IMG_UPLOAD_SIZE)
                }
                resultPath = compressedFile.absolutePath
                onGetResult1(resultPath)
            }
        } else{
            onGetResult1(resultPath)
        }
    }

    private fun onGetResult1(path: String?) {
        if (path != null) {
            val intent = getIntent()
            intent.putExtra("data", path)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var granted = true
        for (item in grantResults) {
            if (item != PackageManager.PERMISSION_GRANTED) {
                granted = false
                break
            }
        }
        if (granted) {
            if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
                callCamera()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun grantUriPermission(intent: Intent, uri: Uri, permission: Int) {
        if (mPermissionMap == null) {
            mPermissionMap = HashMap()
        }
        mPermissionMap!!.put(uri, permission)
        val resolveInfoList: MutableList<ResolveInfo> = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resolveInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(packageName, uri, permission)
        }
    }

    private fun revokePermission() {
        if (mPermissionMap != null) {
            for ((key, value) in mPermissionMap!!) {
                revokeUriPermission(key, value)
            }
        }
    }

    private fun callCrop(fileUri: Uri?, fromCamera: Boolean) {
        if (fileUri != null) {
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(fileUri, "image/*")
            intent.putExtra("crop", "true")
            // X方向上的比例
            intent.putExtra("aspectX", 1)
            // Y方向上的比例
            intent.putExtra("aspectY", 1)
            // scale: 是否保留比例
            // circleCrop: 是否是圆形裁剪区域
            if (mCropConfig != null) {
                intent.putExtra("outputX", mCropConfig!!.getOutputX())
                intent.putExtra("outputY", mCropConfig!!.getOutputY())
            }

            val cropFile = createAppSpecificImgFile()
            mCropFileUri = FileProvider.getUriForFile(getSelf(),
                    getApplicationContext().getPackageName() + ".scaffold.fileprovider", cropFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropFileUri)
            intent.putExtra("return-data", false)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.putExtra("noFaceDetection", true)

            if (fromCamera) {
                grantUriPermission(intent, fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            grantUriPermission(intent, mCropFileUri!!, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            startActivityForResult(intent, REQUEST_CODE_IMG_CLIP)
        }
    }

    private fun callFile() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction("android.intent.action.PICK")
        intent.addCategory("android.intent.category.DEFAULT")

        startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMG_FROM_FILE)
    }

    private fun callCamera() {
        // check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA_PERMISSION)
            return
        }

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            photoFile = createAppSpecificImgFile()
            mCameraFile = photoFile
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraUri = FileProvider.getUriForFile(this, getPackageName() + ".scaffold.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri)
            }
        }

        startActivityForResult(takePictureIntent, REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA)
    }

    private fun createAppSpecificImgFile() = File(getAppSpecificDir(), FileUtil.generateRandomFileName(IMG_SUFFIX))

    private fun getAppSpecificDir(): File {
        val dir = getCacheDir()
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
}
