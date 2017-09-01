package com.merseyside.admin.coordsapp.Main;

import com.merseyside.admin.coordsapp.Point;

import java.util.ArrayList;

/**
 * Created by ivan_ on 22.08.2017.
 */

public interface CoordsInteractor {
    interface OnFinishedResult {
        void onSuccess(ArrayList<Point> points);
        void onError(String message);
    }

    void getCoords(int count);
}
