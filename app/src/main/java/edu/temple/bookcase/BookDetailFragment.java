package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "bookTitle";
    private String bookTitle;
    private View v;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    public static BookDetailFragment newInstance(String bookTitle) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, bookTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookTitle = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_book_detail, container, false);
        TextView textView = v.findViewById(R.id.textView);
        if (getArguments() == null){

        } else {
            String displayText = getArguments().getString(ARG_PARAM1);
            textView.setText(displayText);
            textView.setTextSize(60);
        }
        return v;
    }

    public void setText(String bookTitle){
        TextView textView = v.findViewById(R.id.textView);
        textView.setText(bookTitle);
        textView.setTextSize(60);
    }
}
