package com.merseyside.admin.coordsapp;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by ivan_ on 22.08.2017.
 */

public class Point implements Serializable, Comparable {
    private float x,y;
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Point point = (Point) o;
        if (x == point.getX()) return 0;
        else return x > point.getX() ? 1 : -1;
    }
}
