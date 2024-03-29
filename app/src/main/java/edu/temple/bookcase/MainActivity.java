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
import java.lang.reflect.Array;
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
                currentDisplayedBook = 0;
                FrameLayout detailContainer = findViewById(R.id.detailContainer);

                if (detailContainer instanceof FrameLayout) { //either landscape or big screen
                    BookListFragment fragment1 = (BookListFragment) getSupportFragmentManager().findFragmentByTag("bookListFragment");

                    if (fragment1 == null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragmentContainer, BookListFragment.newInstance(bookCollection), "bookListFragment")
                                .commit();
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(fragment1)
                                .add(R.id.fragmentContainer, BookListFragment.newInstance(bookCollection), "bookListFragment")
                                .commit();
                    }
                } else { //single panel display
                    BookPagerFragment fragment1 = (BookPagerFragment)getSupportFragmentManager().findFragmentByTag("bookPagerFragment");
                    //
                    if (fragment1 == null) {
                        Log.v("error","here");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.pagerContainer, BookPagerFragment.newInstance(bookCollection, 0), "bookPagerFragment")
                                .commit();
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(fragment1)
                                .replace(R.id.pagerContainer, BookPagerFragment.newInstance(bookCollection, 0), "bookPagerFragment")
                                .commit();
                    }
                }
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ArrayList<Book> bookCol = new ArrayList<>();
        FrameLayout pagerContainer = findViewById(R.id.pagerContainer);
        if (pagerContainer instanceof FrameLayout){ //if portrait
            BookListFragment fragment = (BookListFragment) getSupportFragmentManager().findFragmentByTag("bookListFragment");
            if (fragment != null){
                bookCol = fragment.getBook();
            }
            BookPagerFragment fragment1 = (BookPagerFragment) getSupportFragmentManager().findFragmentByTag("bookPagerFragment");
            if (fragment1 != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragment1)
                        .commit();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pagerContainer, BookPagerFragment.newInstance(bookCol, currentDisplayedBook), "bookPagerFragment")
                    .commit();
        } else { //if landscape
            BookPagerFragment fragment = (BookPagerFragment) getSupportFragmentManager().findFragmentByTag("bookPagerFragment");
            if (fragment != null){
                bookCol = fragment.getBook();
            }
            BookListFragment fragment1 = (BookListFragment) getSupportFragmentManager().findFragmentByTag("bookListFragment");
            //TODO: This will be NULL --> Fix
            if (fragment1 != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragment1)
                        .commit();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, BookListFragment.newInstance(bookCol), "bookListFragment")
                    .commit();
            onBookSelected(currentDisplayedBook, bookCol);
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
        if (js.length() > 0) {
            bookCollection.clear();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Can't find requested book",
                    Toast.LENGTH_LONG).show();
        }
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
    public void onBookSelected(int position, ArrayList<Book> bookCol) {
        currentDisplayedBook = position;
        BookDetailFragment fragment = (BookDetailFragment) getSupportFragmentManager().findFragmentByTag("bookDetailFragment");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        if (position < bookCol.size()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, BookDetailFragment.newInstance(bookCol.get(position)), "bookDetailFragment")
                    .commit();
        }
    }

    @Override
    public void onPageSelect(int position) {
        currentDisplayedBook = position;
    }
}
