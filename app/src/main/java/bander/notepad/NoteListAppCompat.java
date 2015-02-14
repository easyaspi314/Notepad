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

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.devin.notepad.R;

import java.util.ArrayList;

import bander.provider.Note;

/**
 * Main activity for Notepad, shows a list of notes. Light theme.
 */
public class NoteListAppCompat extends ActionBarActivity implements ListView.OnItemClickListener {
    public static final int INSERT_ID = Menu.FIRST;
    public static final int SEARCH_ID = Menu.FIRST + 1;
    public static final int PREFS_ID = Menu.FIRST + 2;

    public static final int DELETE_ID = Menu.FIRST + 3;
    public static final int SEND_ID = Menu.FIRST + 4;

    private static final String[] PROJECTION = new String[]{Note._ID,
            Note.TITLE};

    private static final int COLUMN_INDEX_ID = 0;
    private static final int COLUMN_INDEX_TITLE = 1;

    private static boolean GIVEN_HINT = false;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Notepad.setAppCompatThemeFromPreferences(this, "NoteList");
        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(this);
        setContentView(R.layout.list_appcompat);

        ViewStub stub = (ViewStub) findViewById(R.id.toolbarWrapper);
        if (mSettings.getBoolean("darkAppCompatTheme", false))
            stub.setLayoutResource(R.layout.toolbar_dark);
        else
            stub.setLayoutResource(R.layout.toolbar_light);
        stub.inflate();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Notepad.setToolbarColor(this);

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(Note.CONTENT_URI);
        }

        registerForContextMenu(getListView());
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean largeListItems = preferences.getBoolean("listItemSize", true);

        int sortOrder = Integer
                .valueOf(preferences.getString("sortOrder", "1"));
        boolean sortAscending = preferences.getBoolean("sortAscending", true);
        String sorting = Note.SORT_ORDERS[sortOrder]
                + ((sortAscending ? " ASC" : " DESC"));
        Cursor cursor = getContentResolver().query(getIntent().getData(),
                PROJECTION, null, null, sorting);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                (largeListItems) ? R.layout.row_large_appcompat : R.layout.row_small_appcompat,
                cursor, new String[]{Note.TITLE},
                new int[]{android.R.id.text1}, 0);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        if ((!GIVEN_HINT) && (adapter.getCount() == 1)) {
            Toast.makeText(this, R.string.hint_longpress, Toast.LENGTH_LONG)
                    .show();
            GIVEN_HINT = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        {
            getMenuInflater().inflate(R.menu.list_menu_appcompat, menu);

            return super.onCreateOptionsMenu(menu);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        ArrayList<Drawable> mTempList = new ArrayList<>();
        Log.i("Menu Size", menu.size() + "");
        for (int i = 0; i < menu.size(); i++) {
            mTempList.add(menu.getItem(i).getIcon());
        }
        final ArrayList<Drawable> mList;
        if (!PreferenceManager.getDefaultSharedPreferences(NoteListAppCompat.this)
                .getBoolean("darkAppCompatTheme", false))
            mList = IconTintFactory
                    .setDarkMaterialColor(mTempList, NoteListAppCompat.this);
        else
            mList = IconTintFactory
                    .setLightMaterialColor(mTempList, NoteListAppCompat.this);
        for (int i = 0; i < menu.size(); i++) {
            if (mList.get(i) != null) {
                menu.getItem(i).setIcon(mList.get(i));
            }

        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.INSERT_ID:
                startActivity(new Intent(Intent.ACTION_INSERT, getIntent()
                        .getData()).setClassName(getApplicationContext().getPackageName(), "bander.notepad.NoteEditAppCompat"));
                return true;
            case R.id.SEARCH_ID:
                onSearchRequested();
                return true;
            case R.id.PREFS_ID:

            {
                Intent prefsActivity = new Intent(this,
                        PrefsActivityAppCompat.class);
                startActivity(prefsActivity);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            return;
        }

        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
        if (cursor == null) {
            return;
        }

        menu.setHeaderTitle(cursor.getString(COLUMN_INDEX_TITLE));

        Uri uri = ContentUris.withAppendedId(getIntent().getData(),
                cursor.getInt(COLUMN_INDEX_ID));

        Intent[] specifics = new Intent[1];
        specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
        MenuItem[] items = new MenuItem[1];

        Intent intent = new Intent(null, uri);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, null, specifics,
                intent, 0, items);

        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        // TODO: When you click on this, it crashes. I commented it out for now.
        // menu.add(0, SEND_ID, 0, R.string.menu_send);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            return false;
        }
        switch (item.getItemId()) {
            case DELETE_ID:
                deleteNote(this, info.id);
                return true;
            case SEND_ID:
                Uri uri = ContentUris.withAppendedId(Note.CONTENT_URI, info.id);
                Cursor cursor = getContentResolver().query(uri,
                        new String[]{Note._ID, Note.TITLE, Note.BODY}, null,
                        null, null);
                Note note = Note.fromCursor(cursor);
                cursor.close();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, note.getBody());
                startActivity(Intent.createChooser(intent,
                        getString(R.string.menu_send)));
                return true;
        }
        return false;
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);

        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action)
                || Intent.ACTION_GET_CONTENT.equals(action)) {
            setResult(
                    RESULT_OK,
                    new Intent().setData(uri).setClassName(getApplicationContext().getPackageName(),
                            "bander.notepad.NoteEditAppCompat"));
        } else {
            startActivity(new Intent(Intent.ACTION_EDIT, uri).setClassName(
                    getApplicationContext().getPackageName(), "bander.notepad.NoteEditAppCompat"));
        }
    }

    /**
     * Delete a note, confirm when preferred.
     *
     * @param context Context to use.
     * @param id      ID of the note to delete.
     */
    private void deleteNote(Context context, long id) {
        final long noteId = id;
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean deleteConfirmation = preferences.getBoolean(
                "deleteConfirmation", true);
        if (deleteConfirmation) {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.dialog_delete)
                    .setMessage(R.string.delete_confirmation)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                // OnClickListener
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Uri noteUri = ContentUris.withAppendedId(
                                            Note.CONTENT_URI, noteId);
                                    getContentResolver().delete(noteUri, null,
                                            null);
                                }
                            }).setNegativeButton(android.R.string.cancel, null)
                    .create();
            alertDialog.show();
        } else {
            Uri noteUri = ContentUris.withAppendedId(Note.CONTENT_URI, noteId);
            getContentResolver().delete(noteUri, null, null);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar.isOverflowMenuShowing()) {
                toolbar.dismissPopupMenus();
            } else {
                toolbar.showOverflowMenu();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private ListView getListView() {
        return ((ListView) findViewById(android.R.id.list));
    }

    private ListAdapter getListAdapter() {
        return getListView().getAdapter();
    }

    private void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    @Override
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        View emptyView = findViewById(android.R.id.empty);
        if (emptyView != null)
            getListView().setEmptyView(emptyView);
    }
}
