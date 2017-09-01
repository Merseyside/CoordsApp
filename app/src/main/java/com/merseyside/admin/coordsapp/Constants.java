package com.merseyside.admin.coordsapp;

/**
 * Created by ivan_ on 22.08.2017.
 */

public class Constants {

    public static final String POINTS_KEY = "points";
    public static final String URL = "https://demo.bankplus.ru/mobws/json/pointsList";

    public static int width;
    public static int height;

    public static final int DEFAULT_SCALE = 1;
    public static final int DEFAULT_SEGMENT_SIZE = 5;
    public static final int DEFAULT_POINT_SIZE = 10;
    public static final int DEFAULT_AXE_SIZE = 5;
    public static final int DEFAULT_STROKE_SIZE = 2;
    public static final int DEFAULT_LINE_SIZE = 3;

    public static final float ZOOM_STEP = 0.2f;

    static void swapDensity() {
        int t = Constants.height;
        Constants.height = Constants.width;
        Constants.width = t;
    }
}
