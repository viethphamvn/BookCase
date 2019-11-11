package edu.temple.bookcase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface, BookPagerFragment.BookPagerInterface {
    static ArrayList<Book> bookCollection = new ArrayList<>();
    static int currentDisplayedBook = 0;
    private JSONArray bookJSON;
    private String apiURL = "https://kamorris.com/lab/audlib/booksearch.php";
    private String apiURLs = "https://kamorris.com/lab/audlib/booksearch.php?search=";

    Handler getBookHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            try {
                bookJSON = new JSONArray(msg.obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initializeBook(bookJSON);
            //TODO: if this succeed then assign to static bookCollection --> use temp collection
            if (bookCollection.size() > 0) {
                FrameLayout detailContainer = findViewById(R.id.detailContainer);

                BookListFragment bookList;
                BookPagerFragment bookPager;

                if (detailContainer instanceof FrameLayout) { //either landscape or big screen
                    Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("BookListFragment");
                    if (fragment1 == null) {
                        bookList = BookListFragment.newInstance(bookCollection);
                    } else {
                        bookList = (BookListFragment) fragment1;
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, bookList)
                            .commit();
                } else { //single panel display
                    Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("BookPagerFragment");
                    //
                    if (fragment1 == null) {
                        bookPager = BookPagerFragment.newInstance(bookCollection, 0);
                    } else {
                        bookPager = (BookPagerFragment) fragment1;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pagerContainer, bookPager)
                            .commit();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                    "Can't find requested book",
                    Toast.LENGTH_LONG).show();
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (bookCollection.size() > 0){
            FrameLayout pagerContainer = findViewById(R.id.pagerContainer);
            if (pagerContainer instanceof FrameLayout){ //if portrait
                BookPagerFragment bookPager = BookPagerFragment.newInstance(bookCollection, currentDisplayedBook);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.pagerContainer, bookPager)
                        .commit();
            } else { //if landscape
                BookListFragment bookList = BookListFragment.newInstance(bookCollection);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, bookList)
                        .commit();
                onBookSelected(currentDisplayedBook);
            }
        }

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url;
                            EditText searchBox = findViewById(R.id.searchBox);
                            if (searchBox.getText().toString().isEmpty()){
                                url = new URL(apiURL);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(apiURLs);
                                sb.append(searchBox.getText().toString());
                                url = new URL(sb.toString());
                            }
                            BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));
                            StringBuilder builder = new StringBuilder();
                            String response = "";
                            while ((response = rd.readLine()) != null){
                                builder.append(response);
                            }
                            Message msg = Message.obtain(); //changed from getBookHandler.obtain()
                            msg.obj = builder.toString();
                            getBookHandler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                t.start();
            }
        });

    }

    public void initializeBook(JSONArray js){
        bookCollection.clear();
        for (int i = 0; i < js.length(); i++){
            try {
                JSONObject e = js.getJSONObject(i);
                Book b = new Book(
                        e.getInt("book_id"),
                        e.getString("title"),
                        e.getString("author"),
                        e.getInt("published"),
                        e.getString("cover_url")
                );
                bookCollection.add(b);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBookSelected(int position) {
        currentDisplayedBook = position;
        BookDetailFragment fragment = (BookDetailFragment) getSupportFragmentManager().findFragmentByTag("BookDetailFragment");
        if (fragment != null){
            fragment.displayBook(bookCollection.get(position));
        } else {
            fragment = BookDetailFragment.newInstance(bookCollection.get(position));
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detailContainer, fragment)
                .commit();
    }

    @Override
    public void onPageSelect(int position) {
        currentDisplayedBook = position;
    }
}
