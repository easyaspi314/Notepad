/*
 * Copyright (C) 2011 BanderLabs
 * Copyright (C) 2014-2015 Devin Hussey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bander.notepad;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;

import com.afollestad.materialdialogs.ThemeSingleton;
import com.devin.notepad.R;

import de.mrapp.android.preference.ListPreference;

public class AppCompatPrefsFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_SORTORDER = "sortOrder";
    private static final String KEY_SORTASCENDING = "sortAscending";
    private static final String KEY_TEXTSIZE = "textSize";
    private static final String KEY_ABOUT = "about";
    private static final String KEY_THEME_TYPE = "themeType";
    private static final String KEY_COLOR = "actionBarColor";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addPreferencesFromResource(R.xml.preferences_appcompat);
        ListPreference sortOrderPreference = (ListPreference) findPreference(KEY_SORTORDER);
        sortOrderPreference.setOnPreferenceChangeListener(this);
        setSortOrderSummary(sortOrderPreference);
        setSortAscendingEnabled(!sortOrderPreference.getValue().equals("0"));

        ListPreference textSizePreference = (ListPreference) findPreference(KEY_TEXTSIZE);
        textSizePreference.setOnPreferenceChangeListener(this);
        setTextSizeSummary(textSizePreference);

        Preference actionBarPref = findPreference(KEY_COLOR);
        actionBarPref.setOnPreferenceChangeListener(this);

        Preference aboutPreference = findPreference(KEY_ABOUT);
        aboutPreference
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        About.newInstance().show(ft, "dialog");
                        return false;
                    }
                });
        final Preference actionBarColor = findPreference("actionBarColor");
        if (actionBarColor != null) {
            actionBarColor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showCustomColorChooser();
                    return false;
                }
            });
        }
    }

    static int selectedColorIndex = -1;

    private void showCustomColorChooser() {
        new ColorChooserDialog().show(getActivity(), selectedColorIndex, new ColorChooserDialog.Callback() {
            @Override
            public void onColorSelection(int index, int color, int darker) {
                selectedColorIndex = index;
                ActionBarActivity aba = (ActionBarActivity) getActivity();
                aba.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                ThemeSingleton.get().positiveColor = color;
                ThemeSingleton.get().neutralColor = color;
                ThemeSingleton.get().negativeColor = color;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    aba.getWindow().setStatusBarColor(darker);
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO: Implement this method
        final String key = preference.getKey();

        if (KEY_COLOR.equals(key)|| KEY_THEME_TYPE.equals(key)) {
            ((PreferenceActivityAppCompat)getActivity()).updateToolbar(((PreferenceActivityAppCompat)getActivity()));
            return false;
        }

        if (KEY_SORTORDER.equals(key)) {
            ListPreference sortOrderPreference = (ListPreference) preference;
            sortOrderPreference.setValue((String) newValue);
            setSortOrderSummary(sortOrderPreference);
            setSortAscendingEnabled(!newValue.equals("0"));
            return false;
        }
        if (KEY_TEXTSIZE.equals(key)) {
            ListPreference textSizePreference = (ListPreference) preference;
            textSizePreference.setValue((String) newValue);
            setTextSizeSummary(textSizePreference);
            return false;
        }
        return true;
    }

    private void setSortOrderSummary(ListPreference preference) {
        preference.setSummary(getString(R.string.pref_sortOrderSummary,
                preference.getEntry()));
    }

    private void setTextSizeSummary(ListPreference preference) {
        preference.setSummary(getString(R.string.pref_textSizeSummary,
                preference.getEntry()));
    }

    private void setSortAscendingEnabled(boolean enabled) {
        CheckBoxPreference sortAscendingPreference = (CheckBoxPreference) findPreference(KEY_SORTASCENDING);
        sortAscendingPreference.setEnabled(enabled);
        if (!enabled)
            sortAscendingPreference.setChecked(true);
    }
}
