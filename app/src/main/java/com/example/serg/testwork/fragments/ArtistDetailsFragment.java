package com.example.serg.testwork.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.models.Artist;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 16.07.16.
 */

public class ArtistDetailsFragment extends Fragment {

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

    private static final String CUSTOM_SESSION = "android.support.customtabs.extra.SESSION";
    private static final String TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    private static final String KEY_PARCELABLE_DATA = "parcelable_data";

    private Artist artist;

    public static ArtistDetailsFragment newInstance(Artist artist) {

        Bundle args = new Bundle();

        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        fragment.setArguments(args);
        args.putParcelable(KEY_PARCELABLE_DATA, artist);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_details_scrolling, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        if (bundle != null) {
            artist = bundle.getParcelable(KEY_PARCELABLE_DATA);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });

        if (artist != null) {

            toolbar.setTitle(artist.getName());

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(context)
                    .load(artist.getCover().getBig())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(imageView);

            typeMusic.setText(RecyclerViewItemListAdapter.getStringGenres(artist.getGenres()));
            typeInfo.setText(RecyclerViewItemListAdapter.getStringAlbumsAndTrack(context,
                    artist.getAlbums(), artist.getTracks(), false));
            biographyInfo.setText(artist.getDescription());
        }

    }


    @OnClick(R.id.floating_button)
    public void onClickFloatingActionButton(View view) {
        int color = ContextCompat.getColor(getContext(), R.color.my_primary);
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
