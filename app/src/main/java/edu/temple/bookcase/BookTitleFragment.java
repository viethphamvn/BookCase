package edu.temple.bookcase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookTitleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "bookTitle";

    // TODO: Rename and change types of parameters
    private String bookTitle;

//    private OnFragmentInteractionListener mListener;

    public BookTitleFragment() {
        // Required empty public constructor
    }

    public static BookTitleFragment newInstance(String bookTitle) {
        BookTitleFragment fragment = new BookTitleFragment();
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
        View v = inflater.inflate(R.layout.fragment_book_title_pager, container, false);
        TextView textView = v.findViewById(R.id.textView);
        textView.setText(bookTitle);

        return v;
    }

}
