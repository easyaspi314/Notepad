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
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.preference.PreferenceFragment;

import com.devin.notepad.R;

public class PrefsFragment extends PreferenceFragment implements
		Preference.OnPreferenceChangeListener {

	private static final String KEY_SORTORDER = "sortOrder";
	private static final String KEY_SORTASCENDING = "sortAscending";
	private static final String KEY_TEXTSIZE = "textSize";
	private static final String KEY_ABOUT = "about";
    private static final String KEY_THEME_TYPE = "themeType";
    private static final String KEY_COLOR = "colorScheme";


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		addPreferencesFromResource(R.xml.preferences);

		ListPreference sortOrderPreference = (ListPreference) findPreference(KEY_SORTORDER);
		sortOrderPreference.setOnPreferenceChangeListener(this);
		setSortOrderSummary(sortOrderPreference);
		setSortAscendingEnabled(!sortOrderPreference.getValue().equals("0"));

		ListPreference textSizePreference = (ListPreference) findPreference(KEY_TEXTSIZE);
		textSizePreference.setOnPreferenceChangeListener(this);
		setTextSizeSummary(textSizePreference);

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

    }



    @Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO: Implement this method
		final String key = preference.getKey();

        if (KEY_COLOR.equals(key)|| KEY_THEME_TYPE.equals(key)) {
            getActivity().recreate();
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
