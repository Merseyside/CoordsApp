package com.merseyside.admin.coordsapp;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class Application extends android.app.Application {
    private static AppComponent component;
    public static AppComponent getComponent(){
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
    }

    private AppComponent buildComponent(){
        getScreenDimensions(this);
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Constants.swapDensity();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Constants.swapDensity();
        }
    }

    private void getScreenDimensions(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        android.graphics.Point size = new Point();
        display.getSize(size);
        Constants.width = size.x;
        Constants.height = size.y;
    }
}
