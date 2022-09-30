package com.am.induster.SupportingFiles.Button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class TelegraphRegularButton extends
        androidx.appcompat.widget.AppCompatButton {

    public TelegraphRegularButton(Context context)
    {
        super(context);
        initTypeface(context);
    }

    public TelegraphRegularButton(Context context,
                                  AttributeSet attrs)
    {
        super(context, attrs);
        initTypeface(context);
    }

    public TelegraphRegularButton(Context context,
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
                "Telegraf-Regular.otf");
        this.setTypeface(tf);
    }
}
