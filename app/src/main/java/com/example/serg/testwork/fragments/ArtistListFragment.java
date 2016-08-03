package com.example.serg.testwork.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.example.serg.testwork.R;
import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.decoration.DividerItemDecoration;
import com.example.serg.testwork.models.Artist;

import java.util.ArrayList;

import butterknife.Bind;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by user on 16.07.16.
 */

public class ArtistListFragment extends BaseFragment {

    public interface ListArtistFragmentListener {
        void onSettingsClicked();

        void onListAtristClicked(int indexClickArtist);
    }

    private static final String KEY_PARCELABLE_LIST = "parcelable_list";
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.first_recycler_view)
    RecyclerView recyclerView;
    private ListArtistFragmentListener fragmentListener;
    private RecyclerViewItemListAdapter adapter;

    private ArrayList<Artist> artistArrayList = new ArrayList<>();

    public static ArtistListFragment newInstance(ArrayList<Artist> list) {
        Bundle args = new Bundle();
        ArtistListFragment fragment = new ArtistListFragment();
        args.putParcelableArrayList(KEY_PARCELABLE_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_artists, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getActivity();
        toolbar.setTitle(R.string.app_name);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);

        Bundle bundle = getArguments();
        if (bundle != null) {
            artistArrayList.clear();
            artistArrayList.addAll(bundle.<Artist>getParcelableArrayList(KEY_PARCELABLE_LIST));
        }

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewItemListAdapter(context);
        adapter.addAllData(artistArrayList);

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());

        recyclerView.setAdapter(scaleInAnimationAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL));
        adapter.setOnItemClickListener(new RecyclerViewItemListAdapter
                .RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                fragmentListener.onListAtristClicked(position);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Resources resources = getResources();
        switch (item.getItemId()) {
            case R.id.menu_item_feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822")
                        .putExtra(Intent.EXTRA_EMAIL, new String[]{"sergey.atroshchenko@yandex.ru"})
                        .putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject))
                        .putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.email_text));
                try {
                    startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.email_title)));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(this.getContext(), resources.getString(R.string.email_title_error), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_item_about_app:
                DialogFragment fragment = new AboutAppDialogFragment();
                fragment.show(getChildFragmentManager(), null);
                return true;
            case R.id.menu_item_settings:
                fragmentListener.onSettingsClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (ListArtistFragmentListener) context;
    }
}
