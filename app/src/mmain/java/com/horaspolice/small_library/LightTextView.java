package com.horaspolice.small_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.horaspolice.R;


@SuppressLint("AppCompatCustomView")
public class LightTextView extends TextView {

    public LightTextView(Context context) {
        super(context);
    }

    public LightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(context.getString(R.string.lang).equals("ar")) {
            setTypeface(FontManger.yad);
        }if (context.getString(R.string.lang).equals("zh")) {
            setTypeface(FontManger.asian);
        }if (context.getString(R.string.lang).equals("cs")){
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("nl")){
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("en")){
            setTypeface(FontManger.english);
        } if(context.getString(R.string.lang).equals("fr")) {
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("de")) {
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("in")){
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("it")){
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("ms")){
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("hi")){
            setTypeface(FontManger.hindi);
        }if (context.getString(R.string.lang).equals("ja")){
            setTypeface(FontManger.asian);
        }if (context.getString(R.string.lang).equals("ko")){
            setTypeface(FontManger.asian);
        }if (context.getString(R.string.lang).equals("fa")){
            setTypeface(FontManger.yad);
        }if (context.getString(R.string.lang).equals("pl")){
            setTypeface(FontManger.english);
        } if(context.getString(R.string.lang).equals("pt")) {
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("ro")) {
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("ru")){
            setTypeface(FontManger.russi);
        }if (context.getString(R.string.lang).equals("es")){
            setTypeface(FontManger.english);
        }if (context.getString(R.string.lang).equals("th")){
            setTypeface(FontManger.english);
        }else if (context.getString(R.string.lang).equals("tr"))
            setTypeface(FontManger.english);
    }
}


