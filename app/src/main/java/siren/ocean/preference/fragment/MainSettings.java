package siren.ocean.preference.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import siren.ocean.preference.R;

/**
 * 主页Fragment
 * Created by Siren on 2024/4/24.
 */
public class MainSettings extends PreferenceFragmentCompat {
    private static final String KEY_SYSTEM = "key_system";
    private static final String KEY_HIDE_ACTION_BAR = "key_hide_action_bar";
    private static final String KEY_FULL_SCREEN = "key_full_screen";
    private static final String KEY_THEME = "key_theme";
    private static final String KEY_TITLE = "key_title";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings_main, rootKey);
        initSystemPref();
        initHideActionBarPref();
        initHideNavigationBarPref();
        initThemePref();
    }

    @Override
    public void onResume() {
        super.onResume();
        initEditTitlePref();
    }

    private void initEditTitlePref() {
        EditTextPreference titlePref = findPreference(KEY_TITLE);
        if (titlePref != null) {
            String title = TextUtils.isEmpty(titlePref.getText()) ? getString(R.string.app_name) : titlePref.getText();
            titlePref.setSummary(title);
            setActionBarTitle(title);
            titlePref.setOnPreferenceChangeListener((preference, newValue) -> {
                String value = (String) newValue;
                titlePref.setSummary(value);
                setActionBarTitle(value);
                return true;
            });
        }
    }

    private void initSystemPref() {
        Preference versionPref = findPreference(KEY_SYSTEM);
        if (versionPref != null) {
            versionPref.setSummary(Build.MODEL);
        }
    }

    private void initHideActionBarPref() {
        SwitchPreference switchPref = findPreference(KEY_HIDE_ACTION_BAR);
        if (switchPref != null) {
            switchPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isHide = (boolean) newValue;
                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    if (isHide) {
                        actionBar.hide();
                    } else {
                        actionBar.show();
                    }
                }
                return true;
            });
        }
    }

    private void initHideNavigationBarPref() {
        CheckBoxPreference checkBoxPref = findPreference(KEY_FULL_SCREEN);
        if (checkBoxPref != null) {
            checkBoxPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (boolean) newValue;
                View decorView = getActivity().getWindow().getDecorView();
                if (isChecked) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                } else {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
                return true;
            });
        }
    }

    private void initThemePref() {
        ListPreference themeListPref = findPreference(KEY_THEME);
        if (themeListPref != null) {
            String currentValue = themeListPref.getValue();
            updateDescription(themeListPref, currentValue);
            setThemeStyle(currentValue);
            themeListPref.setOnPreferenceChangeListener((preference, newValue) -> {
                String value = (String) newValue;
                updateDescription(themeListPref, value);
                setThemeStyle(value);
                return true;
            });
        }
    }

    private void setThemeStyle(String theme) {
        if (getString(R.string.theme_dark).equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void updateDescription(ListPreference preference, String currentValue) {
        preference.setSummary(getDescription(currentValue));
    }

    private CharSequence getDescription(String currentValue) {
        final CharSequence[] entries = getResources().getStringArray(R.array.theme_entries);
        final CharSequence[] values = getResources().getStringArray(R.array.theme_values);
        return getDescription(currentValue, entries, values);
    }

    private CharSequence getDescription(String currentValue, CharSequence[] entries, CharSequence[] values) {
        if (TextUtils.isEmpty(currentValue)) {
            return null;
        }
        for (int i = 0; i < values.length; i++) {
            if (currentValue.equals(values[i].toString())) {
                return entries[i];
            }
        }
        return null;
    }

    private void setActionBarTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }
}