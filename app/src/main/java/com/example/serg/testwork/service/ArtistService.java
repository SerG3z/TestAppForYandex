package com.example.serg.testwork.service;

import com.example.serg.testwork.models.Artist;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by serg on 09.04.16.
 */

public interface ArtistService {
    String SERVICE_ENDPOINT = "http://cache-novosibirsk02.cdn.yandex.net/download.cdn.yandex.net/";

    @GET("/mobilization-2016/{login}")
    Observable<List<Artist>> getUser(@Path("login") String login);
}
