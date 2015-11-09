package ru.edu.asu.minobrlabs.activities;

import android.os.Bundle;
import android.view.Menu;

import ru.edu.asu.minobrlabs.R;

public class StatsActivity extends AbstractActivity {
    private static final String TAG = StatsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        this.initWebView("file:///android_asset/web/stats.html", R.id.statsWebView);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_to_stats).setVisible(false);
        return true;
    }
}