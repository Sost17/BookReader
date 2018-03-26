package com.lib.book.bookreader.ClickListener;

import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);

    public abstract static class NoDoubleItemClickListener implements AdapterView.OnItemClickListener {
        public static final int MIN_CLICK_DELAY_TIME = 5000;
        private long lastClickTime = 0;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(parent,view,position,id);
            }
        }


        public abstract void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id);
    }
}
