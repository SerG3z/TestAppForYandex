package com.example.serg.testwork.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.serg.testwork.R;
import com.example.serg.testwork.fragments.ArtistDetailsFragment;
import com.example.serg.testwork.fragments.ArtistListFragment;
import com.example.serg.testwork.fragments.ErrorConnectionFragment;
import com.example.serg.testwork.fragments.SettingFragment;
import com.example.serg.testwork.manager.Cache;
import com.example.serg.testwork.models.Artist;
import com.example.serg.testwork.service.ArtistService;
import com.example.serg.testwork.service.MusicInputReceiver;
import com.example.serg.testwork.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity
        implements ErrorConnectionFragment.ErrorConnectionFragmentListener,
        ArtistListFragment.ListArtistFragmentListener {

    private static final String TAG_ERROR_CONNECTION = "error_connection_fragment";
    private static final String SAVE_LISTARTIST_KEY = "save_list_artist";
    private static final String LOG_TAG = "Error download";

    // local cache
    private ArrayList<Artist> artistListCache = new ArrayList<>();
    // main data
    private ArrayList<Artist> mainListArtist = new ArrayList<>();

    private FragmentManager fragmentManager;
    private ArtistService service;
    private MusicInputReceiver musicInputReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        if (getPreferenceNotification(getApplicationContext())) {
            addMusicInputReceiver();
        } else {
            removeMusicInputReceiver();
        }

        fragmentManager = getSupportFragmentManager();

        service = ServiceFactory.createRetrofitService(ArtistService.class,
                ArtistService.SERVICE_ENDPOINT);

        if (savedInstanceState != null) {
            artistListCache = savedInstanceState.getParcelableArrayList(SAVE_LISTARTIST_KEY);
            mainListArtist.addAll(artistListCache);

        } else {
            downloadDate(service);
        }
    }

    private boolean getPreferenceNotification(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.preference_notify), true);
    }

    private void addMusicInputReceiver() {
        musicInputReceiver = new MusicInputReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(musicInputReceiver, filter);
    }

    private void removeMusicInputReceiver() {
        if (musicInputReceiver != null) {
            musicInputReceiver.closeNotify();
            unregisterReceiver(musicInputReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeMusicInputReceiver();
    }

    /*
     * downloads data from json and adds data in adapter and local cache
     * if download error and cache is empty, then show error fragment
     * */
    private void downloadDate(ArtistService service) {
        service.getDataArtist()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Artist>>() {

                    @Override
                    public final void onCompleted() {
                        removeErrorFragment();
//                        updated local cache
                        artistListCache.clear();
                        // обновить кэш
                        artistListCache.addAll(mainListArtist);
                        Cache.writeToCache(artistListCache, getApplicationContext());
                        //stop animation downloads
                        addFragmentListArtist();
                        Toast.makeText(getApplicationContext(),
                                R.string.text_toast_download_complete,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(LOG_TAG, e.getMessage());

                        /*
                        * if local cache isn't empty
                        * */
                        try {
                            artistListCache = Cache.readFromCache(getApplicationContext());
                        } catch (Exception ignored) {

                        } finally {
                            if (artistListCache.size() > 0) {
                                //хватаем данные из кэша
                                mainListArtist.addAll(artistListCache);

                                addFragmentListArtist();

                                Toast.makeText(getApplicationContext(),
                                        R.string.text_toast_error,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                addErrorConnection();
                            }
                        }
                    }

                    @Override
                    public void onNext(List<Artist> artists) {
                        // чистим и добавляем данные в адаптер
                        mainListArtist.clear();
                        mainListArtist.addAll(artists);
                    }
                });
    }

    /*
    * remove fragment if connection true
    * */
    private void removeErrorFragment() {
        ErrorConnectionFragment errorConnectionFragment = (ErrorConnectionFragment) fragmentManager.findFragmentByTag(TAG_ERROR_CONNECTION);
        if (errorConnectionFragment != null) {
            fragmentManager.beginTransaction()
                    .remove(errorConnectionFragment)
                    .commit();
        }
    }

    /*
    * add and show fragment if connection error
    * */
    private void addErrorConnection() {
        ErrorConnectionFragment errorConnectionFragment = ErrorConnectionFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, errorConnectionFragment, TAG_ERROR_CONNECTION)
                .commit();
    }

    private void addFragmentListArtist() {
        ArtistListFragment artistListFragment = ArtistListFragment.newInstance(mainListArtist);
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, artistListFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_LISTARTIST_KEY, artistListCache);
    }

    @Override
    public void onErrorConnectiontClicked() {
        downloadDate(service);
    }

    @Override
    public void onSettingsClicked() {
        SettingFragment settingFragment = SettingFragment.newInstance();
        addFragment(settingFragment);
    }

    @Override
    public void onListAtristClicked(int indexClikArtist) {
        ArtistDetailsFragment artistDetailsFragment = ArtistDetailsFragment.newInstance(mainListArtist.get(indexClikArtist));
        addFragment(artistDetailsFragment);
    }

    private void addFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
