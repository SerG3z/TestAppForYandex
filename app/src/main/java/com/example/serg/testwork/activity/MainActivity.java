package com.example.serg.testwork.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
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
    List<Artist> artistList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final RecyclerViewItemListAdapter adapter = new RecyclerViewItemListAdapter();

        if (savedInstanceState != null) {
            artistList = savedInstanceState.getParcelableArrayList(SAVE_LISTARTIST_KEY);
                adapter.addAllData(artistList);
        } else {
            ArtistService service = ServiceFactory.createRetrofitService(ArtistService.class, ArtistService.SERVICE_ENDPOINT);
            service.getUser("artists.json")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Artist>>() {
                        @Override
                        public final void onCompleted() {
                            // do nothing
                        }

                        @Override
                        public final void onError(Throwable e) {
                            Log.e("GithubDemo", e.getMessage());
                        }

                        @Override
                        public void onNext(List<Artist> githubses) {
                            for (Artist item : githubses) {
                                adapter.addData(item);
                                artistList.add(item);
                            }
                        }
                    });
        }

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewItemListAdapter.RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = ArtistDetailsActivity.newIntent(getApplicationContext(), adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_LISTARTIST_KEY, (ArrayList<? extends Parcelable>) artistList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
