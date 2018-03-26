package com.lib.book.bookreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lib.book.bookreader.R;
import com.lib.book.bookreader.base.Bookview;

import java.util.ArrayList;
import java.util.HashMap;

public class BookAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> data;

    private LayoutInflater layoutInflater;
    private Context context;

    public BookAdapter(Context context, ArrayList<HashMap<String, Object>> data) {

        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Bookview bookview = null;
        final int selectId = position;
        if(convertView == null){
            bookview = new Bookview();
            convertView = layoutInflater.inflate(R.layout.item_book,null);
            bookview.book_name_view = convertView.findViewById(R.id.book_name);
            bookview.bookItemLinearLayout = convertView.findViewById(R.id.bookItemLinearLayout);
            convertView.setTag(bookview);
        } else {
            bookview = (Bookview) convertView.getTag();
        }

        bookview.book_name_view.setText((String)data.get(position).get("name"));

        return convertView;
    }
}
