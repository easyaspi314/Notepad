package bander.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.devin.notepad.R;

import de.mrapp.android.dialog.MaterialDialogBuilder;

/**
 * A dialog that lets you choose the color for the {@link android.support.v7.widget.Toolbar}
 *
 * @author Aidan Follestad (afollestad)
 * @author Devin Hussey
 */
public class ColorChooserDialog extends DialogFragment implements GridView.OnItemClickListener {

    private Callback mCallback;

    /**
     * Put the color position to the prefs.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences.Editor mEditor = PreferenceManager
                .getDefaultSharedPreferences(getActivity()).edit();
        mEditor.putInt("actionBarColor", position);
        mEditor.apply();
        dismiss();
        getActivity().recreate();
    }


    public static interface Callback {
        void onColorSelection(int index, int color, int darker);
    }

    /**
     * Static initializer
     */
    public ColorChooserDialog() {
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /**
         * Get the current position to try and select the current color. Only scrolls to that area
         * for now.
         */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int current = prefs.getInt("actionBarColor", 0);

        /**
         * Create the Material Dialog {@link de.mrapp.android.dialog.MaterialDialogBuilder}
         */
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.color_picker_gridview, null);
        AlertDialog dialog = new MaterialDialogBuilder(getActivity())
                .setTitle(R.string.pref_action_bar_color)
                .setView(textEntryView)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        final GridView gridView = (GridView) textEntryView.findViewById(R.id.grid);

        gridView.setAdapter(new ColorGridViewAdapter(getActivity()));
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setItemChecked(current, true);
        gridView.setOnItemClickListener(this);

        return dialog;
    }

    public void show(FragmentActivity context, int preselect, Callback callback) {
        mCallback = callback;
        Bundle args = new Bundle();
        args.putInt("preselect", preselect);
        setArguments(args);
        show(context.getSupportFragmentManager(), "COLOR_SELECTOR");
    }
}
