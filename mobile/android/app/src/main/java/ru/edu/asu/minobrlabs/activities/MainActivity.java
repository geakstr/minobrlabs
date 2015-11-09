package ru.edu.asu.minobrlabs.activities;

import android.os.Bundle;

import ru.edu.asu.minobrlabs.R;

public class MainActivity extends AbstractActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initWebView("file:///android_asset/web/index.html", R.id.mainWebView);
    }
}
