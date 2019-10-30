package edu.temple.bookcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> bookTitle = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.bookTitle)));
        FrameLayout detailContainer = findViewById(R.id.detailContainer);

        BookListFragment bookList;
        BookPagerFragment bookPager;

        if (detailContainer instanceof FrameLayout){ //either landscape or big screen
            Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("BookListFragment");
            if (fragment1 == null){
                bookList = BookListFragment.newInstance(bookTitle);
            } else {
                bookList = (BookListFragment)fragment1;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, bookList)
                    .commit();
        } else { //single panel display
            Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("BookPagerFragment");

            if (fragment1 == null){
                bookPager = BookPagerFragment.newInstance(bookTitle);
            } else {
                bookPager = (BookPagerFragment)fragment1;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pagerContainer, bookPager)
                    .commit();
        }
    }

    @Override
    public void onBookSelected(String bookTitle) {
        BookDetailFragment fragment = (BookDetailFragment) getSupportFragmentManager().findFragmentByTag("BookDetailFragment");
        if (fragment != null){
            fragment.setText(bookTitle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("bookTitle", bookTitle);
            fragment = BookDetailFragment.newInstance(bookTitle);
            fragment.setArguments(bundle);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detailContainer, fragment)
                .commit();
    }
}
