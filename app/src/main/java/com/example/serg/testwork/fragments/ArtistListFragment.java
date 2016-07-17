package com.example.serg.testwork.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.decoration.DividerItemDecoration;
import com.example.serg.testwork.models.Artist;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 16.07.16.
 */

public class ArtistListFragment extends Fragment {

    private ListArtistFragmentListener fragmentListener;

    @Bind(R.id.first_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String KEY_PARCELABLE_LIST = "parcelable_list";

    private RecyclerViewItemListAdapter adapter;
    private ArrayList<Artist> artistArrayList = new ArrayList<>();

    public static ArtistListFragment newInstance(ArrayList<Artist> list) {
        Bundle args = new Bundle();
        ArtistListFragment fragment = new ArtistListFragment();
        args.putParcelableArrayList(KEY_PARCELABLE_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_artists, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        if (bundle != null) {
            artistArrayList.addAll(bundle.<Artist>getParcelableArrayList(KEY_PARCELABLE_LIST));
        }

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewItemListAdapter(getContext());

        adapter.addAllData(artistArrayList);

        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewItemListAdapter
                .RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                fragmentListener.onListAtristClicked(position);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (ListArtistFragmentListener) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public interface ListArtistFragmentListener {
        void onListAtristClicked(int indexClickArtist);
    }
}
