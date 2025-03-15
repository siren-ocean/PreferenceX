package siren.ocean.preference.fragment;

import static android.support.v4.content.ContextCompat.getSystemService;

import android.app.ActionBar;
import android.app.UiModeManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.text.TextUtils;
import android.view.View;

import siren.ocean.preference.R;

/**
 * 主页Fragment
 * Created by Siren on 2024/4/24.
 */
public class MainSettings extends PreferenceFragment {
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
        EditTextPreference titlePref = (EditTextPreference) findPreference(KEY_TITLE);
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
            versionPref.setOnPreferenceClickListener(preference -> {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_container, new SystemSettings())
                        .commitAllowingStateLoss();
                return false;
            });
        }
    }

    /**
     * 初始化 ActionBar SwitchPreference
     */
    private void initHideActionBarPref() {
        SwitchPreference switchPref = (SwitchPreference) findPreference(KEY_HIDE_ACTION_BAR);
        if (switchPref != null) {
            hideActionBar(switchPref.isChecked());
            switchPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isHide = (boolean) newValue;
                hideActionBar(isHide);
                return true;
            });
        }
    }

    private void hideActionBar(boolean isHide) {
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            if (isHide) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }

    /**
     * 初始化 NavigationBar CheckBoxPreference
     */
    private void initHideNavigationBarPref() {
        CheckBoxPreference checkBoxPref = (CheckBoxPreference) findPreference(KEY_FULL_SCREEN);
        if (checkBoxPref != null) {
            hideNavigationBar(checkBoxPref.isChecked());
            checkBoxPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (boolean) newValue;
                hideNavigationBar(isChecked);
                return true;
            });
        }
    }

    private void hideNavigationBar(boolean isChecked) {
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
    }

    /**
     * 初始化主题选择
     */
    private void initThemePref() {
        ListPreference themeListPref = (ListPreference) findPreference(KEY_THEME);
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
        enableNightMode(getString(R.string.theme_dark).equals(theme));
    }

    public void enableNightMode(boolean enable) {
        UiModeManager uiModeManager = (UiModeManager) getActivity().getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager != null) {
            uiModeManager.setNightMode(enable ? UiModeManager.MODE_NIGHT_YES : UiModeManager.MODE_NIGHT_NO);
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
        getActivity().getActionBar().setTitle(title);
    }
}