package com.example.final_project_backgammon;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySignal.init(this);
        MyImageUtils.initImageHelper(this);
    }
}
