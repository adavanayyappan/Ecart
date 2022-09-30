package com.am.induster.SupportingFiles.Button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class TelegraphMediumButton extends
        androidx.appcompat.widget.AppCompatButton {

    public TelegraphMediumButton(Context context)
    {
        super(context);
        initTypeface(context);
    }

    public TelegraphMediumButton(Context context,
                                 AttributeSet attrs)
    {
        super(context, attrs);
        initTypeface(context);
    }

    public TelegraphMediumButton(Context context,
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
