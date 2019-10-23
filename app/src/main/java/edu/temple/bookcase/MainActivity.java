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
            ViewPager viewPager = findViewById(R.id.viewPager);
            viewPager.setAdapter(new BookPagerAdapter(getSupportFragmentManager(), bookTitle));
        }

    }

    @Override
    public void onBookSelected(String bookTitle) {
        Bundle bundle = new Bundle();
        bundle.putString("bookTitle", bookTitle);
        BookDetailFragment bookDetail = new BookDetailFragment();
        bookDetail.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detailContainer, bookDetail)
                .commit();
    }
}
