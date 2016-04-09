package com.example.serg.testwork.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serg.testwork.R;
import com.example.serg.testwork.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by serg on 09.04.16.
 */
public class ArtistDetailsFragment extends Fragment {

    @Bind(R.id.image_details)
    ImageView imageView;
    @Bind(R.id.music_details)
    TextView typeMusic;
    @Bind(R.id.info_details)
    TextView typeInfo;
    @Bind(R.id.biografy_info_details)
    TextView biografyInfo;

    Artist artist;
    Context context;

    private static final String ARTIST_DATA_KEY = "artist_data";

    public static ArtistDetailsFragment newInstance(Artist artist) {
        Bundle args = new Bundle();
        args.putParcelable(ARTIST_DATA_KEY, artist);
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.artist_details, container, false);
        ButterKnife.bind(this, view);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artist = getArguments().getParcelable(ARTIST_DATA_KEY);

        Picasso.with(context)
                .load(artist.getCover().getBig())
                .into(imageView);

        typeMusic.setText(Arrays.toString(artist.getGenres()));
        typeInfo.setText(artist.getTracks() + " " + artist.getAlbums());
        biografyInfo.setText(artist.getDescription());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
