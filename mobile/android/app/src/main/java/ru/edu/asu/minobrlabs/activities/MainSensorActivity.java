package ru.edu.asu.minobrlabs.activities;

import android.os.Bundle;

import ru.edu.asu.minobrlabs.R;

public class MainSensorActivity extends AbstractSensorActivity {
    private static final String TAG = MainSensorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initWebView("file:///android_asset/web/index.html", R.id.mainWebView);
    }
}
