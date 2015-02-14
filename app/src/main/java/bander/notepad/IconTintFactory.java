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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import com.devin.notepad.R;

import java.util.ArrayList;

public class IconTintFactory {

    public static Drawable setDarkMaterialColor(int resId, Context context) {
        int rgb = context.getResources().getColor(R.color.abc_secondary_text_material_light);
        Drawable mDrawableToColor = context.getResources().getDrawable(resId);
        mDrawableToColor.setColorFilter(new PorterDuffColorFilter(rgb, PorterDuff.Mode.SRC_IN));

        return mDrawableToColor;
    }

    public static ArrayList<Drawable>
    setDarkMaterialColor(ArrayList<Drawable> mList, Context context) {
        ArrayList<Drawable> mTempList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            int rgb = context.getResources().getColor(R.color.abc_secondary_text_material_light);
            Drawable mDrawableToColor = mList.get(i);
            if (mDrawableToColor != null)
                mDrawableToColor.setColorFilter(new PorterDuffColorFilter(rgb, PorterDuff.Mode.SRC_IN));
            mTempList.add(i, mDrawableToColor);
        }

        return mTempList;
    }

    public static Drawable setLightMaterialColor(int resId, Context context) {
        int rgb = context.getResources().getColor(R.color.abc_secondary_text_material_dark);
        Drawable mDrawableToColor = context.getResources().getDrawable(resId);
        mDrawableToColor.setColorFilter(new PorterDuffColorFilter(rgb, PorterDuff.Mode.SRC_IN));

        return mDrawableToColor;
    }

    public static ArrayList<Drawable>
    setLightMaterialColor(ArrayList<Drawable> mList, Context context) {
        ArrayList<Drawable> mTempList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            int rgb = context.getResources().getColor(R.color.abc_secondary_text_material_dark);
            Drawable mDrawableToColor = mList.get(i);
            if (mDrawableToColor != null)
                mDrawableToColor.setColorFilter(new PorterDuffColorFilter(rgb, PorterDuff.Mode.SRC_IN));
            mTempList.add(i, mDrawableToColor);
        }

        return mTempList;
    }

    public static Drawable setDarkHoloColor(int resId, Context context) {
        int rgb = context.getResources().getColor(R.color.color_action_icons_tint_holo);
        Drawable mDrawableToColor = context.getResources().getDrawable(resId);
        mDrawableToColor.setColorFilter(new PorterDuffColorFilter(rgb, PorterDuff.Mode.SRC_IN));

        return mDrawableToColor;
    }

}
