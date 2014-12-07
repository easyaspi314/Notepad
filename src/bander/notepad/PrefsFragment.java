package bander.notepad;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.preference.PreferenceFragment;
import com.devin.notepad.R;

public class PrefsFragment extends PreferenceFragment implements
		Preference.OnPreferenceChangeListener {
	private static final String KEY_SORTORDER = "sortOrder";
	private static final String KEY_SORTASCENDING = "sortAscending";
	private static final String KEY_TEXTSIZE = "textSize";
	private static final String KEY_ABOUT = "about";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		ListPreference sortOrderPreference = (ListPreference) findPreference(KEY_SORTORDER);
		sortOrderPreference.setOnPreferenceChangeListener(this);
		setSortOrderSummary(sortOrderPreference);
		setSortAscendingEnabled(!sortOrderPreference.getValue().equals("0"));

		ListPreference textSizePreference = (ListPreference) findPreference(KEY_TEXTSIZE);
		textSizePreference.setOnPreferenceChangeListener(this);
		setTextSizeSummary(textSizePreference);

		Preference aboutPreference = (Preference) findPreference(KEY_ABOUT);
		aboutPreference
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {

						FragmentManager fm = getChildFragmentManager();
						DialogFragment dialog = new About();
						dialog.show(fm, "dialog");

						return false;
					}
				});

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO: Implement this method
		final String key = preference.getKey();

		if (KEY_SORTORDER.equals(key)) {
			ListPreference sortOrderPreference = (ListPreference) preference;
			sortOrderPreference.setValue((String) newValue);
			setSortOrderSummary(sortOrderPreference);
			setSortAscendingEnabled(!((String) newValue).equals("0"));
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
