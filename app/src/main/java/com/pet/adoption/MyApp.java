package com.pet.adoption;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApp extends Application {

    public static MyApp instance;
    private Bitmap bitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


}
