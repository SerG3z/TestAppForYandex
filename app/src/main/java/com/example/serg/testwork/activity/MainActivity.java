package com.example.serg.testwork.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.decoration.DividerItemDecoration;
import com.example.serg.testwork.fragments.ErrorConnectionFragment;
import com.example.serg.testwork.models.Artist;
import com.example.serg.testwork.service.ArtistService;
import com.example.serg.testwork.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity
        implements ErrorConnectionFragment.ErrorConnectionFragmentListener {

    private static final String TAG_ERROR_CONNECTION = "error_connection_fragment";
    private static final String SAVE_LISTARTIST_KEY = "save_list_artist";
    private static final String LOG_TAG = "Error download";
    @Bind(R.id.first_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    //    local cache
    private List<Artist> artistListCache = new ArrayList<>();

    private RecyclerViewItemListAdapter adapter;
    private FragmentManager fragmentManager;
    private ArtistService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewItemListAdapter(getBaseContext());
        fragmentManager = getSupportFragmentManager();

        service = ServiceFactory.createRetrofitService(ArtistService.class,
                ArtistService.SERVICE_ENDPOINT);

        if (savedInstanceState != null) {
            artistListCache = savedInstanceState.getParcelableArrayList(SAVE_LISTARTIST_KEY);
            adapter.addAllData(artistListCache);
        } else {
            downloadDate(service);
        }


        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewItemListAdapter
                .RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Artist artist = adapter.getItem(position);
                ImageView imageView = (ImageView) view.findViewById(R.id.image_details);
                Bitmap imageArtist = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                Intent intent = ArtistDetailsActivity.newIntent(getApplicationContext(),
                        artist, imageArtist);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Pair<View, String> p1 = Pair.create(view.findViewById(R.id.image_details),
                            getString(R.string.image_trans));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MainActivity.this, p1);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadDate(service);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*
    * downloads data from json and adds data in adapter and local cache
    * if download error and cache is empty, then show error fragment
    * */
    private void downloadDate(ArtistService service) {
        showIndicationDownload();
        service.getDataArtist()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Artist>>() {

                    @Override
                    public final void onCompleted() {
                        removeErrorFragment();
//                        updated local cache
                        artistListCache.clear();
                        artistListCache.addAll(adapter.getAllData());
                        //stop animation downloads
                        hideIndicationDownload();
                        Toast.makeText(getApplicationContext(),
                                R.string.text_toast_download_complete,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(LOG_TAG, e.getMessage());

                        //stop animation downloads
                        hideIndicationDownload();

                        /*
                        * if local cache isn't empty
                        * */
                        if (artistListCache.size() > 0) {
                            adapter.addAllData(artistListCache);
                            Toast.makeText(getApplicationContext(),
                                    R.string.text_toast_error,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            addErrorConnection();
                        }
                    }

                    @Override
                    public void onNext(List<Artist> artists) {
                        adapter.clear();
                        adapter.addAllData(artists);
                    }
                });
    }


    private void showIndicationDownload() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    private void hideIndicationDownload() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    /*
    * remove fragment if connection true
    * */
    private void removeErrorFragment() {
        ErrorConnectionFragment errorConnectionFragment = (ErrorConnectionFragment) fragmentManager.findFragmentByTag(TAG_ERROR_CONNECTION);
        if (errorConnectionFragment != null) {
            fragmentManager.beginTransaction()
                    .remove(errorConnectionFragment)
                    .commit();
        }
    }

    /*
    * add and show fragment if connection error
    * */
    private void addErrorConnection() {
        ErrorConnectionFragment errorConnectionFragment = ErrorConnectionFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.container, errorConnectionFragment, TAG_ERROR_CONNECTION)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_LISTARTIST_KEY,
                (ArrayList<? extends Parcelable>) artistListCache);
    }

    @Override
    public void onErrorConnectiontClicked() {
        downloadDate(service);
    }
}
