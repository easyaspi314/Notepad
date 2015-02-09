/*
 * Copyright (C) 2015 Devin Hussey
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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.devin.notepad.R;

/**
 * A {@link android.widget.BaseAdapter} for {@link bander.notepad.ColorChooserDialog}
 * that shows a GridView of colorful circles to choose.
 */
public class ColorGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private int[] m500Colors;

    public ColorGridViewAdapter(Context c) {
        mContext = c;
        TypedArray m500ta = mContext.getResources().obtainTypedArray(R.array.color500);
        m500Colors = new int[m500ta.length()];
        for (int i = 0; i < m500ta.length(); i++) {
            m500Colors[i] = m500ta.getColor(i, 0);
        }
        m500ta.recycle();
    }

    public int getCount() {
        return m500Colors.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            convertView = inflater.inflate(R.layout.color_picker, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.circle);
            holder.mDrawable = mContext.getResources().getDrawable(R.drawable.circle);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int red = Color.red(m500Colors[position]);
        int green = Color.green(m500Colors[position]);
        int blue = Color.blue(m500Colors[position]);
        int rgb = Color.rgb (red, green, blue);
        Drawable mDrawableToColor  = holder.mDrawable;
        /**
         * Tint the icons.
         */
        mDrawableToColor.setColorFilter(new
                PorterDuffColorFilter(rgb, PorterDuff.Mode.SRC_ATOP));
        holder.imageView.setImageDrawable(mDrawableToColor);
        return convertView;
    }

    /**
     * ViewHolder.
     */
    class ViewHolder {
        Drawable mDrawable;
        ImageView imageView;
    }
}
