package siren.ocean.preference.activity;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import siren.ocean.preference.R;
import siren.ocean.preference.fragment.MainSettings;

/**
 * 主页
 * Created by Siren on 2024/4/24.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.view_container, new MainSettings())
                .commitAllowingStateLoss();
        initActionBar();
    }

    protected void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
