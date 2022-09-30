package com.am.induster.SupportingFiles.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class TelegraphBoldEdiText extends androidx.appcompat.widget.AppCompatEditText {

    public TelegraphBoldEdiText(Context context)
    {
        super(context);
        initTypeface(context);
    }

    public TelegraphBoldEdiText(Context context,
                                AttributeSet attrs)
    {
        super(context, attrs);
        initTypeface(context);
    }

    public TelegraphBoldEdiText(Context context,
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
                "Telegraf-Bold.otf");
        this.setTypeface(tf);
    }
}