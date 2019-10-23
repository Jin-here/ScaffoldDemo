package com.vgaw.scaffold.page.qrcode;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.Collection;
import java.util.List;

final class CameraConfigurationManager {
    private final Context mContext;
    private Point mPreviewResolution;

    CameraConfigurationManager(Context context) {
        mContext = context;
    }

    void initFromCameraParameters(Camera camera) {
        Point screenResolution = BGAQRCodeUtil.getScreenResolution(mContext);
        Point screenResolutionForCamera = new Point();
        screenResolutionForCamera.x = screenResolution.x;
        screenResolutionForCamera.y = screenResolution.y;

        if (BGAQRCodeUtil.isPortrait(mContext)) {
            screenResolutionForCamera.x = screenResolution.y;
            screenResolutionForCamera.y = screenResolution.x;
        }

        mPreviewResolution = findBestPreviewSizeValue(camera.getParameters().getSupportedPreviewSizes(), screenResolution);
    }

    public Point getPreviewResolution() {
        return mPreviewResolution;
    }

    private static boolean autoFocusAble(Camera camera) {
        List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
        String focusMode = findSettableValue(supportedFocusModes, Camera.Parameters.FOCUS_MODE_AUTO);
        return focusMode != null;
    }

    void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(mPreviewResolution.y, mPreviewResolution.x);

        // https://github.com/googlesamples/android-vision/blob/master/visionSamples/barcode-reader/app/src/main/java/com/google/android/gms/samples/vision/barcodereader/ui/camera/CameraSource.java
        int[] previewFpsRange = selectPreviewFpsRange(camera, 60.0f);
        if (previewFpsRange != null) {
            parameters.setPreviewFpsRange(
                    previewFpsRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX],
                    previewFpsRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
        }

        camera.setDisplayOrientation(getDisplayOrientation());
        camera.setParameters(parameters);
    }

    /**
     * Selects the most suitable preview frames per second range, given the desired frames per
     * second.
     *
     * @param camera            the camera to select a frames per second range from
     * @param desiredPreviewFps the desired frames per second for the camera preview frames
     * @return the selected preview frames per second range
     */
    private int[] selectPreviewFpsRange(Camera camera, float desiredPreviewFps) {
        // The camera API uses integers scaled by a factor of 1000 instead of floating-point frame
        // rates.
        int desiredPreviewFpsScaled = (int) (desiredPreviewFps * 1000.0f);

        // The method for selecting the best range is to minimize the sum of the differences between
        // the desired value and the upper and lower bounds of the range.  This may select a range
        // that the desired value is outside of, but this is often preferred.  For example, if the
        // desired frame rate is 29.97, the range (30, 30) is probably more desirable than the
        // range (15, 30).
        int[] selectedFpsRange = null;
        int minDiff = Integer.MAX_VALUE;
        List<int[]> previewFpsRangeList = camera.getParameters().getSupportedPreviewFpsRange();
        for (int[] range : previewFpsRangeList) {
            int deltaMin = desiredPreviewFpsScaled - range[Camera.Parameters.PREVIEW_FPS_MIN_INDEX];
            int deltaMax = desiredPreviewFpsScaled - range[Camera.Parameters.PREVIEW_FPS_MAX_INDEX];
            int diff = Math.abs(deltaMin) + Math.abs(deltaMax);
            if (diff < minDiff) {
                selectedFpsRange = range;
                minDiff = diff;
            }
        }
        return selectedFpsRange;
    }

    boolean openFlashlight(Camera camera) {
        return doSetTorch(camera, true);
    }

    boolean closeFlashlight(Camera camera) {
        return doSetTorch(camera, false);
    }

    private boolean doSetTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        if (flashLightAvailable()) {
            String flashMode;
            if (newSetting) {
                flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_TORCH, Camera.Parameters.FLASH_MODE_ON);
            } else {
                flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_OFF);
            }
            if (flashMode != null) {
                parameters.setFlashMode(flashMode);
                camera.setParameters(parameters);
                return true;
            }
        }
        return false;
    }

    private boolean flashLightAvailable() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
        String result = null;
        if (supportedValues != null) {
            for (String desiredValue : desiredValues) {
                if (supportedValues.contains(desiredValue)) {
                    result = desiredValue;
                    break;
                }
            }
        }
        return result;
    }

    private int getDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return 0;
        }
        Display display = wm.getDefaultDisplay();

        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private static Point findBestPreviewSizeValue(List<Camera.Size> supportSizeList, Point screenResolution) {
        float ratioDiff = Integer.MAX_VALUE;
        float wantRatio = ((float) screenResolution.y) / screenResolution.x;
        Point resultResolution = new Point();
        for (Camera.Size item : supportSizeList) {
            int cameraX = item.height;
            int cameraY = item.width;
            if (cameraX == screenResolution.x && cameraY == screenResolution.y) {
                // 如果一样，直接返回
                resultResolution.x = cameraX;
                resultResolution.y = cameraY;
                break;
            } else {
                float crtRatio = ((float) cameraY) / cameraX;
                float temp = Math.abs(crtRatio - wantRatio);
                if (temp < ratioDiff) {
                    // 取与结果相近者
                    resultResolution.x = cameraX;
                    resultResolution.y = cameraY;

                    ratioDiff = temp;
                } else if (BGAQRCodeUtil.floatEqual(temp, ratioDiff)) {
                    // 比例相同，取分辨率大者
                    if (cameraX > resultResolution.x) {
                        resultResolution.x = cameraX;
                        resultResolution.y = cameraY;
                    }
                }
            }
        }
        return resultResolution;
    }
}