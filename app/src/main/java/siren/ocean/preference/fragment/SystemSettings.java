package siren.ocean.preference.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.Preference;

import siren.ocean.preference.R;

/**
 * 系统信息
 * Created by Siren on 2024/1/24.
 */
public class SystemSettings extends PreferenceFragment {

    private final static String KEY_MODEL = "key_model";
    private final static String KEY_PRODUCT = "key_product";
    private final static String KEY_DEVICE = "key_device";
    private final static String KEY_BOARD = "key_board";
    private final static String KEY_MANUFACTURER = "key_manufacturer";
    private final static String KEY_BOOTLOADER = "key_bootloader";
    private final static String KEY_RADIO = "key_radio";
    private final static String KEY_ANDROID_VERSION = "key_android_version";
    private final static String KEY_BUILD = "key_build";
    private final static String KEY_API = "key_api";

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.system);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings_system, rootKey);
        updateSummary(KEY_MODEL, Build.MODEL);
        updateSummary(KEY_PRODUCT, Build.PRODUCT);
        updateSummary(KEY_DEVICE, Build.DEVICE);
        updateSummary(KEY_BOARD, Build.BOARD);
        updateSummary(KEY_MANUFACTURER, Build.MANUFACTURER);
        updateSummary(KEY_BOOTLOADER, Build.BOOTLOADER);
        updateSummary(KEY_RADIO, Build.getRadioVersion());
        updateSummary(KEY_ANDROID_VERSION, Build.VERSION.RELEASE);
        updateSummary(KEY_API, String.valueOf(Build.VERSION.SDK_INT));
        updateSummary(KEY_BUILD, Build.FINGERPRINT);
    }

    private void updateSummary(String key, String value) {
        Preference firmwareVersionPref = findPreference(key);
        if (firmwareVersionPref != null) {
            firmwareVersionPref.setSummary(value);
        }
    }
}