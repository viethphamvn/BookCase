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

    public BookDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_detail, container, false);
        TextView textView = v.findViewById(R.id.textView);
        if (getArguments() == null){

        } else {
            String displayText = getArguments().getString(ARG_PARAM1);
            textView.setText(displayText);
            textView.setTextSize(60);
        }
        return v;
    }

}
