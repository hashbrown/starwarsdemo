package com.bitfarmsoftware.starwars.service.remote;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import com.bitfarmsoftware.starwars.model.Film;
import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.model.UriTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import retrofit.Response;
import retrofit.http.Url;
import rx.Observable;
import timber.log.Timber;


/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class MockStarWarsApi implements StarWarsApi {

    private final Gson gson;
    private final Context ctx;
    private long delayMillis = 0L;

    public MockStarWarsApi() {
        this.ctx = InstrumentationRegistry.getContext();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriTypeAdapter())
                .create();
    }


    @Override
    public Observable<Response<PagedApiResponse<Starship>>> getStarships() {
        try {
            InputStream response = ctx.getAssets().open("starships.json");
            TypeToken<PagedApiResponse<Starship>> responseType = new TypeToken<PagedApiResponse<Starship>>(){};
            PagedApiResponse<Starship> pagedResponse = gson.fromJson(new JsonReader(new InputStreamReader(response)), responseType.getType());
            return Observable.just(Response.success(pagedResponse)).delay(delayMillis, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            return Observable.just(Response.<PagedApiResponse<Starship>>error(500, ResponseBody.create(MediaType.parse("text/plain"), e.getMessage())));
        }
    }

    @Override
    public Observable<Response<Film>> getFilmByUrl(@Url final String filmUrl) {

        // strip film_id from url, eg http://swapi.co/api/films/1/
        String filmId = filmUrl.substring(filmUrl.length() - 2, filmUrl.length() -1);
        String filename = String.format("film_%s.json",filmId);
        try {
            InputStream response = ctx.getAssets().open(filename);
            TypeToken<PagedApiResponse<Starship>> responseType = new TypeToken<PagedApiResponse<Starship>>(){};
            Film film = gson.fromJson(new JsonReader(new InputStreamReader(response)), Film.class);
            return Observable.just(Response.success(film)).delay(delayMillis, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            return Observable.just(Response.<Film>error(500, ResponseBody.create(MediaType.parse("text/plain"), e.getMessage())));
        }
    }

    public void setNetworkDelay(long delayMillis){
        this.delayMillis = delayMillis;
    }
}
