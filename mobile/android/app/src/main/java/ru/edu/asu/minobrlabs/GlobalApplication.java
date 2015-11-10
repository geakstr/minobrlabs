package ru.edu.asu.minobrlabs;

import android.app.Application;

public class GlobalApplication extends Application {
    private static GlobalApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static GlobalApplication getInstance() {
        return singleton;
    }
}
