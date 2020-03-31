package com.vgaw.scaffold.page.common.chooseimg;

import android.os.Parcel;
import android.os.Parcelable;

public class CropConfig implements Parcelable {
    private int outputX;
    private int outputY;

    public static boolean valid(CropConfig cropConfig) {
        return (cropConfig != null && cropConfig.outputX > 0 && cropConfig.outputY > 0);
    }

    public CropConfig(int outputX, int outputY) {
        this.outputX = outputX;
        this.outputY = outputY;
    }

    protected CropConfig(Parcel in) {
        outputX = in.readInt();
        outputY = in.readInt();
    }

    public static final Creator<CropConfig> CREATOR = new Creator<CropConfig>() {
        @Override
        public CropConfig createFromParcel(Parcel in) {
            return new CropConfig(in);
        }

        @Override
        public CropConfig[] newArray(int size) {
            return new CropConfig[size];
        }
    };

    public int getOutputX() {
        return outputX;
    }

    public void setOutputX(int outputX) {
        this.outputX = outputX;
    }

    public int getOutputY() {
        return outputY;
    }

    public void setOutputY(int outputY) {
        this.outputY = outputY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(outputX);
        dest.writeInt(outputY);
    }
}
