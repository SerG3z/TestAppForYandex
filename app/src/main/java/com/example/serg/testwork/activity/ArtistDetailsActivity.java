package com.example.serg.testwork.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.models.Artist;

import butterknife.Bind;
import butterknife.ButterKnife;

//import com.squareup.picasso.Picasso;

/**
 * Created by serg on 09.04.16.
 */
public class ArtistDetailsActivity extends AppCompatActivity {

    @Bind(R.id.image_details)
    ImageView imageView;
    @Bind(R.id.music_details)
    TextView typeMusic;
    @Bind(R.id.info_details)
    TextView typeInfo;
    @Bind(R.id.biografy_info_details)
    TextView biografyInfo;

    Artist artist;
    android.support.v7.app.ActionBar actionBar;

    private static final String ARTIST_DATA_KEY = "artist_data";

    public static Intent newIntent(Context context, Artist artist) {
        Intent intent = new Intent(context, ArtistDetailsActivity.class);
        intent.putExtra(ARTIST_DATA_KEY, artist);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_details);
        ButterKnife.bind(this);
        initIntent(getIntent());

        Glide.with(getApplicationContext())
                .load(artist.getCover().getBig())
                .crossFade()
                .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loader8))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        typeMusic.setText(RecyclerViewItemListAdapter.getStringGenres(artist.getGenres()));
        typeInfo.setText(RecyclerViewItemListAdapter.getStringAlbumsAndTrack(
                getApplicationContext(), artist.getAlbums(), artist.getTracks()));
        biografyInfo.setText(artist.getDescription());

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(artist.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initIntent(Intent intent) {
        if (intent != null) {
            artist = intent.getParcelableExtra(ARTIST_DATA_KEY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
