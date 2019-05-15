package com.example.whitetiles.helper;

import android.content.Context;
import android.graphics.Typeface;

import com.example.whitetiles.R;

public class FontManager {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fa_regular.ttf",
            FONTAWESOME_SOLID = ROOT + "fa_solid.ttf",
            FONTAWESOME_BRANDS = ROOT + "fa_brands.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}