/*
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.devin.notepad.R;

/**
 * A class that acts like an {@link android.app.Application} but it is actually an Activity. This is
 * the core of Notepad. What this class does:<p>
 * <ul>
 * <li>It switches between the two NoteList activities due to the AppCompat conflict.</li>
 * <br>
 * <li>It changes the theme {@link Notepad#setThemeFromPreferences(android.content.Context)}</li>
 * <br>
 * <li>It sets the color of the {@link android.support.v7.widget.Toolbar}</li>
 * </ul>
 */
public class Notepad extends FragmentActivity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean passEnabled = mSettings.getBoolean("passEnabled", true);
        String password = mSettings.getString("password", "");
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            if (passEnabled && !password.equals("")) {
                Intent intent = new Intent(this, Password.class);
                startActivity(intent);
                finish();
            }
        }

        Log.i("Hello!", "Welcome to Notepad!");

        String theme = mSettings.getString("themeType", "0");
        if (!theme.equals("0")) {
            Intent intent = new Intent(this, NoteList.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, NoteListAppCompat.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * Sets the normal theme.
     * @param context
     */
    public static void setThemeFromPreferences(Context context) {
        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String color = mSettings.getString("colorScheme", "Dark");
        String theme = mSettings.getString("themeType", "0");

        switch (Integer.parseInt(theme)) {

            // Lollipop theme
            case 0:
                // Do nothing
                break;
            // KitKat-styled theme
            case 1:
                switch (color) {
                    case "Light":
                        context.setTheme(R.style.Theme_KitKat_Light);

                        break;
                    case "Mixed":
                        context.setTheme(R.style.Theme_KitKat_Light_DarkActionBar);

                        break;
                    default:
                        context.setTheme(R.style.Theme_KitKat_Dark);
                        break;
                }

                break;
            // Default Holo theme
            case 2:
                switch (color) {
                    case "Light":
                        context.setTheme(android.R.style.Theme_Holo_Light);
                        break;
                    case "Mixed":
                        context.setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
                        break;
                    default:
                        context.setTheme(android.R.style.Theme_Holo);
                        break;
                }
                break;
            // Device Default
            case 3:
                switch (color) {
                    case "Light":
                        context.setTheme(android.R.style.Theme_DeviceDefault_Light);
                        break;
                    case "Mixed":
                        context.setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                        break;
                    default:
                        context.setTheme(android.R.style.Theme_DeviceDefault);
                        break;
                }
                break;
            // Gingerbread
            case 4:
                switch (color) {
                    case "Light":
                        context.setTheme(android.R.style.Theme_Light);
                        break;
                    case "Mixed":
                        context.setTheme(R.style.Theme_WithActionBar);
                        break;
                    default:
                        context.setTheme(R.style.Theme);
                        break;
                }
                break;
            // fallback for error.
            default:
                switch (color) {
                    case "Light":
                        context.setTheme(R.style.Theme_KitKat_Light);
                        break;
                    case "Mixed":
                        context.setTheme(R.style.Theme_KitKat_Light_DarkActionBar);
                        break;
                    default:
                        context.setTheme(R.style.Theme_KitKat_Dark);
                        break;
                }
                break;
        }
    }

    /**
     * Sets the theme for the dialogs.
     * @param context
     */
    public static void setDialogThemeFromPreferences(Context context) {
        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String color = mSettings.getString("colorScheme", "Dark");
        String theme = mSettings.getString("themeType", "0");


        switch (Integer.parseInt(theme)) {
            // Holo theme
            case 1:
            case 2:
                if (color.equals("Light") | color.equals("Mixed")) {
                    context.setTheme(android.R.style.Theme_Holo_Light_Dialog);
                } else {
                    context.setTheme(android.R.style.Theme_Holo_Dialog);
                }

                break;
            // Device Default
            case 3:
                if (color.equals("Light") | color.equals("Mixed")) {
                    context.setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
                } else {
                    context.setTheme(android.R.style.Theme_DeviceDefault_Dialog);
                }
                break;
            // Gingerbread
            case 4:
                context.setTheme(android.R.style.Theme_Dialog);

                break;
            // fallback for error.
            default:
                if (color.equals("Light") | color.equals("Mixed")) {
                    context.setTheme(android.R.style.Theme_Holo_Light_Dialog);
                } else {
                    context.setTheme(android.R.style.Theme_Holo_Dialog);
                }
                break;
        }
    }

    /**
     * Sets the theme for AppCompat. There is really no dark theme at the moment, it is mainly for
     * the Toolbar Color Chooser.
     * @param activity
     * @param classType
     */
    public static void setAppCompatThemeFromPreferences(ActionBarActivity activity,
                                                        String classType) {
        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(activity);
        if (mSettings.getString("themeType", "0").equals("0")) {
            final int abc = mSettings.getInt("actionBarColor", 0);

            /**
             * In case you are wondering, the random numbers are the 500 colors that show up with
             * black text from here:
             * {@link http://www.google.com/design/spec/style/color.html}
             * The black text means that I have to use a light theme.
             */
            if (abc >= 11 && abc <= 15 || abc == 18) {
                mSettings.edit().putBoolean("darkAppCompatTheme", false).apply();
            } else {
                mSettings.edit().putBoolean("darkAppCompatTheme", true).apply();
            }
        } else {
            /**
             * Somehow, we ended up with the wrong theme, so let's launch the right activity.
             */
            switch (classType) {
                case "Prefs": {
                    Intent intent = new Intent();
                    intent.setClass(activity, SetPreferenceActivity.class);
                    activity.startActivity(intent);
                    break;
                }
                case "Edit": {
                    Intent intent = new Intent();
                    intent.setClass(activity, NoteEdit.class);
                    activity.startActivity(intent);
                    break;
                }
                case "NoteList": {
                    Intent intent = new Intent();
                    intent.setClass(activity, NoteList.class);
                    activity.startActivity(intent);
                    break;
                }
                default: {
                    Intent intent = new Intent();
                    intent.setClass(activity, Notepad.class);
                    activity.startActivity(intent);
                    break;
                }
            }
        }
    }

    /**
     * Sets the color of the {@link android.support.v7.widget.Toolbar}
     * @param activity
     */
    public static void setToolbarColor(final ActionBarActivity activity) {
        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(activity);

        /**
         * The lighter toolbar color {@link com.devin.notepad.R.array.color500}
         */
        TypedArray m500ta = activity.getResources().obtainTypedArray(R.array.color500);
        final int[] mToolbarColors = new int[m500ta.length()];
        for (int i = 0; i < m500ta.length(); i++) {
            mToolbarColors[i] = m500ta.getColor(i, 0);
        }
        m500ta.recycle();

        /**
         * The darker status bar color. {@link com.devin.notepad.R.array.color700}
         */
        TypedArray m700ta = activity.getResources().obtainTypedArray(R.array.color700);
        int[] statusBarColors = new int[m700ta.length()];
        for (int i = 0; i < m700ta.length(); i++) {
            statusBarColors[i] = m700ta.getColor(i, 0);
        }
        m700ta.recycle();


        final int abc = mSettings.getInt("actionBarColor", 0);

        Toolbar mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);

        if (mToolbar != null)
            mToolbar.setBackgroundColor(mToolbarColors[abc]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(statusBarColors[abc]);
        }
    }

}
