package com.merseyside.admin.coordsapp.Coords;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import com.merseyside.admin.coordsapp.Application;
import com.merseyside.admin.coordsapp.Point;
import com.merseyside.admin.coordsapp.R;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.merseyside.admin.coordsapp.Constants.DEFAULT_AXE_SIZE;
import static com.merseyside.admin.coordsapp.Constants.DEFAULT_LINE_SIZE;
import static com.merseyside.admin.coordsapp.Constants.DEFAULT_POINT_SIZE;
import static com.merseyside.admin.coordsapp.Constants.DEFAULT_SEGMENT_SIZE;
import static com.merseyside.admin.coordsapp.Constants.DEFAULT_STROKE_SIZE;
import static com.merseyside.admin.coordsapp.Constants.height;
import static com.merseyside.admin.coordsapp.Constants.width;


public class DrawThread extends Thread {

    @Inject Context context;
    private Canvas canvas = new Canvas();
    private SurfaceHolder holder;
    private ArrayList<Point> points;
    private float scale;
    private int center_x, center_y;

    DrawThread(SurfaceHolder holder, ArrayList<Point> points, float scale) {
        Application.getComponent().inject(this);
        this.holder = holder;
        this.points = points;
        this.scale = scale;
        center_x = width/2;
        center_y = height/2;
    }

    private void draw() {
        canvas = holder.lockCanvas(null);
        try {
            canvas.drawColor(Color.WHITE);
        } catch (NullPointerException e){
            return;
        }
        drawAxes();
        ArrayList<Point> coords = drawPoints();
        drawLines(coords);
        if(canvas != null) {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawAxes() {
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.black));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DEFAULT_AXE_SIZE * scale);
        canvas.drawLine(width/2, 0, width/2, height, paint);
        canvas.drawLine(0, height/2, width, height/2, paint);
    }

    private ArrayList<Point> drawPoints() {
        ArrayList<Point> coords = new ArrayList<>();
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.point_color));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(DEFAULT_STROKE_SIZE);

        for (Point point : points) {
            int x = (int) (center_x + point.getX() * (DEFAULT_SEGMENT_SIZE * scale));
            int y = (int) (center_y - point.getY() * (DEFAULT_SEGMENT_SIZE * scale));
            coords.add(new Point(x, y));
            canvas.drawCircle(x, y, DEFAULT_POINT_SIZE * scale, paint);
        }
        return coords;
    }

    private void drawLines(ArrayList<Point> coords) {
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.line_color));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DEFAULT_LINE_SIZE * scale);
        for (int i = 0; i<coords.size()-1; i++)
            canvas.drawLine(coords.get(i).getX(), coords.get(i).getY(), coords.get(i+1).getX(), coords.get(i+1).getY(), paint);
    }

    @Override
    public void run() {
        draw();
    }
}
