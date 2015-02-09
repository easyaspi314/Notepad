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

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.devin.notepad.R;

import bander.provider.Note;

/** Secondary activity for Notepad, shows search results. */

		//	Can someone make this a SearchView on the Action Bar? 
public class NoteSearchResults extends ListActivity {
	public static final int SEARCH_ID = Menu.FIRST;

	/** The columns we are interested in from the database */
	private static final String[] PROJECTION = new String[] { 
		Note._ID, Note.TITLE
	};

	private TextView mEmptyText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Notepad.setThemeFromPreferences(this);
		setContentView(R.layout.list);

		mEmptyText = (TextView) findViewById(android.R.id.empty);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Intent intent = getIntent();
		handleSearchIntent(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleSearchIntent(intent);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Uri uri = ContentUris.withAppendedId(Note.CONTENT_URI, id);
		startActivity(new Intent(Intent.ACTION_EDIT, uri));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
				boolean theme=preferences.getBoolean("theme", true);
				if(theme){
		MenuItem item =
		menu.add(0, SEARCH_ID, 0, R.string.menu_search)
			.setIcon(R.drawable.ic_action_search_light);
			item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	} else {
		MenuItem item =
			menu.add(0, SEARCH_ID, 0, R.string.menu_search)
			.setIcon(R.drawable.ic_action_search_dark);
		item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case SEARCH_ID:
				onSearchRequested();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Handles callbacks from the system search service.
	 * @param intent Intent passed to this activity.
	 */
	private void handleSearchIntent(Intent intent) {
		String action = intent.getAction();
        switch (action) {
            case Intent.ACTION_SEARCH:
                String query = intent.getStringExtra(SearchManager.QUERY);
                showResults(query);
                break;
            case Intent.ACTION_VIEW:
                final Intent viewIntent = new Intent(Intent.ACTION_VIEW, intent.getData());
                startActivity(viewIntent);
                finish();
                break;
            default:
                finish();
                break;
        }
	}

	/** Searches the notes for a given search term and displays the results.
	 * @param query Search term to query with.
	 */
	private void showResults(String query) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		int sortOrder = Integer.valueOf(preferences.getString("sortOrder", "1"));
		String sorting = Note.SORT_ORDERS[sortOrder];

		Cursor cursor = getContentResolver().query(
			Note.CONTENT_URI, PROJECTION, Note.BODY + " LIKE ?", new String[] { "%" + query + "%" }, sorting
		);

		if (cursor != null) {
			int count = cursor.getCount();
			String countString = getResources().getQuantityString(
				R.plurals.search_results, count, new Object[] { count, query }
			);
			setTitle(countString);

			if (count == 0) {
				mEmptyText.setText(getString(R.string.search_noresults, new Object[] { query }));
			} else {
				Boolean largeListItems = preferences.getBoolean("listItemSize", true);

				SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
					(largeListItems) ? R.layout.row_large : R.layout.row_small, 
					cursor,
					new String[] { Note.TITLE }, new int[] { android.R.id.text1 }, 0
				);
				setListAdapter(adapter);
			}
		}
	}

}
