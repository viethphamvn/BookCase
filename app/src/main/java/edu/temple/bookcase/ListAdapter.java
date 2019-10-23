package edu.temple.bookcase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    ArrayList<String> bookTitle;
    Context c;

    public ListAdapter(Context c, ArrayList<String> bookTitle){
        this.bookTitle = bookTitle;
        this.c = c;
    }

    @Override
    public int getCount() {
        return bookTitle.size();
    }

    @Override
    public Object getItem(int position) {
        return bookTitle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v;
        if (convertView instanceof  View){
            v = (TextView)convertView;
        } else {
            v = new TextView(c);
        }
        v.setTextSize(30);
        v.setText(bookTitle.get(position));
        return v;
    }
}
