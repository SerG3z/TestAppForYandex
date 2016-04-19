package com.example.serg.testwork.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.models.Artist;

import butterknife.Bind;

//import com.squareup.picasso.Picasso;

/**
 * Created by serg on 09.04.16.
 */
public class ArtistDetailsActivity extends BaseActivity {

    @Bind(R.id.image_details)
    ImageView imageView;
    @Bind(R.id.music_details)
    TextView typeMusic;
    @Bind(R.id.info_details)
    TextView typeInfo;
    @Bind(R.id.biography_info_details)
    TextView biographyInfo;

    private static final String ARTIST_DATA_KEY = "artist_data";

    /*
    * returns intent, which must be passed in this Activity
    * */
    public static Intent newIntent(Context context, Artist artist) {
        Intent intent = new Intent(context, ArtistDetailsActivity.class);
        intent.putExtra(ARTIST_DATA_KEY, artist);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.artist_details);
        super.onCreate(savedInstanceState);

        Artist artist = initIntent(getIntent());

        Glide.with(getApplicationContext())
                .load(artist.getCover().getBig())
//                .placeholder(R.drawable.loader2)
                //gif animation download
//                .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loader2))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade(500)
                .into(imageView);

        typeMusic.setText(RecyclerViewItemListAdapter.getStringGenres(artist.getGenres()));
        typeInfo.setText(RecyclerViewItemListAdapter.getStringAlbumsAndTrack(
                getApplicationContext(), artist.getAlbums(), artist.getTracks()));
        biographyInfo.setText(artist.getDescription());

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private Artist initIntent(Intent intent) {
        if (intent != null) {
            return intent.getParcelableExtra(ARTIST_DATA_KEY);
        } else {
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
