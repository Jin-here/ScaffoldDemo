package com.vgaw.scaffold.page.qrcode;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final int NO_CAMERA_ID = -1;
    private Camera mCamera;
    protected int mCameraId;
    private CameraConfigurationManager mCameraConfigurationManager;
    protected BarcodeType mBarcodeType = BarcodeType.ONLY_QR_CODE;

    private boolean mTorchOn;

    private Delegate mDelegate;

    protected ProcessDataTask mProcessDataTask;
    private int mWidth = -1;
    private int mHeight = -1;

    // 上次环境亮度记录的时间戳
    private long mLastAmbientBrightnessRecordTime = System.currentTimeMillis();
    // 上次环境亮度记录的索引
    private int mAmbientBrightnessDarkIndex = 0;
    // 环境亮度历史记录的数组，255 是代表亮度最大值
    private static final long[] AMBIENT_BRIGHTNESS_DARK_LIST = new long[]{255, 255, 255, 255};
    // 环境亮度扫描间隔
    private static final int AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME = 150;
    // 亮度低的阀值
    private static final int AMBIENT_BRIGHTNESS_DARK = 60;

    private QRCodeView mParent;
    private int mRectWidth;
    private RecognizeStateListener mListener;

    protected CameraPreview(Context context) {
        super(context);
        getHolder().addCallback(this);
        getHolder().setKeepScreenOn(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getHolder().removeCallback(this);
    }

    public void setData(QRCodeView qrCodeView, int rectWidth) {
        mParent = qrCodeView;
        mRectWidth = rectWidth;
    }

    public Rect getScanBoxAreaRect(int previewHeight) {
        // 此处扩大识别范围是为了提高用户友好性
        int rectWidth = (int) (mRectWidth * 1.4f);
        int leftOffset = (mWidth - rectWidth) / 2;
        int topOffset = (mHeight - rectWidth) / 2;
        Rect rect = new Rect(leftOffset, topOffset, leftOffset + rectWidth, topOffset + rectWidth);

        float ratio = 1.0f * previewHeight / mHeight;

        float centerX = rect.exactCenterX();
        float centerY = rect.exactCenterY();

        float halfWidth = rect.width() / 2f;
        float halfHeight = rect.height() / 2f;
        float newHalfWidth = halfWidth * ratio;
        float newHalfHeight = halfHeight * ratio;

        rect.left = (int) (centerX - newHalfWidth);
        rect.right = (int) (centerX + newHalfWidth);
        rect.top = (int) (centerY - newHalfHeight);
        rect.bottom = (int) (centerY + newHalfHeight);
        return rect;
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        boolean suc = startCamera();
        if (suc) {
            // 计算view的尺寸
            Point screenResolution = BGAQRCodeUtil.getScreenResolution(getContext());
            int width = screenResolution.x;
            int height = screenResolution.y;
            if (mCameraConfigurationManager != null) {
                Point previewResolution = mCameraConfigurationManager.getPreviewResolution();
                int cameraPreviewWidth = previewResolution.x;
                int cameraPreviewHeight = previewResolution.y;
                float screenRatio = ((float) height) / width;
                float cameraRatio = ((float) cameraPreviewHeight) / cameraPreviewWidth;
                if (!BGAQRCodeUtil.floatEqual(screenRatio, cameraRatio)) {
                    if (screenRatio < cameraRatio) {
                        height = (int) (width * cameraRatio);
                    } else {
                        float temp = ((float) height) / cameraPreviewHeight;
                        height *= temp;
                        width = (int) (height / cameraRatio);
                    }
                }
            }
            mWidth = width;
            mHeight = height;
            requestLayout();

            Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
            mProcessDataTask = new ProcessDataTask(previewSize.width, previewSize.height, this, BGAQRCodeUtil.isPortrait(getContext()), mBarcodeType);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        if (mWidth != -1 && mWidth == width) {
            startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopPreview();
        stopCamera();

        if (mProcessDataTask != null) {
            mProcessDataTask.cancelTask();
            mProcessDataTask = null;
        }

        mWidth = -1;
        mHeight = -1;
    }

    protected boolean torchOn() {
        return mTorchOn;
    }

    protected boolean switchTorch() {
        if (mTorchOn) {
            mTorchOn = false;
            return mCameraConfigurationManager.closeFlashlight(mCamera);
        }
        mTorchOn = true;
        return mCameraConfigurationManager.openFlashlight(mCamera);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidth != -1) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        handleAmbientBrightness(data, camera);

        mProcessDataTask.onGetNewFrame(data);
    }

    private boolean startCamera() {
        if (Camera.getNumberOfCameras() == 0) {
            callOpenCameraError();
            return false;
        }
        mCameraId = findCameraIdByFacing(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (mCameraId != NO_CAMERA_ID) {
            try {
                mCamera = Camera.open(mCameraId);

                if (mCamera != null) {
                    mCameraConfigurationManager = new CameraConfigurationManager(getContext());
                    mCameraConfigurationManager.initFromCameraParameters(mCamera);
                    mCameraConfigurationManager.setDesiredCameraParameters(mCamera);

                    startContinuousAutoFocus();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();

                callOpenCameraError();
            }
        } else {
            callOpenCameraError();
        }
        return false;
    }

    private void stopCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void startPreview() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(getHolder());
                mCamera.setPreviewCallback(this);
                mCamera.startPreview();

                callRecognizeStart();
            } catch (IOException e) {
                e.printStackTrace();

                callOpenCameraError();
            }
        }
    }

    private void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        callRecognizeStop();
    }

    private void handleAmbientBrightness(byte[] data, Camera camera) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastAmbientBrightnessRecordTime < AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME) {
            return;
        }
        mLastAmbientBrightnessRecordTime = currentTime;

        int width = camera.getParameters().getPreviewSize().width;
        int height = camera.getParameters().getPreviewSize().height;
        // 像素点的总亮度
        long pixelLightCount = 0L;
        // 像素点的总数
        long pixelCount = width * height;
        // 采集步长，因为没有必要每个像素点都采集，可以跨一段采集一个，减少计算负担，必须大于等于1。
        int step = 10;
        // data.length - allCount * 1.5f 的目的是判断图像格式是不是 YUV420 格式，只有是这种格式才相等
        //因为 int 整形与 float 浮点直接比较会出问题，所以这么比
        if (Math.abs(data.length - pixelCount * 1.5f) < 0.00001f) {
            for (int i = 0; i < pixelCount; i += step) {
                // 如果直接加是不行的，因为 data[i] 记录的是色值并不是数值，byte 的范围是 +127 到 —128，
                // 而亮度 FFFFFF 是 11111111 是 -127，所以这里需要先转为无符号 unsigned long 参考 Byte.toUnsignedLong()
                pixelLightCount += ((long) data[i]) & 0xffL;
            }
            // 平均亮度
            long cameraLight = pixelLightCount / (pixelCount / step);
            // 更新历史记录
            int lightSize = AMBIENT_BRIGHTNESS_DARK_LIST.length;
            AMBIENT_BRIGHTNESS_DARK_LIST[mAmbientBrightnessDarkIndex = mAmbientBrightnessDarkIndex % lightSize] = cameraLight;
            mAmbientBrightnessDarkIndex++;
            boolean isDarkEnv = true;
            // 判断在时间范围 AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME * lightSize 内是不是亮度过暗
            for (long ambientBrightness : AMBIENT_BRIGHTNESS_DARK_LIST) {
                if (ambientBrightness > AMBIENT_BRIGHTNESS_DARK) {
                    isDarkEnv = false;
                    break;
                }
            }
            BGAQRCodeUtil.d("摄像头环境亮度为：" + cameraLight);
            mParent.onCameraAmbientBrightnessChanged(isDarkEnv, torchOn());
        }
    }

    private int findCameraIdByFacing(int cameraFacing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
            try {
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (cameraInfo.facing == cameraFacing) {
                    return cameraId;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return NO_CAMERA_ID;
    }

    private void callOpenCameraError() {
        if (mDelegate != null) {
            mDelegate.onScanQRCodeOpenCameraError();
        }
    }

    protected boolean callOnScanQRCodeSuccess(String result) {
        if (mDelegate != null) {
            return mDelegate.onScanQRCodeSuccess(result);
        }
        return false;
    }

    private void startContinuousAutoFocus() {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            // 连续对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(parameters);
            // 要实现连续的自动对焦，这一句必须加上
            mCamera.cancelAutoFocus();
        } catch (Exception e) {
            BGAQRCodeUtil.e("连续对焦失败");
        }
    }

    protected void setOnRecognizeStateListener(RecognizeStateListener listener) {
        mListener = listener;
    }

    protected void callRecognizeStart() {
        if (mListener != null) {
            mListener.onRecognizeStart();
        }
    }

    protected void callRecognizeStop() {
        if (mListener != null) {
            mListener.onRecognizeStop();
        }
    }

    public interface Delegate {
        /**
         * @param result
         * @return false to continue to recognize, else true
         */
        boolean onScanQRCodeSuccess(String result);

        /**
         * 处理打开相机出错
         */
        void onScanQRCodeOpenCameraError();
    }

    public interface RecognizeStateListener {
        void onRecognizeStart();

        /**
         * may not on the main thread
         */
        void onRecognizeStop();
    }
}