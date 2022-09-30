package com.am.induster.SupportingFiles.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

public class TelegraphMediumTextView extends
        androidx.appcompat.widget.AppCompatTextView {

    public TelegraphMediumTextView(Context context)
    {
        super(context);
        initTypeface(context);
    }

    public TelegraphMediumTextView(Context context,
                                 AttributeSet attrs)
    {
        super(context, attrs);
        initTypeface(context);
    }

    public TelegraphMediumTextView(Context context,
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
                "Telegraf-Medium.otf");
        this.setTypeface(tf);
    }
}




