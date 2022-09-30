package com.am.induster.SupportingFiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class HeadingCustomTextView extends androidx.appcompat.widget.AppCompatTextView {


    public HeadingCustomTextView(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Typeface_Lifehack.otf");
        this.setTypeface(face);
    }

    public HeadingCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Typeface_Lifehack.otf");
        this.setTypeface(face);
    }

    public HeadingCustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Typeface_Lifehack.otf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}

