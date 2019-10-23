package edu.temple.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class BookListFragment extends Fragment {
    private static final String ARG_PARAM1 = "bookTitle";

    // TODO: Rename and change types of parameters
    private ArrayList<String> bookTitle;

    private BookListFragmentInterface parentFragment;

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance(ArrayList<String> bookTitle) {
        BookListFragment fragment = new BookListFragment();
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
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView listView = v.findViewById(R.id.listView);

        ListAdapter listAdapter = new ListAdapter(getActivity(), bookTitle);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parentFragment.onBookSelected(bookTitle.get(position));
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListFragmentInterface) {
            parentFragment = (BookListFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface BookListFragmentInterface {
        // TODO: Update argument type and name
        void onBookSelected(String bookTitle);
    }
}
