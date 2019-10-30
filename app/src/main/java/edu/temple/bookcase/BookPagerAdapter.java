package edu.temple.bookcase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class BookPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> bookTitle;

    public BookPagerAdapter(@NonNull FragmentManager fm, ArrayList<String> bookTitle) {
        super(fm);
        this.bookTitle = bookTitle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        BookPagerFragment fragment = BookPagerFragment.newInstance(bookTitle.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return bookTitle.size();
    }
}