package com.am.induster.SupportingFiles.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class TelegraphSemiBoldEditText extends
        androidx.appcompat.widget.AppCompatEditText {

    public TelegraphSemiBoldEditText(Context context)
    {
        super(context);
        initTypeface(context);
    }

    public TelegraphSemiBoldEditText(Context context,
                                     AttributeSet attrs)
    {
        super(context, attrs);
        initTypeface(context);
    }

    public TelegraphSemiBoldEditText(Context context,
                                     AttributeSet attrs,
                                     int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initTypeface(context);
    }

    private void initTypeface(Context context)
    {
        Typeface tf = Typeface.createFromAsset(
                context.getAssets(),
                "Telegraf-SemiBold.otf");
        this.setTypeface(tf);
    }
}
