package com.merseyside.admin.coordsapp.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.merseyside.admin.coordsapp.Constants;
import com.merseyside.admin.coordsapp.MyUtils;
import com.merseyside.admin.coordsapp.Point;
import com.merseyside.admin.coordsapp.R;

import java.util.ArrayList;

public class SurfaceActivity extends AppCompatActivity  {

    private MySurfaceView MSV;
    public ImageButton plus, minus;
    private ArrayList<Point> points;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();

        points = (ArrayList<Point>) intent.getSerializableExtra(Constants.POINTS_KEY);

        MSV = new MySurfaceView(this, points);

        FrameLayout game = new FrameLayout(this);
        LinearLayout buttonsWidget = new LinearLayout (this);

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linearParams.setMargins(0, 0, 0, 0);

        plus = new ImageButton(this);
        plus.setImageResource(R.drawable.plus);
        plus.setVisibility(View.VISIBLE);
        plus.setMaxHeight(MyUtils.dpsInPixels(this, R.dimen.imagebutton_size));
        plus.setMaxWidth(MyUtils.dpsInPixels(this, R.dimen.imagebutton_size));
        plus.setAdjustViewBounds(true);
        plus.setScaleType(ImageView.ScaleType.FIT_CENTER);
        plus.setBackground(null);
        plus.setOnClickListener(view -> MSV.zoomOn());

        minus = new ImageButton(this);
        minus.setImageResource(R.drawable.minus);
        minus.setVisibility(View.VISIBLE);
        minus.setMaxHeight(MyUtils.dpsInPixels(this, R.dimen.imagebutton_size));
        minus.setMaxWidth(MyUtils.dpsInPixels(this, R.dimen.imagebutton_size));
        minus.setAdjustViewBounds(true);
        minus.setScaleType(ImageView.ScaleType.FIT_CENTER);
        minus.setBackground(null);
        minus.setOnClickListener(view -> MSV.zoomOut());

        buttonsWidget.addView(plus, linearParams);
        buttonsWidget.addView(minus, linearParams);
        buttonsWidget.setGravity(Gravity.CENTER|Gravity.END);
        buttonsWidget.setOrientation(LinearLayout.VERTICAL);

        game.addView(MSV);
        game.addView(buttonsWidget);

        setContentView(game);

        MSV.setOnTouchListener(MSV);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
