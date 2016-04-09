package com.example.serg.testwork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.models.Artist;
import com.example.serg.testwork.service.ArtistService;
import com.example.serg.testwork.service.ServiceFactory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.first_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final RecyclerViewItemListAdapter adapter = new RecyclerViewItemListAdapter();
        recyclerView.setAdapter(adapter);

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
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
