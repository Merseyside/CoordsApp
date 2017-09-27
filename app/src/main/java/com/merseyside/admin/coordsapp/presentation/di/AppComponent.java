package com.merseyside.admin.coordsapp.presentation.di;

import com.merseyside.admin.coordsapp.DrawThread;
import com.merseyside.admin.coordsapp.domain.interactor.CoordsInteractorImpl;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(CoordsInteractorImpl coordsInteractor);
    void inject(DrawThread gameThread);
}
