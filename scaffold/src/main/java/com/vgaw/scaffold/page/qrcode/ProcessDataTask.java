package com.vgaw.scaffold.page.qrcode;

import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

class ProcessDataTask {
    private static final int WHAT_DATA = 0;

    private int mPreviewWidth;
    private int mPreviewHeight;
    private boolean mIsPortrait;
    private WeakReference<CameraPreview> mQRCodeViewRef;

    private MultiFormatReader mMultiFormatReader;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private AtomicBoolean mProcessing = new AtomicBoolean(false);

    protected ProcessDataTask(int previewWidth, int previewHeight, CameraPreview qrCodeView, boolean isPortrait, BarcodeType barCodeType) {
        mPreviewWidth = previewWidth;
        mPreviewHeight = previewHeight;
        mQRCodeViewRef = new WeakReference<>(qrCodeView);
        mIsPortrait = isPortrait;

        setupReader(barCodeType);

        mHandlerThread = new HandlerThread("qrcode_scan");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == WHAT_DATA) {
                    byte[] data = (byte[]) msg.obj;
                    ScanResult scanResult = processData(data);

                    CameraPreview qrCodeView = mQRCodeViewRef.get();
                    if (qrCodeView == null) {
                        return;
                    }

                    String result = scanResult == null ? null : scanResult.result;
                    if (TextUtils.isEmpty(result)) {
                    } else {
                        boolean stopRecognize = qrCodeView.callOnScanQRCodeSuccess(result);
                        if (stopRecognize) {
                            return;
                        }
                    }

                    mProcessing.set(false);
                }
            }
        };
    }

    protected void onGetNewFrame(byte[] data) {
        if (!mProcessing.get()) {
            mProcessing.set(true);

            mHandler.obtainMessage(WHAT_DATA, data).sendToTarget();
        }
    }

    protected void cancelTask() {
        mQRCodeViewRef.clear();
        mHandler.removeCallbacksAndMessages(null);
        mHandlerThread.quit();
    }

    private ScanResult processData(byte[] data) {
        if (data == null) {
            return null;
        }

        int width = mPreviewWidth;
        int height = mPreviewHeight;

        if (mIsPortrait) {
            byte[] data1 = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    data1[x * height + height - y - 1] = data[x + y * width];
                }
            }
            int tmp = width;
            width = height;
            height = tmp;

            return processData(data1, width, height);
        }

        return processData(data, width, height);
    }

    private ScanResult processData(byte[] data, int width, int height) {
        CameraPreview mCameraPreview = mQRCodeViewRef.get();
        if (mCameraPreview == null) {
            return null;
        }

        Result rawResult = null;
        Rect scanBoxAreaRect = null;

        try {
            PlanarYUVLuminanceSource source;
            scanBoxAreaRect = mCameraPreview.getScanBoxAreaRect(height);
            if (scanBoxAreaRect != null) {
                source = new PlanarYUVLuminanceSource(data, width, height, scanBoxAreaRect.left, scanBoxAreaRect.top, scanBoxAreaRect.width(),
                        scanBoxAreaRect.height(), false);
            } else {
                source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            }

            rawResult = mMultiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMultiFormatReader.reset();
        }

        if (rawResult == null) {
            return null;
        }

        String result = rawResult.getText();

        return new ScanResult(result);
    }

    private void setupReader(BarcodeType barCodeType) {
        mMultiFormatReader = new MultiFormatReader();

        if (barCodeType == BarcodeType.ONE_DIMENSION) {
            mMultiFormatReader.setHints(QRCodeDecoder.ONE_DIMENSION_HINT_MAP);
        } else if (barCodeType == BarcodeType.TWO_DIMENSION) {
            mMultiFormatReader.setHints(QRCodeDecoder.TWO_DIMENSION_HINT_MAP);
        } else if (barCodeType == BarcodeType.ONLY_QR_CODE) {
            mMultiFormatReader.setHints(QRCodeDecoder.QR_CODE_HINT_MAP);
        } else if (barCodeType == BarcodeType.ONLY_CODE_128) {
            mMultiFormatReader.setHints(QRCodeDecoder.CODE_128_HINT_MAP);
        } else if (barCodeType == BarcodeType.ONLY_EAN_13) {
            mMultiFormatReader.setHints(QRCodeDecoder.EAN_13_HINT_MAP);
        } else if (barCodeType == BarcodeType.HIGH_FREQUENCY) {
            mMultiFormatReader.setHints(QRCodeDecoder.HIGH_FREQUENCY_HINT_MAP);
        } else {
            mMultiFormatReader.setHints(QRCodeDecoder.ALL_HINT_MAP);
        }
    }
}
