package com.merseyside.admin.coordsapp.presentation.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.merseyside.admin.coordsapp.Constants;
import com.merseyside.admin.coordsapp.Point;
import com.merseyside.admin.coordsapp.R;
import com.merseyside.admin.coordsapp.presentation.presenter.MainPresenterImpl;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private Button go_button;
    private EditText count_et;
    private MainPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go_button = (Button) findViewById(R.id.go_button);
        go_button.setOnClickListener(this);

        count_et = (EditText) findViewById(R.id.count_et);

        this.presenter = new MainPresenterImpl(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.go_button):
                try {
                    presenter.goButtonClicked(Integer.valueOf(count_et.getText().toString()));
                } catch (NumberFormatException e) {
                    errorMessage(getString(R.string.wrong_parameters));
                }
                break;
        }
    }

    @Override
    public void errorMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void goToCoordinateActivity(ArrayList<Point> points) {
        Intent intent = new Intent(this, SurfaceActivity.class);
        intent.putExtra(Constants.POINTS_KEY, points);
        startActivity(intent);
    }
}
