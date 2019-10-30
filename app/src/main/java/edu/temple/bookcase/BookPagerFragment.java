package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookPagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "bookTitle";

    // TODO: Rename and change types of parameters
    private ArrayList<String> bookTitle;


    public BookPagerFragment() {
        // Required empty public constructor
    }

    public static BookPagerFragment newInstance(ArrayList<String> bookTitle) {
        BookPagerFragment fragment = new BookPagerFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, bookTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookTitle = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_pager, container, false);
        ViewPager viewPager = v.findViewById(R.id.viewPager);
        BookPagerAdapter pagerAdapter = new BookPagerAdapter(getChildFragmentManager(), bookTitle);
        viewPager.setAdapter(pagerAdapter);

        return v;
    }

}
