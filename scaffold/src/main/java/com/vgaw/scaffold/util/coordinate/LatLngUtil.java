package com.vgaw.scaffold.util.coordinate;

import java.text.DecimalFormat;

/**
 * Created by caojin on 2017/1/6.
 * <p>
 * 所有计算基于UTM，非三维系统
 */

public class LatLngUtil {

    /**
     * 获取两点之间的距离
     *
     * @param startLng
     * @param startLat
     * @param endLng
     * @param endLat
     * @return
     */
    public static float calculateLineDistance(double startLat, double startLng, double endLat, double endLng) {
        try {
            double var2 = 0.01745329251994329D;
            double var4 = startLng;
            double var6 = startLat;
            double var8 = endLng;
            double var10 = endLat;
            var4 *= var2;
            var6 *= var2;
            var8 *= var2;
            var10 *= var2;
            double var12 = Math.sin(var4);
            double var14 = Math.sin(var6);
            double var16 = Math.cos(var4);
            double var18 = Math.cos(var6);
            double var20 = Math.sin(var8);
            double var22 = Math.sin(var10);
            double var24 = Math.cos(var8);
            double var26 = Math.cos(var10);
            double[] var28 = new double[3];
            double[] var29 = new double[3];
            var28[0] = var18 * var16;
            var28[1] = var18 * var12;
            var28[2] = var14;
            var29[0] = var26 * var24;
            var29[1] = var26 * var20;
            var29[2] = var22;
            double var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
            return (float) (Math.asin(var30 / 2.0D) * 1.27420015798544E7D);
        } catch (Throwable var32) {
            var32.printStackTrace();
            return 0.0F;
        }
    }

    /**
     * @param start [lat, lng]
     * @param end   [lat, lng]
     * @return
     */
    public static double getPolarAngle(double[] start, double[] end) {
        CoordinateSysConversion conversion = new CoordinateSysConversion();
        CoordinateSysConversion.UTM sUTM = conversion.latLon2UTM(start[0], start[1]);
        CoordinateSysConversion.UTM eUTM = conversion.latLon2UTM(end[0], end[1]);
        int dX = eUTM.easting - sUTM.easting;
        int dY = eUTM.northing - sUTM.northing;
        if (dX == 0) {
            if (dY > 0) {
                return 90;
            }
            if (dY < 0) {
                return 270;
            }
            return -1;
        }
        if (dY == 0) {
            if (dX > 0) {
                return 0;
            }
            if (dX < 0) {
                return 180;
            }
        }
        double tempAngle = Math.toDegrees(Math.atan((double) dY / (double) dX));
        // 第一象限：raw
        // 第二象限：180 + raw
        // 第三象限：180 + raw
        // 第四象限：360 + raw
        if (dX > 0) {
            return dY > 0 ? tempAngle : (360 + tempAngle);
        } else {
            return 180 + tempAngle;
        }
    }

    public static double vertical(double rawAngle) {
        return -1;
    }

    /**
     * @param start    起始经纬度[lat, lng]
     * @param angle    极坐标角度
     * @param distance 移动距离
     * @return
     */
    public static double[] getLatLngAfterMove(double[] start, double angle, double distance) {
        CoordinateSysConversion conversion = new CoordinateSysConversion();
        DecimalFormat df = new DecimalFormat("0.00");
        double verticalDistance = Double.parseDouble(df.format(conversion.SIN(Math.toRadians(angle)) * distance));
        double horizontalDistance = Double.parseDouble(df.format(conversion.COS(Math.toRadians(angle)) * distance));

        CoordinateSysConversion.UTM utm = conversion.latLon2UTM(start[0], start[1]);

        horizontalDistance += utm.easting;
        verticalDistance += utm.northing;

        String utmAfterMove = utm.zone + " " + utm.band + " " + horizontalDistance + " " + verticalDistance;

        return conversion.utm2LatLon(utmAfterMove);
    }
}
