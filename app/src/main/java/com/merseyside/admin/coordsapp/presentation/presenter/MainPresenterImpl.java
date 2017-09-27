package com.merseyside.admin.coordsapp.presentation.presenter;

import com.merseyside.admin.coordsapp.domain.interactor.CoordsInteractor;
import com.merseyside.admin.coordsapp.domain.interactor.CoordsInteractorImpl;
import com.merseyside.admin.coordsapp.presentation.view.MainView;
import com.merseyside.admin.coordsapp.Point;

import java.util.ArrayList;

/**
 * Created by ivan_ on 22.08.2017.
 */

public class MainPresenterImpl implements CoordsInteractor, CoordsInteractor.OnFinishedResult {

    private MainView mainView;
    private CoordsInteractor coordsInteractor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        coordsInteractor = new CoordsInteractorImpl(this);
    }

    public void goButtonClicked(int count) {
        coordsInteractor.getCoords(count);
    }

    @Override
    public void onSuccess(ArrayList<Point> points) {
        mainView.goToCoordinateActivity(points);
    }

    @Override
    public void onError(String message) {
        mainView.errorMessage(message);
    }

    @Override
    public void getCoords(int count) {

    }
}
