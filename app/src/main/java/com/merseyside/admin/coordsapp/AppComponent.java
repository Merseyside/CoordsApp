package com.merseyside.admin.coordsapp;

import com.merseyside.admin.coordsapp.Coords.DrawThread;
import com.merseyside.admin.coordsapp.Main.CoordsInteractorImpl;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(CoordsInteractorImpl coordsInteractor);
    void inject(DrawThread gameThread);
}
