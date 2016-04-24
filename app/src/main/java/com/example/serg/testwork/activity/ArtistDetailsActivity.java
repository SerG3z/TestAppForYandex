package com.example.serg.testwork.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.models.Artist;

import butterknife.Bind;
import butterknife.OnClick;

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    Artist artist;
    Bitmap imageArtist;
    private static final String CUSTOM_SESSION = "android.support.customtabs.extra.SESSION";
    private static final String TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    private static final String ARTIST_DATA_KEY = "artist_data";
    private static final String IMAGE_ARTIST_KEY = "image_artist";

    /*
    * returns intent, which must be passed in this Activity
    * */
    public static Intent newIntent(Context context, Artist artist, Bitmap bitmap) {
        Intent intent = new Intent(context, ArtistDetailsActivity.class);
        intent.putExtra(ARTIST_DATA_KEY, (Parcelable) artist);
        intent.putExtra(IMAGE_ARTIST_KEY, bitmap);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.artist_details_scrolling);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initIntent(getIntent());
        if (artist != null) {

            toolbar.setTitle(artist.getName());

            imageView.setPadding(0, 0, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Drawable drawable = new BitmapDrawable(imageArtist);
            Glide.with(getApplicationContext())
                    .load(artist.getCover().getBig())
                    .placeholder(drawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(imageView);

            typeMusic.setText(RecyclerViewItemListAdapter.getStringGenres(artist.getGenres()));
            typeInfo.setText(RecyclerViewItemListAdapter.getStringAlbumsAndTrack(
                    getApplicationContext(), artist.getAlbums(), artist.getTracks(), false));
            biographyInfo.setText(artist.getDescription());
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.floating_button)
    public void onClickFloatingActionButton(View view) {
        int color = ContextCompat.getColor(this, R.color.my_primary);
        if (artist.getLink() != null) {
            Uri address = Uri.parse(artist.getLink());
            Intent browseIntent = new Intent(Intent.ACTION_VIEW, address);
            Bundle extras = new Bundle();
            extras.putBinder(CUSTOM_SESSION, null);
            extras.putInt(TOOLBAR_COLOR, color);
            browseIntent.putExtras(extras);
            startActivity(browseIntent);
        } else {
            Snackbar snackbar = Snackbar.make(view, R.string.error_link, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(color);
            snackbar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initIntent(Intent intent) {
        if (intent != null) {
            artist = intent.getParcelableExtra(ARTIST_DATA_KEY);
            imageArtist = intent.getParcelableExtra(IMAGE_ARTIST_KEY);
        }
    }
}
