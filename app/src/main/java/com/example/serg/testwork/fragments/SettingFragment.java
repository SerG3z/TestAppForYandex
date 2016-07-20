package com.example.serg.testwork.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.serg.testwork.R;

/**
 * Created by user on 19.07.16.
 */

public class SettingFragment extends PreferenceFragmentCompat {

    public static SettingFragment newInstance() {

        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.list_settings);
    }
}
