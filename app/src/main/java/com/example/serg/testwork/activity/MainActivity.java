package com.example.serg.testwork.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.decoration.DividerItemDecoration;
import com.example.serg.testwork.models.Artist;
import com.example.serg.testwork.service.ArtistService;
import com.example.serg.testwork.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String SAVE_LISTARTIST_KEY = "save_list_artist";
    @Bind(R.id.first_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    List<Artist> artistList = new ArrayList<>();
    RecyclerViewItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewItemListAdapter();

        if (savedInstanceState != null) {
            artistList = savedInstanceState.getParcelableArrayList(SAVE_LISTARTIST_KEY);
            adapter.addAllData(artistList);
        } else {
            downloadDate();
        }


        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewItemListAdapter
                .RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = ArtistDetailsActivity.newIntent(getApplicationContext(),
                        adapter.getItem(position));
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadDate();
            }
        });
    }

    /*
    * downloads data from json and adds data in adapter and local cache
    *
    * */
    private void downloadDate() {
        ArtistService service = ServiceFactory.createRetrofitService(ArtistService.class,
                ArtistService.SERVICE_ENDPOINT);
        service.getDataArtist()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Artist>>() {
                    @Override
                    public final void onCompleted() {
                        artistList.clear();
                        artistList.addAll(adapter.getAllData());
                        //stop animation downloads
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("Error download", e.getMessage());
                        if (artistList.size() > 0) {
                            adapter.addAllData(artistList);
                            Toast.makeText(getApplicationContext(),
                                    "Проблема с сетью, загружены данные из кэша",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Проблема с сетью, проблема с кэшем, кэш пуст",
                                    Toast.LENGTH_LONG).show();

                        }
                        //stop animation downloads
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Artist> githubses) {
                        adapter.clear();
                        for (Artist item : githubses) {
                            adapter.addData(item);
                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_LISTARTIST_KEY,
                (ArrayList<? extends Parcelable>) artistList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
