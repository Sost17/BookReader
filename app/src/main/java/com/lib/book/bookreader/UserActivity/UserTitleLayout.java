package com.lib.book.bookreader.UserActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lib.book.bookreader.R;

public class UserTitleLayout extends LinearLayout {
    public UserTitleLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_usertitle,this);
    }
}
