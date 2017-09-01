package com.merseyside.admin.coordsapp.Coords;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.merseyside.admin.coordsapp.Constants;
import com.merseyside.admin.coordsapp.Point;

import java.util.ArrayList;

/**
 * Created by ivan_ on 11.07.2017.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private DrawThread game;
    private ArrayList<Point> points;
    private SurfaceHolder holder;
    private float scale;

    public MySurfaceView(Context context, ArrayList<Point> points) {
        super(context);
        getHolder().addCallback(this);
        this.points = points;
        scale = Constants.DEFAULT_SCALE;
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        holder = surfaceHolder;
        createThread(Constants.DEFAULT_SCALE);
    }

    private void createThread(float scale) {
        game = new DrawThread(holder, points, scale);
        game.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        createThread(scale);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        game.interrupt();
        game = null;
    }

    public void zoomOn() {
        if (scale < 2) {
            scale += Constants.ZOOM_STEP;
            createThread(scale);
        }
    }

    public void zoomOut() {
        if (scale - Constants.ZOOM_STEP > 0.2) {
            scale -= Constants.ZOOM_STEP;
            createThread(scale);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d("Event", "X = " + String.valueOf(motionEvent.getX()) + " Y = " + String.valueOf(motionEvent.getY()));
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        return false;
    }
}
